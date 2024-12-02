package com.api;

import com.utilities.GetPropertiesFile;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static com.utilities.FetchTokenFromUI.clkUI;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;


public class QuickFilterApi {
    @Test
    public void quickFilterApi() throws InterruptedException {
        GetPropertiesFile file = new GetPropertiesFile();
        baseURI = file.getProperty("Api.baseUrl");
        String token = clkUI();
        System.out.println("Quick filter token="+token);
        Response response = given().header("X-XSRF-TOKEN",token)
                .when().get("course-quick-filters");
        System.out.println("Quick filter API Status code=" + response.getStatusCode());
        long time = response.getTime();
        double responseTime = time / 1000.0 / 60.0;
        System.out.println("Quick filter API Response time of API="+responseTime);
        System.out.println(" Response of API=" + response.asString());
        JSONObject obj = new JSONObject(response.getBody().asString());
        int id = obj.getInt("id");
        System.out.println("Id of the API="+id);
        int notAssigned = obj.getInt("notAssigned");
        int notStarted = obj.getInt("notStarted");
        int inProgress = obj.getInt("inProgress");
        int behindSchedule = obj.getInt("behindSchedule");
        int completed = obj.getInt("completed");
        int total = notAssigned+notStarted+inProgress+behindSchedule+completed;
        System.out.println("Number of User count in Quick filter API="+total);
    }
}
