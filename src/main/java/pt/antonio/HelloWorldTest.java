package pt.antonio;

import javax.management.RuntimeErrorException;

import static io.restassured.RestAssured.*;
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

	@Test
	public void shouldUseFluentMode() {
		
		Response response = request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		ValidatableResponse validation = response.then();
		validation.statusCode(200);
		
		// Short mode:
		get("http://restapi.wcaquino.me:80/ola").then().statusCode(200);
		
		//Fluent mode:
		given() //Pre-conditions
		.when() //Actions
			.get("http://restapi.wcaquino.me:80/ola")
		.then()// Validations
			//.assertThat()
			//Or
			.statusCode(200);
	}

}
