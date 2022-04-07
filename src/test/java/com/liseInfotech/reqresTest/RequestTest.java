package com.liseInfotech.reqresTest;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static io.restassured.RestAssured.*;

public class RequestTest {

    @Test
    public void getListOfUsers(){
        baseURI = "https://reqres.in/api/users?page=2";

        Response response = given()
                .contentType(ContentType.JSON)
                .get();

        System.out.println("response : " + response.prettyPrint());

        JsonPath jsonPath = new JsonPath(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(jsonPath.get("page"), is(1));
        assertThat(jsonPath.get("per_page"), is(6));
        assertThat(jsonPath.get("total"), is(12));
        assertThat(jsonPath.get("total_pages"), is(2));

        assertThat(jsonPath.get("data.id[0]"), is(1));
        assertThat(jsonPath.get("data.email[0]"), is("george.bluth@reqres.in"));
        assertThat(jsonPath.get("data.first_name[0]"), is("George"));
        assertThat(jsonPath.get("data.last_name[0]"), is("Bluth"));
        assertThat(jsonPath.get("data.avatar[0]"), is("https://reqres.in/img/faces/1-image.jpg"));

        assertThat(jsonPath.get("data.id[1]"), is(2));
        assertThat(jsonPath.get("data.email[1]"), is("janet.weaver@reqres.in"));
        assertThat(jsonPath.get("data.first_name[1]"), is("Janet"));
        assertThat(jsonPath.get("data.last_name[1]"), is("Weaver"));
        assertThat(jsonPath.get("data.avatar[1]"), is("https://reqres.in/img/faces/2-image.jpg"));

        assertThat(jsonPath.get("data.id[2]"), is(3));
        assertThat(jsonPath.get("data.email[2]"), is("emma.wong@reqres.in"));
        assertThat(jsonPath.get("data.first_name[2]"), is("Emma"));
        assertThat(jsonPath.get("data.last_name[2]"), is("Wong"));
        assertThat(jsonPath.get("data.avatar[2]"), is("https://reqres.in/img/faces/3-image.jpg"));

        assertThat(jsonPath.get("support.url"), is("https://reqres.in/#support-heading"));
        assertThat(jsonPath.get("support.text"), is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void getSingleUser(){
        int id=2;
        baseURI = "https://reqres.in/api";

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request(Method.GET, "/users/"+id)
                .then()
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());
        System.out.println(response.prettyPrint());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonPath.get("data.id"), is(id));
        assertThat(jsonPath.get("data.email"), is("janet.weaver@reqres.in"));
        assertThat(jsonPath.get("data.first_name"), is("Janet"));
        assertThat(jsonPath.get("data.last_name"), is("Weaver"));
        assertThat(jsonPath.get("data.avatar"), is("https://reqres.in/img/faces/2-image.jpg"));

        assertThat(jsonPath.get("support.url"), is("https://reqres.in/#support-heading"));
        assertThat(jsonPath.get("support.text"), is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void getInvalidUser(){
        int invalidId = 23;
        baseURI = "https://reqres.in/api";

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request(Method.GET, "/users/"+invalidId)
                        .then()
                                .extract()
                                        .response();

        System.out.println(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void getUnknownUserList(){
        String dummyText = "test";
        baseURI = "https://reqres.in/api";

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                        .request(Method.GET, "/"+dummyText)
                                .then()
                                        .extract()
                                                .response();

        System.out.println(response.asString());
        JsonPath jsonPath = new JsonPath(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonPath.get("page"), is(1));
        assertThat(jsonPath.get("data.id[0]"), is(1));
        assertThat(jsonPath.get("data.name[0]"), is("cerulean"));
        assertThat(jsonPath.get("data.year[0]"), is(2000));
        assertThat(jsonPath.get("data.color[0]"), is("#98B2D1"));
        assertThat(jsonPath.get("data.pantone_value[0]"), is("15-4020"));

        assertThat(jsonPath.get("data.id[1]"), is(2));
        assertThat(jsonPath.get("data.name[1]"), is("fuchsia rose"));

        assertThat(jsonPath.get("data.year[2]"), is(2002));
        assertThat(jsonPath.get("data.color[2]"), is("#BF1932"));

        assertThat(jsonPath.get("data.pantone_value[3]"), is("14-4811"));
    }

    @Test
    public void getSingleUnknownUser(){
        String dummyText = "test123";

        baseURI = "https://reqres.in/api";

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                        .request(Method.GET, "/"+dummyText+"/2")
                                .then()
                                        .extract()
                                                .response();

        System.out.println(response.asString());
        JsonPath jsonPath = new JsonPath(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(jsonPath.get("data.id"), is(2));
        assertThat(jsonPath.get("data.name"), is("fuchsia rose"));
        assertThat(jsonPath.get("data.year"), is(2001));
        assertThat(jsonPath.get("data.color"), is("#C74375"));
        assertThat(jsonPath.get("data.pantone_value"), is("17-2031"));

        assertThat(jsonPath.get("support.url"), is("https://reqres.in/#support-heading"));
        assertThat(jsonPath.get("support.text"), is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    public void getInvalidUnknownUser(){
        String invalidUserId = "/test/23";
        System.out.println(invalidUserId);

        baseURI = "https://reqres.in/api";

        Response response = given()
                .contentType(ContentType.JSON)
                .get(invalidUserId);

        assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void postUser(){
        String empName = "morpheus";
        String jobTitle = "leader";
        baseURI = "https://reqres.in/api";

        String body = "{" + "\"name\": \""+empName+"\"," + "\"job\": \""+jobTitle+"\"\n" + "}";

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.POST, "/users")
                .then()
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());

        System.out.println(response.asString());
        assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));

        assertThat(jsonPath.get("name"), containsString(empName));
        assertThat(jsonPath.get("job"), containsString(jobTitle));
        assertThat(jsonPath.get("id"), notNullValue());
        assertThat(jsonPath.get("createdAt"), notNullValue());
    }

    @Test
    public void updateUserDetailsByPut(){
        int userId = 2;
        String empName = "morpheus";
        String updatedJobTitle = "zion resident";
        String body = "{"+" \"name\": \""+empName+"\"," + "\"job\": \""+updatedJobTitle+"\"" + "}";

        baseURI = "https://reqres.in/api";

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.PUT, "/users/"+userId)
                .then()
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());
        System.out.println(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonPath.get("name"), is(empName));
        assertThat(jsonPath.get("job"), is(updatedJobTitle));
        assertThat(jsonPath.get("updatedAt"), notNullValue());
    }

    @Test
    public void updateUserDetailsByPatch(){
        int userId = 2;
        String empName = "morpheus";
        String updatedJobTitle = "zion resident";
        String body = "{"+" \"name\": \""+empName+"\"," + "\"job\": \""+updatedJobTitle+"\"" + "}";

        baseURI = "https://reqres.in/api";

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.PATCH, "/users/"+userId)
                .then()
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());
        System.out.println(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonPath.get("name"), is(empName));
        assertThat(jsonPath.get("job"), is(updatedJobTitle));
        assertThat(jsonPath.get("updatedAt"), notNullValue());
    }

    @Test
    public void deleteDBObject(){
        int userId = 2;
        baseURI = "https://reqres.in/api";

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request(Method.DELETE, "/users/"+userId)
                .then()
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    public void postUserCredential(){
        String userRegistered = "register";
        String userEmail = "eve.holt@reqres.in";
        String userPassword = "pistol";
        baseURI = "https://reqres.in/api";
        String body = "{" + "\"email\": \""+userEmail+"\"," + " \"password\": \""+userPassword+"\"" + "}";

        System.out.println("body : " + body);
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.POST, "/register")
                .then()
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());
        System.out.println("status : " + response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonPath.get("id"), is(greaterThan(0)));
        assertThat(jsonPath.get("token"), notNullValue());
    }

