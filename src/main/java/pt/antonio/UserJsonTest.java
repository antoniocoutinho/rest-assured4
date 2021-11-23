package pt.antonio;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

public class UserJsonTest {
	@Test
	public void shouldValidateFirstLevel()
	{
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
		;
	}
	@Test
	public void shouldValidateSecondLevel()
	{
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("id", is(2))
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"))
		;
	}
	
	@Test
	public void shouldValidateJsonList()
	{
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("id", is(3))
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Luizinho", "Zezinho"))
		;
	}
	
	@Test
	public void shouldValidateRootJsonList()
	{
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))//Root
			.body("", hasSize(3))//Root
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia")) // get all names
			.body("age[1]", is(25))
			.body("filhos.name", hasItems(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null)); // contains should have all itens at the same order.
		
		;
	}
	
	@Test
	public void shouldValidateFirstLevelPathExtraction()
	{
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		//Path
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s", "id"));// using parameter
		
		//JsonPath
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));
		
		//from method
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
	@Test
	public void shouldValidateErrorMessage()
	{
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"))
		;
	}
	
	@Test
	public void shouldAdvancedValidations()
	{
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))//Root
			.body("age.findAll{it <= 25}.size()", is(2))// findAll return all occurrences
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1)) 
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina")) // return list should use hasItem
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina")) //return object
			.body("findAll{it.age <= 25}[-1].name", is("Ana Júlia")) // Last item
			.body("find{it.age <= 25}.name", is("Maria Joaquina")) // find the fist occurrence
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia")) // find All names containing 'n'
			.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina")) // find All names with length > 10
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA")) // multple queries
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1))) //allOf()
			.body("id.max()", is(3))
			.body("salary.min()",is(1234.5678f))
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)) )
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
		;
	}
	@Test
	public void shouldUseJsonPathAndJava() {
		ArrayList<String> names =
			given()
			.when()
				.get("http://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)
				.extract().path("name.findAll{it.startsWith('Maria')}")
			;
		Assert.assertEquals(1, names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("MariA JoaQuina"));
		
	}
}
