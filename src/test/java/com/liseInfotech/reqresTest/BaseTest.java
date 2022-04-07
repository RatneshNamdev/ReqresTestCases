package com.liseInfotech.reqresTest;

import org.testng.annotations.BeforeSuite;
import static io.restassured.RestAssured.*;

public class BaseTest {
    public String baseUrl;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){
        baseURI = "https://reqres.in/api";
        baseUrl = baseURI;
    }

}
