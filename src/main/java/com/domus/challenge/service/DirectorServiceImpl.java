package com.domus.challenge.service;

import com.domus.challenge.dto.RemoteMovie;
import com.domus.challenge.dto.RemotePageResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DirectorServiceImpl implements IDirectorService {

	private final WebClient moviesApiClient;
	private static final int CONCURRENCY = 5;

	public DirectorServiceImpl(WebClient moviesApiClient) {
		this.moviesApiClient = moviesApiClient;
	}

	@Override
	public Mono<List<String>> findDirectorsAboveThreshold(int threshold) {

		//If the threshold is negative, return an empty list
		if (threshold < 0) {
			return Mono.just(Collections.emptyList());
		}

		//Requests the first page just once
		return moviesApiClient.get()
				.uri("/api/movies/search?page=1")
				.retrieve()
				.bodyToMono(RemotePageResponse.class)
				.flatMapMany(firstPage -> {

					int totalPages = firstPage.getTotal_pages();

					//It gets data from the first page (already obtained)
					var firstPageData = Flux.fromIterable(firstPage.getData()); //Flux<RemoteMovie>
					
					if (totalPages == 1) {
						return firstPageData;
					}

					//Fetches only remaining pages: from 2 to totalPages
					var remainingPages = Flux.range(2, totalPages - 1)
							.flatMap(page -> moviesApiClient.get().uri("/api/movies/search?page=" + page)
							.retrieve()
							.bodyToMono(RemotePageResponse.class)
							.map(RemotePageResponse::getData)
							.flatMapMany(Flux::fromIterable), CONCURRENCY); //Flux<RemoteMovie>
					
					return Flux.concat(firstPageData, remainingPages);
				})
				.map(RemoteMovie::getDirector)
				.filter(Objects::nonNull)
				.map(String::trim)
				.collect(Collectors.toMap(director -> director, director -> 1, Integer::sum))
				.map(map -> map.entrySet().stream()
						.filter(entry -> entry.getValue() > threshold)
						.map(Map.Entry::getKey)
						.sorted(String::compareToIgnoreCase)
						.collect(Collectors.toList()));
	}
}
