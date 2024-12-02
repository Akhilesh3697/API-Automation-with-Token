package com.api;

import com.utilities.GetPropertiesFile;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.utilities.FetchTokenFromUI.clkUI;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

public class LearnerApi {
    @Test
    public void learnerApi() throws InterruptedException {
        useRelaxedHTTPSValidation();
        GetPropertiesFile file = new GetPropertiesFile();
        RestAssured.baseURI = file.getProperty("Api.baseUrl");
        String token = clkUI();
        System.out.println("Learner API token="+token);
        Response response = RestAssured.given().header("X-XSRF-TOKEN",token)
                .when().get("learners-course-progress");
        System.out.println("Status code=" + response.getStatusCode());
        long time = response.getTime();
        double responseTime = time / 1000.0 / 60.0;
        System.out.println("Response time of API="+responseTime);

        JSONObject obj = new JSONObject(response.getBody().asString());
        int totalUserCount = obj.getInt("totalUserCount");
        System.out.println("Total User Count in the learner API="+totalUserCount);
        JSONArray array = obj.getJSONArray("learnerCourseProgressDTO");
        int numberOfUsers =0;
        int notAssigned=0;
        int completed=0;
        int behindSchedule=0;
        int inprogress=0;
        int notStarted=0;
        for (int i=0; i<array.length(); i++){
            JSONObject learner = array.getJSONObject(i);

            if(learner.has("courses") && learner.isNull("courses")){
                notAssigned++;
            }else {
                JSONArray courseArray = learner.getJSONArray("courses");
                boolean allCompleted = true;
                boolean allBehindSchedule = true;
                boolean allInProgress = true;
                boolean allnotStarted = true;
                boolean onlyBS = false;
                boolean onlyIP = false;
                boolean onlyNS = false;
                for (int j = 0; j < courseArray.length(); j++) {
                    JSONObject course = courseArray.getJSONObject(j);
                    String status = course.getString("status");
                    if (!status.equals("Completed")) {
                        allCompleted = false;
                    } if (!status.equals("In Progress")) {
                        allInProgress = false;
                    } if (!status.equals("Not Started")) {
                        allnotStarted = false;
                    } if (!status.equals("Behind Schedule")) {
                        allBehindSchedule = false;
                    } if((status.equals("Behind Schedule") && status.equals("Completed")) ||
                            (status.equals("Behind Schedule") && status.equals("In Progress")) ||
                            (status.equals("Behind Schedule") && status.equals("Not Started"))){
                        onlyBS = true;
                    } if (status.equals("In Progress") && status.equals("Completed")){
                        onlyIP = true;
                    } if (status.equals("Not Started") && status.equals("Completed")){
                        onlyNS = true;
                    }
                }
                if (allCompleted) {
                    completed++;
                } if (allBehindSchedule || onlyBS) {
                    behindSchedule++;
                } if (allInProgress || onlyIP) {
                    inprogress++;
                } if (allnotStarted || onlyNS) {
                    notStarted++;
                }
            }
            numberOfUsers++;
        }
        System.out.println("Number of User count in the learner list="+numberOfUsers);
        Assert.assertEquals(totalUserCount,numberOfUsers);
        System.out.println("Not Assigned User count="+notAssigned);
        System.out.println("Completed User count="+completed);
        System.out.println("Behind Schedule User count="+behindSchedule);
        System.out.println("In Progress User count="+inprogress);
        System.out.println("Not Started User count="+notStarted);
    }
}
