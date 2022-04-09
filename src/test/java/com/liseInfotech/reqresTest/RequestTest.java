package com.liseInfotech.reqresTest;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
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

        ReqresData resData = response.as(ReqresData.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(resData.getPage(), is(2));
        assertThat(resData.getPer_page(), is(6));
        assertThat(resData.getTotal(), is(12));
        assertThat(resData.getTotal_pages(), is(2));

        assertThat(resData.getData().get(0).getId(), is(notNullValue()));
        assertThat(resData.getData().get(0).getEmail(), is("michael.lawson@reqres.in"));
        assertThat(resData.getData().get(0).getFirst_name(), is("Michael"));
        assertThat(resData.getData().get(0).getLast_name(), is("Lawson"));
        assertThat(resData.getData().get(0).getAvatar(), is("https://reqres.in/img/faces/7-image.jpg"));

        assertThat(resData.getSupport().url, is("https://reqres.in/#support-heading"));
        assertThat(resData.getSupport().text, is("To keep ReqRes free, contributions towards server costs are appreciated!"));
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

        SingleUserReqData userData = response.as(SingleUserReqData.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(userData.getData().getId(), is(notNullValue()));
        assertThat(userData.getData().getEmail(), is("janet.weaver@reqres.in"));
        assertThat(userData.getData().getFirst_name(), is("Janet"));
        assertThat(userData.getData().getLast_name(), is("Weaver"));
        assertThat(userData.getData().getAvatar(), is("https://reqres.in/img/faces/2-image.jpg"));

        assertThat(userData.getSupport().url, is("https://reqres.in/#support-heading"));
        assertThat(userData.getSupport().text, is("To keep ReqRes free, contributions towards server costs are appreciated!"));
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

        UnknownUserListData userData = response.as(UnknownUserListData.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(userData.getPage(), is(1));
        assertThat(userData.getPer_page(), is(6));
        assertThat(userData.getTotal(), is(12));

        assertThat(userData.getData().get(0).getId(), is(notNullValue()));
        assertThat(userData.getData().get(0).getName(), is("cerulean"));
        assertThat(userData.getData().get(0).getYear(), is(2000));
        assertThat(userData.getData().get(0).getColor(), is("#98B2D1"));
        assertThat(userData.getData().get(0).getPantone_value(), is("15-4020"));

        assertThat(userData.getSupport().url, is("https://reqres.in/#support-heading"));
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

        SingleUnknownUserData userData = response.as(SingleUnknownUserData.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(userData.getData().getId(), is(notNullValue()));
        assertThat(userData.getData().getName(), is("fuchsia rose"));
        assertThat(userData.getData().getYear(), is(2001));
        assertThat(userData.getData().getColor(), is("#C74375"));
        assertThat(userData.getData().getPantone_value(), is("17-2031"));

        assertThat(userData.getSupport().url, is("https://reqres.in/#support-heading"));
        assertThat(userData.getSupport().text, is("To keep ReqRes free, contributions towards server costs are appreciated!"));
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

        PostUserData userData = response.as(PostUserData.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_CREATED));

        assertThat(userData.getName(), containsString(empName));
        assertThat(userData.getJob(), containsString(jobTitle));
        assertThat(userData.getId(), notNullValue());
        assertThat(userData.getCreatedAt(), notNullValue());
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

        PutPatchUserData userData = response.as(PutPatchUserData.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(userData.getName(), is(empName));
        assertThat(userData.getJob(), is(updatedJobTitle));
        assertThat(userData.getUpdatedAt(), notNullValue());
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

        PutPatchUserData userData = response.as(PutPatchUserData.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(userData.getName(), is(empName));
        assertThat(userData.getJob(), is(updatedJobTitle));
        assertThat(userData.getUpdatedAt(), notNullValue());
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

        PostUserCredential userCredential = response.as(PostUserCredential.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(userCredential.getId(), is(greaterThan(0)));
        assertThat(userCredential.getToken(), notNullValue());
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

        ReqresData resData = response.as(ReqresData.class);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        assertThat(resData.getPage(), is(1));
        assertThat(resData.getPer_page(), is(6));
        assertThat(resData.getTotal(), is(12));
        assertThat(resData.getTotal_pages(), is(2));

        assertThat(resData.getData().get(0).getId(), is(notNullValue()));
        assertThat(resData.getData().get(0).getEmail(), is("george.bluth@reqres.in"));
        assertThat(resData.getData().get(0).getFirst_name(), is("George"));
        assertThat(resData.getData().get(0).getLast_name(), is("Bluth"));
        assertThat(resData.getData().get(0).getAvatar(), is("https://reqres.in/img/faces/1-image.jpg"));

        assertThat(resData.getSupport().url, is("https://reqres.in/#support-heading"));
        assertThat(resData.getSupport().text, is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }
}
