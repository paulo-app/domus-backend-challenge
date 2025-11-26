package com.domus.challenge.controller;

import com.domus.challenge.service.IDirectorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.List;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = DirectorController.class)
public class DirectorControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private IDirectorService directorService;

	@TestConfiguration
	static class MockConfig {
		@Bean
		IDirectorService directorService() {
			return Mockito.mock(IDirectorService.class);
		}
	}

	@Test
	void testValidThresholdReturnsDirectors() {

		when(directorService.findDirectorsAboveThreshold(2))
			.thenReturn(Mono.just(List.of("Nolan", "Tarantino")));

		webTestClient.get().uri("/api/directors?threshold=2")
			.exchange()
			.expectStatus()
			.isOk().expectBody()
			.jsonPath("$.directors[0]")
			.isEqualTo("Nolan");
	}

	@Test
	void testInvalidThresholdReturns400() {

		webTestClient.get().uri("/api/directors?threshold=abc")
			.exchange()
			.expectStatus()
			.isBadRequest()
			.expectBody()
			.jsonPath("$.error[0]")
			.isEqualTo("threshold must be an integer");
	}
	
	@Test
	void testNegativeThresholdReturnsEmptyList() {

		when(directorService.findDirectorsAboveThreshold(-1)).thenReturn(Mono.just(List.of()));

		webTestClient.get().uri("/api/directors?threshold=-1")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody()
			.jsonPath("$.directors")
			.isEmpty();
	}
}
