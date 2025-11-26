package com.domus.challenge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.domus.challenge.service.IDirectorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class DirectorController {

	private final IDirectorService service;

	public DirectorController(IDirectorService service) {
		this.service = service;
	}

	@Operation(
			summary = "List directors who have directed more movies than the given threshold",
			description = "Returns directors sorted alphabetically. Threshold must be a non-negative integer."
			)
	@GetMapping("/directors")
	public Mono<ResponseEntity<Map<String, List<String>>>> getDirectors(
			@Parameter(description = "Integer threshold")
			@RequestParam(name = "threshold") String thresholdParam) {

		int threshold;

		//VALIDATION: threshold must be a valid integer number
		try {
			threshold = Integer.parseInt(thresholdParam);
		} catch (NumberFormatException ex) {
			return Mono.just(ResponseEntity
							.badRequest()
							.body(Map.of("error", List.of("threshold must be an integer"))));
		}
		
		return this.service.findDirectorsAboveThreshold(threshold)
				.map(list -> ResponseEntity.ok(Map.of("directors", list)));
	}

}
