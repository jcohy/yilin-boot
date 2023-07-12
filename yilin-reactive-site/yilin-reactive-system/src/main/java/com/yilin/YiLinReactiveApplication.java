package com.yilin;

import java.time.Duration;

import reactor.core.publisher.Flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/11:15:02
 * @since 2023.0.1
 */
@SpringBootApplication
public class YiLinReactiveApplication {
	public static void main(String[] args) {
		SpringApplication.run(YiLinReactiveApplication.class, args);
	}

	@RestController
	public class HelloIndex {

		@GetMapping("/index")
		Flux<String> index() {
//			return Flux.interval("Hello World!");
			return Flux.interval(Duration.ofSeconds(1))
					.map(i -> "Hello World!");
		}
	}
}
