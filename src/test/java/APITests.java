
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class APITests {


    @Test
    public void avatarOfUsers() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpecOK());
        Map<String, String> avatar = new HashMap<String, String>();
        avatar.put("id7", "https://s3.amazonaws.com/uifaces/faces/twitter/follettkyle/128.jpg");
        avatar.put("id8", "https://s3.amazonaws.com/uifaces/faces/twitter/araa3185/128.jpg");
        avatar.put("id9", "https://s3.amazonaws.com/uifaces/faces/twitter/vivekprvr/128.jpg");
        avatar.put("id10", "https://s3.amazonaws.com/uifaces/faces/twitter/russoedu/128.jpg");
        avatar.put("id11", "https://s3.amazonaws.com/uifaces/faces/twitter/mrmoiree/128.jpg");
        avatar.put("id12", "https://s3.amazonaws.com/uifaces/faces/twitter/hebertialmeida/128.jpg");

        Response response = given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("avatar"), avatar.get("avatar"), "Имена файлов-аватаров пользователей не совпадают");
    }

    @Test
    public void registerSuccessful() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpecOK());
        Map<String, String> register = new HashMap<String, String>();
        register.put("email", "eve.holt@reqres.in");
        register.put("password", "pistol");

        given()
                .body(register)
                .when()
                .post("/api/register")
                .then()
                .log().all()
                .assertThat()
                .body("id", equalTo(4))
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void registerUnsuccessful() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpecERROR());
        Map<String, String> register = new HashMap<String, String>();
        register.put("email", "sydney@fife");

        given()
                .body(register)
                .when()
                .post("/api/register")
                .then()
                .log().all()
                .assertThat()
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void sortByYear() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpecOK());
        Response response = given()
                .queryParam("sort", "year")
                .when()
                .get("/api/unknown")
                .then()
                .log().all()
                .extract().response();
        List<Integer> jsonResponse = response.jsonPath().getList("data.year");
        System.out.println(jsonResponse);
        //сравниваем отсортированный jsonResponse, с тем, который получили
        Assert.assertEquals(jsonResponse.stream().sorted().collect(Collectors.toList()), jsonResponse);

    }

}
