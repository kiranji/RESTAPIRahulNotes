package googleAPI;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import static io.restassured.RestAssured.*;

public class base {

	public static void main(String arg[]) throws IOException {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\kkrid\\eclipse-workspace\\RestAPI\\nio.json")))).when().post("/maps/api/place/add/json").then().assertThat()
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)").extract().response()
				.asString();
		System.out.println(response);
		JsonPath jsonpath = new JsonPath(response);
		String placeid = jsonpath.getString("place_id");
		System.out.println(placeid);

		// Update
		String newAddress = "Madurai";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + placeid + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}\r\n" + " \r\n" + "")
				.when().put("maps/api/place/update/json").then().log().all().assertThat()
				.body("msg", equalTo("Address successfully updated"));

		/// Read API
		response = given().log().all().queryParam("place_id", placeid).queryParam("key", "qaclick123").when()
				.get("maps/api/place/get/json").then().assertThat().statusCode(200).extract().response().asString();

		jsonpath = new JsonPath(response);
		String actual = jsonpath.getString("address");
		System.out.println(actual);
		Assert.assertEquals(newAddress, actual);
	

	}
}