    @Test
    public void invalidUserCredentialBypost(){
        String userRegistered = "register";
        String userEmail = "sydney@fife";
        baseURI = "https://reqres.in/api";
        String body = "{" + "\"email\": \""+userEmail+"\" }";

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.POST, "/"+userRegistered)
                .then()
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());
        System.out.println("status : " + response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_BAD_REQUEST));
        assertThat(jsonPath.get("error"), is("Missing password"));
    }

    @Test
    public void userLoginByPost(){
        baseURI = "https://reqres.in/api";
        String userLogin = "login";
        String userEmail = "eve.holt@reqres.in";
        String userPassword = "cityslicka";
        String body = "{" + "\"email\": \""+userEmail+"\"," + " \"password\": \""+userPassword+"\"" + "}";

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.POST, "/"+userLogin)
                .then()
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.asString());
        System.out.println(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonPath.get("token"), notNullValue());
    }

    @Test
    public void invalidUserLoginByPost(){
        baseURI = "https://reqres.in/api";
        String userLogin = "login";
        String userEmail = "peter@klaven";
        String body = "{" + "\"email\": \""+userEmail+"\","+ "}";

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.POST, "/"+userLogin)
                .then()
                .extract()
                .response();

        assertThat(response.statusCode(), is(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void getDelayResponse(){
        baseURI = "https://reqres.in/api/users?delay=3";

        Response response = given()
                .contentType(ContentType.JSON)
                .get();

        System.out.println("response : " + response.asString());

        JsonPath jsonPath = new JsonPath(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(jsonPath.get("page"), is(1));
        assertThat(jsonPath.get("per_page"), is(6));
        assertThat(jsonPath.get("total"), is(12));
        assertThat(jsonPath.get("total_pages"), is(2));

        assertThat(jsonPath.get("data.id[0]"), is(1));
        assertThat(jsonPath.get("data.email[0]"), is("george.bluth@reqres.in"));
        assertThat(jsonPath.get("data.first_name[0]"), is("George"));
        assertThat(jsonPath.get("data.last_name[0]"), is("Bluth"));
        assertThat(jsonPath.get("data.avatar[0]"), is("https://reqres.in/img/faces/1-image.jpg"));

        assertThat(jsonPath.get("data.id[1]"), is(2));
        assertThat(jsonPath.get("data.email[1]"), is("janet.weaver@reqres.in"));
        assertThat(jsonPath.get("data.first_name[1]"), is("Janet"));
        assertThat(jsonPath.get("data.last_name[1]"), is("Weaver"));
        assertThat(jsonPath.get("data.avatar[1]"), is("https://reqres.in/img/faces/2-image.jpg"));

        assertThat(jsonPath.get("data.id[2]"), is(3));
        assertThat(jsonPath.get("data.email[2]"), is("emma.wong@reqres.in"));
        assertThat(jsonPath.get("data.first_name[2]"), is("Emma"));
        assertThat(jsonPath.get("data.last_name[2]"), is("Wong"));
        assertThat(jsonPath.get("data.avatar[2]"), is("https://reqres.in/img/faces/3-image.jpg"));

        assertThat(jsonPath.get("support.url"), is("https://reqres.in/#support-heading"));
        assertThat(jsonPath.get("support.text"), is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }
}
