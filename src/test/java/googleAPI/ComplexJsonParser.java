package googleAPI;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParser {

	public static void complexJson() {
		JsonPath jsonPath = new JsonPath(Payload.complexJsonString());

		// Print No of courses returned by API

		System.out.println(jsonPath.getInt("courses.size()"));

		// Print Purchase Amount

		System.out.println(jsonPath.getInt("dashboard.purchaseAmount"));

		// Print Title of the first course
		System.out.println(jsonPath.getString("courses[1].title"));

		// Print All course titles and their respective Prices
		for (int i = 0; i < jsonPath.getInt("courses.size()"); i++) {
			System.out.println("Course title " + jsonPath.getString("courses[" + i + "].title"));
			System.out.println("Course price " + jsonPath.getInt("courses[" + i + "].price"));
		}
		
		//Print no of copies sold by RPA Course
		for(int i=0;i< jsonPath.getInt("courses.size()");i++) {
			String key="RPA";
			String title=jsonPath.getString("courses["+i+"].title");
			if(title.equalsIgnoreCase(key)) {
				System.out.println("Print no of copies sold by "+key+" "+jsonPath.getInt("courses["+i+"].copies"));
				break;
			}
		}
		
		//Verify if Sum of all Course prices matches with Purchase Amount
		int sum=0;
		for(int i=0;i< jsonPath.getInt("courses.size()");i++) {
			
			int price=jsonPath.getInt("courses["+i+"].price");
			int copies=jsonPath.getInt("courses["+i+"].copies");
			sum=sum+(price*copies);
			
		}
		//Use TESTNG
		System.out.println("Verify if Sum of all Course prices matches with Purchase Amount "+sum);

	}

	public static void main(String args[]) {
		complexJson();
	}

}
