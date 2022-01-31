import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.JSONResponse;

import static io.restassured.RestAssured.*;

public class Ouath2 {
	WebDriver driver;
	@BeforeTest
	public void setup() {
		WebDriverManager.chromedriver().setup();
	}

	@Test
	public void oAuthLogin() throws InterruptedException {
	
		driver = new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		driver.findElement(By.xpath("//*[@type='email']")).sendKeys("testuser1997insta@gmail.com");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Next']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@type='password']")).sendKeys("kiranji@123");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Next']")).click();
		Thread.sleep(3000);
		String curentURl=driver.getCurrentUrl();
		//4%2F0AX4XfWhYGsL_eXT5huRieGw_Ooi2sLIR7c1mK0GDbg0YOzZJ_VQPLX4mhURqUK9aVaheZw
		curentURl=curentURl.split("/?code=")[1];
		
		System.out.println("curentURl "+curentURl);
		String code=curentURl.split("&scope")[0];
		System.out.println("code "+code);
		
		
		//First Step
	//	given().log().all().queryParam("scope", "https://www.googleapis.com/auth/userinfo.email").queryParam("auth_url", "https://accounts.google.com/o/oauth2/v2/auth").queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").queryParam("response_type", "code").queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
	//	.when().get("https://accounts.google.com/o/oauth2/v2/auth");
		
		
	System.out.println("ccode printed"+ code);
		
		//second step
		String accessTokenRes=given().urlEncodingEnabled(false).log().all().queryParam("code",code ).queryParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com" )
		.queryParam("client_secret","erZOWM9g3UtwNRj340YYaK_W" ).queryParam("redirect_uri","https://rahulshettyacademy.com/getCourse.php" )
		.queryParam("grant_type","authorization_code" ).when().post("https://www.googleapis.com/oauth2/v4/token").then().log().all()
		.extract().asString();
		
		JsonPath js=new JsonPath(accessTokenRes);
		String accesstoken=js.getString("access_token");
		
		System.out.println("Access token "+accesstoken);
		
		
		//third step
		JSONResponse res=given().queryParam("access_token",accesstoken).log().all().expect().defaultParser(Parser.JSON).when().get("https://rahulshettyacademy.com/getCourse.php").then().log().all().statusCode(200).extract().as(JSONResponse.class);
	
		for(int i=0;i<res.getCourses().getApi().size();i++) {
			if(res.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java")) {
				System.out.println(res.getCourses().getApi().get(i).getCourseTitle()+" "+res.getCourses().getApi().get(i).getPrice() );
			}
		}
		driver.quit();
	}
}
