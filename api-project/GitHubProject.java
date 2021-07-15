package projects;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GitHubProject {
	//declare request Specification
	RequestSpecification requestSpec;
	//Global properties
	String sshkey;
	int sshKeyId;
	
  @BeforeClass
  public void setUp() {
	  //Create request specification
	  requestSpec = new RequestSpecBuilder()
              // Set content type
              .setContentType(ContentType.JSON)
              .addHeader("Authorization","token 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")
              // Set base URL
              .setBaseUri("https://api.github.com")
              // Build request specification
              .build();
	  sshkey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC1Ay/X3mvVNnLHoAvH+t6h2G7CET3Ndfyil1k8zc2Znn9N/hfK5YnQVeAAItNr9BqfnkPi1PsVdFgeRZXEJ5ZrB1UG1IHJvWbrgzKo2V8QNjmVvFZ5Pwd5t2buklxw5M0sY0lllFUvE47c27UhAdGPmmX45/a6Gn8PaeUUeM/USTh6Twir0SPu8bIVkCDd6LEMO1Us+/YbxHKFgwWzSnuBQzIS+m06U/+kPTG6W7v8H1GhY4zEEq9Xn9Jrb8/9uS6V1KlRUvH0Z9iSkvQ6TSKJiq0HF+TwvTLAlJsvoCSx+JWj1T7yA7lWAqAChBRbhj2iYbOHkS/gtLjBXucrE2rj";
  }
	
  @Test
  public void addNewKey() {
      // Create JSON request
      String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQClWvRXIFsIPxY9stFAhe97lTKdAG/FX+bDB6mLcyXbMOeNuKC4Zbm1pH48UAqdxsT2SVTHomXEXocPtQjIHmWO8QxT5mLo8Yir98HfxCsme1HBVLZVDHnYY8JemRA9ZkPIiHMXkZlMn7Vyv4nhItzHvV2y3EsGfdkXRT3IqwU4XUwTySyBF/s20/F7aPrdwnxkuMxIhjZpardubf6C4nxK1zpOcRj7jmWdlejStYcrZP3CX6iPMOPEFn/4UZotCjNF/hKg8gR89fVRM6GXCGyM9116mNbes1YmxVLrTxt0RZc+imx8kzeTKKI2FsMTqPBdIwXm+7Mgo3nz82p8U\"}";

      Response response = 
          given().spec(requestSpec) // Set headers
          .body(reqBody) // Add request body
          .when().post("/user/keys"); // Send POST request
      
      sshKeyId = response.then().extract().path("id");
      
      // Assertions
      response.then().statusCode(201);
      response.then().body("id", equalTo(sshKeyId));
}
  @Test(priority=2)
  public void getKeyInfo() {
      Response response = 
          given().spec(requestSpec) // Set headers
          .when().get("/user/keys"); // Send GET request

      Reporter.log(response.body().asPrettyString());
      // Assertion
      response.then().statusCode(200);
      response.then().body("[0].id", equalTo(sshKeyId));
  }

  @Test(priority=3)
  public void deleteKey() {
      Response response = 
          given().spec(requestSpec) // Set headers
          .when().pathParam("id", sshKeyId) // Set path parameter
          .delete("/user/keys/{id}"); // Send DELETE request
      
      Reporter.log(response.body().asPrettyString());
      // Assertion
      response.then().statusCode(204);
  }
  
}
