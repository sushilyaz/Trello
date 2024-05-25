package com.suhoi.demo;

import com.suhoi.demo.container.PostgresContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskManagerApplicationTests extends PostgresContainer {

	@Test
	void contextLoads() {
	}

}
