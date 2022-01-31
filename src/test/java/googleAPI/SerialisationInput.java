package googleAPI;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import pojo.*;

public class SerialisationInput {

	// SerialisationInput with google add place API

	@Test
	public void addplace() {
		AddPlace addplace=new AddPlace();
		Location location=new Location();
		location.setLat(-38.383494);
		location.setLng(33.427362);
		List<String> li=new ArrayList<String>();
		li.add("shoe park");
		li.add("shop");
		
		addplace.setLocation(location);
		addplace.setAccuracy(50);
		addplace.setName("Frontline house");
		addplace.setPhone_number("(+91) 983 893 3937\r\n");
		addplace.setAddress("29, side layout, cohen 09");
		addplace.setWebsite("http://google.com");
		addplace.setLanguage("French-IN");
		addplace.setTypes(li);
		
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		given().log().all().body(addplace).queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.post("/maps/api/place/add/json").then().assertThat().statusCode(200).log().all();
	}
}
