package com.domus.challenge.service;

import java.util.List;
import reactor.core.publisher.Mono;

public interface IDirectorService {

    Mono<List<String>> findDirectorsAboveThreshold(int threshold);
}
