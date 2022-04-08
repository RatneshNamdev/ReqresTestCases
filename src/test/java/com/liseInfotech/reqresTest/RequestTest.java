package com.liseInfotech.reqresTest;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static io.restassured.RestAssured.*;

public class RequestTest extends BaseTest{

    @Test(priority = 1)
    public void getListOfUsers(){
        Response response = given()
                .contentType(ContentType.JSON)
                        .when()
                                .param("page", 2)
                                        .request(Method.GET, "/users")
                                                .then()
                                                        .extract()
                                                                .response();
        System.out.println("This is first priority...get(Userlist)");
        JSONObject jsonObject = new JSONObject(response.asString());
        JSONArray data = jsonObject.getJSONArray("data");
        System.out.println("DataArray :" + data);

        JSONObject indexedArrayObject = data.getJSONObject(0);
        JSONObject indexedArrayObject1 = data.getJSONObject(1);
        System.out.println("ArrayInsideObject : " + indexedArrayObject);

        JSONObject subJsonObject = jsonObject.getJSONObject("support");

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(jsonObject.getInt("page"), is(2));
        assertThat(jsonObject.getInt("per_page"), is(6));
        assertThat(jsonObject.getInt("total"), is(12));
        assertThat(jsonObject.getInt("total_pages"), is(2));

        assertThat(indexedArrayObject.getInt("id"), is(notNullValue()));
        assertThat(indexedArrayObject.getString("email"), is("michael.lawson@reqres.in"));
        assertThat(indexedArrayObject.getString("first_name"), is("Michael"));
        assertThat(indexedArrayObject.getString("last_name"), is("Lawson"));
        assertThat(indexedArrayObject.getString("avatar"), is("https://reqres.in/img/faces/7-image.jpg"));

        assertThat(indexedArrayObject1.getString("email"), is("lindsay.ferguson@reqres.in"));
        assertThat(indexedArrayObject1.getString("first_name"), is("Lindsay"));
        assertThat(indexedArrayObject1.getString("last_name"), is("Ferguson"));

        assertThat(subJsonObject.getString("url"), is("https://reqres.in/#support-heading"));
        assertThat(subJsonObject.getString("text"), is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test(priority = 2)
    public void getSingleUser(){
        int id=2;

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request(Method.GET, "/users/"+id)
                .then()
                .extract()
                .response();

        System.out.println("This is second priority...get(singleUser)");
        JSONObject jsonObject = new JSONObject(response.asString());
        System.out.println("jsonObj : " + jsonObject);
        JSONObject subObject = jsonObject.getJSONObject("data");
        System.out.println("subObj : " + subObject);
        JSONObject subObj = jsonObject.getJSONObject("support");
        System.out.println("subObj : " + subObj);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(subObject.getInt("id"), is(notNullValue()));
        assertThat(subObject.getString("email"), is("janet.weaver@reqres.in"));
        assertThat(subObject.getString("first_name"), is("Janet"));
        assertThat(subObject.getString("last_name"), is("Weaver"));
        assertThat(subObject.getString("avatar"), is("https://reqres.in/img/faces/2-image.jpg"));

        assertThat(subObj.getString("url"), is("https://reqres.in/#support-heading"));
        assertThat(subObj.getString("text"), is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test(priority = 6)
    public void getInvalidUser(){
        int invalidId = 23;

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request(Method.GET, "/users/"+invalidId)
                        .then()
                                .extract()
                                        .response();
        System.out.println("This is sixth priority...get_invalidUser");
        System.out.println(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));
    }

    @Test(priority = 3)
    public void getUnknownUserList(){
        String dummyText = "test";

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                        .request(Method.GET, "/"+dummyText)
                                .then()
                                        .extract()
                                                .response();

        System.out.println("This is third priority...get_UnknownList");
        JSONObject jsonObject = new JSONObject(response.asString());
        JSONArray data = jsonObject.getJSONArray("data");
        JSONObject objectOfArray = data.getJSONObject(0);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(jsonObject.getInt("page"), is(1));
        assertThat(jsonObject.getInt("per_page"), is(6));
        assertThat(jsonObject.getInt("total"), is(12));

        assertThat(objectOfArray.getInt("id"), is(notNullValue()));
        assertThat(objectOfArray.getString("name"), is("cerulean"));
        assertThat(objectOfArray.getInt("year"), is(2000));
        assertThat(objectOfArray.getString("color"), is("#98B2D1"));
        assertThat(objectOfArray.getString("pantone_value"), is("15-4020"));
    }

    @Test(priority = 4)
    public void getSingleUnknownUser(){
        String dummyText = "test123";

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                        .request(Method.GET, "/"+dummyText+"/2")
                                .then()
                                        .extract()
                                                .response();

        System.out.println("This is fourth priority...get_SingleUnknown");
       JSONObject jsonObject = new JSONObject(response.asString());
       JSONObject subJsonObject = jsonObject.getJSONObject("data");
       JSONObject subJsonObjects = jsonObject.getJSONObject("support");

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(subJsonObject.getInt("id"), is(notNullValue()));
        assertThat(subJsonObject.getString("name"), is("fuchsia rose"));
        assertThat(subJsonObject.getInt("year"), is(2001));
        assertThat(subJsonObject.getString("color"), is("#C74375"));
        assertThat(subJsonObject.getString("pantone_value"), is("17-2031"));

        assertThat(subJsonObjects.getString("url"), is("https://reqres.in/#support-heading"));
        assertThat(subJsonObjects.getString("text"), is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test(priority = 7)
    public void getInvalidUnknownUser(){
        String invalidUserId = "/test/23";

        System.out.println("This is seventh priority...get_InvalidUnknown");
        Response response = given()
                .contentType(ContentType.JSON)
                .get(invalidUserId);

        assertThat(response.statusCode(), is(HttpStatus.SC_NOT_FOUND));
    }

    @Test(priority = 8)
    public void postUser(){
        String empName = "morpheus";
        String jobTitle = "leader";

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

        System.out.println("This is eighth priority...postUser");
        JSONObject jsonObject = new JSONObject(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));

        assertThat(jsonObject.getString("name"), containsString(empName));
        assertThat(jsonObject.getString("job"), containsString(jobTitle));
        assertThat(jsonObject.getInt("id"), notNullValue());
        assertThat(jsonObject.getString("createdAt"), notNullValue());
    }

    @Test(priority = 13)
    public void updateUserDetailsByPut(){
        int userId = 2;
        String empName = "morpheus";
        String updatedJobTitle = "zion resident";
        String body = "{"+" \"name\": \""+empName+"\"," + "\"job\": \""+updatedJobTitle+"\"" + "}";

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.PUT, "/users/"+userId)
                .then()
                .extract()
                .response();

        System.out.println("This is 13 in priority...putUpdateUser");
        JSONObject jsonObject = new JSONObject(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonObject.getString("name"), is(empName));
        assertThat(jsonObject.getString("job"), is(updatedJobTitle));
        assertThat(jsonObject.getString("updatedAt"), notNullValue());
    }

    @Test(priority = 14)
    public void updateUserDetailsByPatch(){
        int userId = 2;
        String empName = "morpheus";
        String updatedJobTitle = "zion resident";
        String body = "{"+" \"name\": \""+empName+"\"," + "\"job\": \""+updatedJobTitle+"\"" + "}";

        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.PATCH, "/users/"+userId)
                .then()
                .extract()
                .response();

        System.out.println("This is 14th priority...patchUpdateUserDetails");
        JSONObject jsonObject =new JSONObject(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonObject.getString("name"), is(empName));
        assertThat(jsonObject.getString("job"), is(updatedJobTitle));
        assertThat(jsonObject.getString("updatedAt"), notNullValue());
    }

    @Test(priority = 15)
    public void deleteDBObject(){
        int userId = 2;
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .request(Method.DELETE, "/users/"+userId)
                .then()
                .extract()
                .response();

        System.out.println("This is 15th priority...delete");
        assertThat(response.statusCode(), is(HttpStatus.SC_NO_CONTENT));
    }

    @Test(priority = 9)
    public void postUserCredential(){
        String userRegistered = "register";
        String userEmail = "eve.holt@reqres.in";
        String userPassword = "pistol";
        String body = "{" + "\"email\": \""+userEmail+"\"," + " \"password\": \""+userPassword+"\"" + "}";

        System.out.println("This is nine th priority...postUserCre");
        System.out.println("body : " + body);
        Response response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .request(Method.POST, "/"+userRegistered)
                .then()
                .extract()
                .response();

       JSONObject jsonObject = new JSONObject(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonObject.getInt("id"), is(greaterThan(0)));
        assertThat(jsonObject.getString("token"), notNullValue());
    }

    @Test(priority = 11)
    public void invalidUserCredentialByPost(){
        String userRegistered = "register";
        String userEmail = "sydney@fife";
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

        System.out.println("This is 11th priority...postInvalidCre");
        JSONObject jsonObject = new JSONObject(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_BAD_REQUEST));
        assertThat(jsonObject.getString("error"), is("Missing password"));
    }

    @Test(priority = 10)
    public void userLoginByPost(){
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

        System.out.println("This is tenth priority...postLogin");
        JSONObject jsonObject = new JSONObject(response.asString());
        System.out.println(response.asString());

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat(jsonObject.getString("token"), notNullValue());
    }

    @Test(priority = 12)
    public void invalidUserLoginByPost(){
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

        System.out.println("This is 12th priority...postInvalidUserLogin");
        assertThat(response.statusCode(), is(HttpStatus.SC_BAD_REQUEST));
    }

    @Test(priority = 5)
    public void getDelayResponse(){
        Response response = given()
                .contentType(ContentType.JSON)
                        .when()
                                .param("delay", 3)
                                        .request(Method.GET, "/users")
                                                .then()
                                                        .extract()
                                                                .response();

        System.out.println("This is fifth priority...getDelayResponse");
        JSONObject jsonObject = new JSONObject(response.asString());
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        JSONObject jsonArrayOfObject = jsonArray.getJSONObject(0);
        JSONObject jsonArrayOfObject2 = jsonArray.getJSONObject(1);
        JSONObject subJsonObject = jsonObject.getJSONObject("support");

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(jsonObject.getInt("page"), is(1));
        assertThat(jsonObject.getInt("per_page"), is(6));
        assertThat(jsonObject.getInt("total"), is(12));
        assertThat(jsonObject.getInt("total_pages"), is(2));

        assertThat(jsonArrayOfObject.getInt("id"), is(notNullValue()));
        assertThat(jsonArrayOfObject.getString("email"), is("george.bluth@reqres.in"));
        assertThat(jsonArrayOfObject.getString("first_name"), is("George"));
        assertThat(jsonArrayOfObject.getString("last_name"), is("Bluth"));
        assertThat(jsonArrayOfObject.getString("avatar"), is("https://reqres.in/img/faces/1-image.jpg"));

        assertThat(jsonArrayOfObject2.getInt("id"), is(notNullValue()));
        assertThat(jsonArrayOfObject2.getString("email"), is("janet.weaver@reqres.in"));
        assertThat(jsonArrayOfObject2.getString("first_name"), is("Janet"));
        assertThat(jsonArrayOfObject2.getString("last_name"), is("Weaver"));
        assertThat(jsonArrayOfObject2.getString("avatar"), is("https://reqres.in/img/faces/2-image.jpg"));

        assertThat(subJsonObject.getString("url"), is("https://reqres.in/#support-heading"));
        assertThat(subJsonObject.getString("text"), is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }
}
