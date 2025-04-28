package com.imsapp.ims;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class ImsApplicationTests {
	@MockitoBean
	private SpringApplication mockSpringApplication;

	@Test
	void contextLoads() {
		// This test ensures that the Spring application context loads successfully
	}

	@Test
	 void testMain() {
		// Call the main method
		ImsApplication.main(new String[]{});

		// Verify that the application context loads successfully
		assertThat(SpringApplication.run(ImsApplication.class, new String[]{})).isNotNull();
	}

}




