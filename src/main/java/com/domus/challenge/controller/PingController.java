package com.domus.challenge.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class PingController {
	
	@Operation(summary = "Shows that the application is up and running",
			   description = "Returns a pong message and the current datetime")		
	@GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> ping() {
		
		return Map.of(
        		"status", "ok",
        	    "message", "application is up and running",
        	    "datetime", Instant.now().truncatedTo(ChronoUnit.SECONDS).toString()
        	);
	}
	
}
