package pt.antonio;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

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
}
