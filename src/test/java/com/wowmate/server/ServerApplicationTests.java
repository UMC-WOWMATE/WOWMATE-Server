package com.wowmate.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class ServerApplicationTests {

	@Test
	void contextLoads() {

		LocalDate today = LocalDate.now();
		int year = today.getYear();

		System.out.println(year);
	}

}
