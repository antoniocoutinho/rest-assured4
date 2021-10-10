package pt.antonio;

import javax.management.RuntimeErrorException;
import static org.junit.Assert.*;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HelloWorldTest {

	@Test
	public void shouldReturnStatusCode200() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		assertEquals(response.getStatusCode(), 200);

		// Simulating Error, error test during execution:
		// throw new RuntimeException();

		// Simulating Failure, result not expected:
		// ValidatableResponse validation = response.then();
		// validation.statusCode(201);

		// Simulating Pass:
		ValidatableResponse validation = response.then();
		validation.statusCode(200);
	}

}
