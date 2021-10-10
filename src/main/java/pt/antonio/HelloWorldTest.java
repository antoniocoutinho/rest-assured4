package pt.antonio;

import javax.management.RuntimeErrorException;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
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
	
	@Test
	public void shouldUseHancrestMachers()
	{
		// String
		assertThat("test", is("test"));
		// int
		assertThat(100, is(100));
		//Integer class
		assertThat(100, isA(Integer.class));
		// Double class
		assertThat(100d, isA(Double.class));
		
		// greater and less than
		assertThat(100d, greaterThan(99d));
		assertThat(100d, lessThan(101d));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9));
		assertThat(impares, containsInAnyOrder(9,1,3,5,7));
		assertThat(impares, hasItem(1));
		assertThat(impares, hasItems(1,3));
		
		
		assertThat("Test", is(not("test")));
		assertThat("Test", not("test"));
		assertThat("Test", anyOf(is("Test"), is("Not test")));
		assertThat("Test", allOf(startsWith("T"), endsWith("t"), containsString("es")));
		
	}
}
