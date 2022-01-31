package libraryAPI;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import googleAPI.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class CRUDlibrary {
	String response="";
	String id;
	
//	@BeforeMethod
//	public void baseURI() {
//		RestAssured.baseURI="http://216.10.245.166";
//	}
	@Test(dataProvider = "BookData")
	public void addLibrary(String s1,String s2) {
		System.out.println("Add library");
		RestAssured.baseURI="http://216.10.245.166";
	 response=	given().body(Payload.addBook(s1,s2 )).header("Content-Type","application/json")
		.when().post("Library/Addbook.php").then().log().all() .assertThat().statusCode(200).extract().response().asString();
	
	
	//System.out.println(response);
	
	JsonPath js=ReusableMethods.rawToJson(response);
	id=js.getString("ID");
	Assert.assertEquals(js.getString("ID"),s1+s2 );
	}
	
	
	@AfterMethod
	public void deleteLibrary() {
		System.out.println("Delete API printing");
		RestAssured.baseURI="http://216.10.245.166";
		given().log().all().body("{\r\n"
				+ " \r\n"
				+ "\"ID\" : \""+id+"\"\r\n"
				+ "} ").header("Content-Type","application/json")
		.when().post("Library/DeleteBook.php").then().assertThat().statusCode(200).log().all();
	}
	
	
	
	@DataProvider(name = "BookData")
	public Object[][] getData() {
		return new Object[][] {
			{"bh11","2211"},{"hb3","3123"}
		};
	}
	
	

}
