package JIRAAPI;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.io.File;

import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import libraryAPI.ReusableMethods;

public class E2E {

	@Test
	public void JiraAutomate() {
		
		//Login
		RestAssured.baseURI="http://localhost:8080";
		SessionFilter sessionFilter=new SessionFilter();
		
		given().log().all().filter(sessionFilter).header("Content-Type","application/json")
		.body("{ \"username\": \"hhh\", \"password\": \"kiranji@123\" }").when().post("/rest/auth/1/session").then()
		.assertThat().statusCode(200).log().all();
		
		//Add comment
		String txt="Hello world123";
		String commentResponse=given().log().all().header("Content-Type","application/json").pathParam("KEY", "10202").filter(sessionFilter).body("{\r\n"
				+ "    \"body\": \""+txt+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").post("/rest/api/2/issue/{KEY}/comment")
		.then().assertThat().statusCode(201).log().all().extract().response().asString();
		
		JsonPath js=ReusableMethods.rawToJson(commentResponse);
	String commentId=	js.get("id").toString();
		
		
		
		///add attachment
		given().log().all().header("X-Atlassian-Token","no-check").multiPart("file",new File("C:\\Users\\kkrid\\eclipse-workspace\\RestAPI\\json.txt"))
		.filter(sessionFilter).pathParam("KEY", "10202").header("Content-Type", "multipart/form-data").when().post("rest/api/2/issue/{KEY}/attachments")
		.then().assertThat().statusCode(200).log().all();
		
		
		//Get issue details
		
		String response=given().log().all().filter(sessionFilter).pathParam("KEY", "10202").queryParam("fields", "comment").when().get("/rest/api/2/issue/{KEY}").then().assertThat()
		.statusCode(200).log().all().extract().response().asString();
		
		
		//Assert the comment by retrieveing from comment id
		js=ReusableMethods.rawToJson(response);
		int commentsCount=js.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++) {
			if(js.getString("fields.comment.comments["+i+"].id").equalsIgnoreCase(commentId)) {
				Assert.assertEquals(txt,js.get("fields.comment.comments["+i+"].body").toString()) ;
				
				break;
			}
		}
		
	
		
		
		
	}

}
