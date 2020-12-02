package org.febsteam.demos.jta;

import org.febsteam.demos.jta.service.ITest1Service;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JtaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class JtaApplicationTests {
	@Autowired
	private ITest1Service test1Service;

	@Test
	void saveTest1Test2() {
		test1Service.saveTest1Test2();
	}
	@Test
	void saveTest1Test2WithException() throws Exception{
		test1Service.saveTest1Test2WithException();
	}
}
