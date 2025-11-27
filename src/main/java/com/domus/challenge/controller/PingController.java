package com.domus.challenge.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class PingController {
	
	@Operation(summary = "Shows that the application is up and running",
			   description = "Returns a pong message and the current datetime")		
	@GetMapping("/ping")
	public String ping() {
		var now = LocalDateTime.now();
		var customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var customFormattedDateTime = now.format(customFormatter);
		return "<h2>Pong... the app is running right now: ".concat(customFormattedDateTime).concat("</h2>");
	}
	
}
