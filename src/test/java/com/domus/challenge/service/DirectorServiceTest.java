package com.domus.challenge.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DirectorServiceTest {

    private MockWebServer mockWebServer;
    private IDirectorService directorService;

    @BeforeEach
    void setupBeforeEach() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        
        var client = WebClient.builder()
        		//.baseUrl(this.mockWebServer.url("/api/movies").toString())
        		.baseUrl(this.mockWebServer.url("/").toString())
        		.build(); //WebClient
        
        this.directorService = new DirectorServiceImpl(client);
    }
    
    @AfterEach
    void tearDownAfterEach() throws IOException {
    		this.mockWebServer.shutdown();
    }

    @Test
    void testFindDirectorsAboveThreshold() {

        //Page 1 with total_pages = 2
    		var page1 = """
        {
           "page":1,
           "per_page":2,
           "total":4,
           "total_pages":2,
           "data":[
              { "Title":"A", "Year":"2011", "Director":"John Doe", "Rated":"test_Rated", "Released":"test_Released", "Runtime":"test_Runtime", "Genre":"test_Genre", "Writer":"test_Writer", "Actors":"Actor A, Actor B, Actor C" },
              { "Title":"B", "Year":"2012", "Director":"Jane Doe", "Rated":"test_Rated_2", "Released":"test_Released_2", "Runtime":"test_Runtime_2", "Genre":"test_Genre_2", "Writer":"test_Writer_2", "Actors":"Actor X, Actor Y, Actor Z" }
           ]
        }
        """;
    		
        //Page number 2
    		var page2 = """
        {
           "page":2,
           "per_page":2,
           "total":4,
           "total_pages":2,
           "data":[
              { "Title":"C", "Year":"2013", "Director":"John Doe" },
              { "Title":"D", "Year":"2014", "Director":"John Doe" }
           ]
        }
        """;
    		
        // Mock responses
        this.mockWebServer.enqueue(new MockResponse()
        		.setBody(page1)
        		.setHeader("Content-Type", "application/json"));
        
        this.mockWebServer.enqueue(new MockResponse()
        		.setBody(page2)
        		.setHeader("Content-Type", "application/json"));
        
        var result = directorService.findDirectorsAboveThreshold(2); //Mono<List<String>>

        StepVerifier.create(result)
        		.expectNext(List.of("John Doe"))
        		.verifyComplete();
    }

    @Test
    void testNegativeThresholdReturnsEmptyList() {
        StepVerifier.create(directorService.findDirectorsAboveThreshold(-1))
        		.expectNext(List.of())
        		.verifyComplete();
    }
    
    @Test
    void testThresholdTwo() {

        var page1 = """
        {
           "page":1,
           "per_page":2,
           "total":4,
           "total_pages":2,
           "data":[
              { "Title":"A", "Year":"2011", "Director":"John Doe" },
              { "Title":"B", "Year":"2012", "Director":"Jane Doe" }
           ]
        }
        """;

        var page2 = """
        {
           "page":2,
           "per_page":2,
           "total":4,
           "total_pages":2,
           "data":[
              { "Title":"C", "Year":"2013", "Director":"John Doe" },
              { "Title":"D", "Year":"2014", "Director":"John Doe" }
           ]
        }
        """;

        mockWebServer.enqueue(new MockResponse().setBody(page1)
        		.addHeader("Content-Type", "application/json"));
        
        mockWebServer.enqueue(new MockResponse().setBody(page2)
        		.addHeader("Content-Type", "application/json"));

        StepVerifier.create(directorService.findDirectorsAboveThreshold(2))
                .expectNext(List.of("John Doe"))
                .verifyComplete();
    }
    
    @Test
    void testThresholdTooHighReturnsEmptyList() {
    	
    		var threshold = 22;

        var page1 = """
        {
           "page":1,
           "per_page":2,
           "total":4,
           "total_pages":2,
           "data":[
              { "Title":"A", "Year":"2011", "Director":"John Doe" },
              { "Title":"B", "Year":"2012", "Director":"Jane Doe" }
           ]
        }
        """;

        var page2 = """
        {
           "page":2,
           "per_page":2,
           "total":4,
           "total_pages":2,
           "data":[
              { "Title":"C", "Year":"2013", "Director":"John Doe" },
              { "Title":"D", "Year":"2014", "Director":"John Doe" }
           ]
        }
        """;

        mockWebServer.enqueue(new MockResponse().setBody(page1)
        		.addHeader("Content-Type", "application/json"));
        
        mockWebServer.enqueue(new MockResponse().setBody(page2)
        		.addHeader("Content-Type", "application/json"));

        StepVerifier.create(directorService.findDirectorsAboveThreshold(threshold))
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }
    
}
