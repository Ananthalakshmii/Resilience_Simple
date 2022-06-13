package com.accolite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController

public class ResilienceController {
	private static final Logger logger=LoggerFactory.getLogger(ResilienceController.class);
	
	@GetMapping("/")
	
	//@Retry(name="sampleapi")//it will continuously try for 3 times(0.5 sec between each try), if it doesnt work then it throws error//customizing nees to modify in properties---- 1.retry
	
	//@Retry(name="sampleapi", fallbackMethod = "fallBackResponse")//----2.fall back
	
	@CircuitBreaker(name = "sampleapi", fallbackMethod = "fallBackResponse")//-------3. circuit breaker--will not retry-will not hit MS again and again-
	
	@RateLimiter(name = "sampleapi")//----4.rate limiter(use 3 and 4)-no link with 1234 can use together as well
	public String simpleAPI() {
		logger.info("--------------simple api called");
		ResponseEntity<String> forEntity   = new RestTemplate().getForEntity("http:localhost:8080/dummy",String.class); // to bring api DOWN
		return forEntity.getBody();
	}
	
	public String fallBackResponse(Exception e) {//similar to exception handling
		logger.info("----from fallback");
		return "fallback respone";
	}

}
