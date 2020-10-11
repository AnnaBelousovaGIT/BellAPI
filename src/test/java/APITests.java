
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class APITests {

    private static final String AVATAR_ID_7 = "https://s3.amazonaws.com/uifaces/faces/twitter/follettkyle/128.jpg";
    private static final String AVATAR_ID_8 = "https://s3.amazonaws.com/uifaces/faces/twitter/araa3185/128.jpg";
    private static final String AVATAR_ID_9 = "https://s3.amazonaws.com/uifaces/faces/twitter/vivekprvr/128.jpg";
    private static final String AVATAR_ID_10 = "https://s3.amazonaws.com/uifaces/faces/twitter/russoedu/128.jpg";
    private static final String AVATAR_ID_11 = "https://s3.amazonaws.com/uifaces/faces/twitter/mrmoiree/128.jpg";
    private static final String AVATAR_ID_12 = "https://s3.amazonaws.com/uifaces/faces/twitter/hebertialmeida/128.jpg";


    @Test
    public void avatarOfUsers() {
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpecOK());
        List<String> avatarReferences = Arrays.asList(
                AVATAR_ID_7,
                AVATAR_ID_8,
                AVATAR_ID_9,
                AVATAR_ID_10,
                AVATAR_ID_11,
                AVATAR_ID_12);

        Response response = given()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().all()
                .extract().response();

        List<Integer> jsonResponse = response.jsonPath().getList("data.avatar");
        Assert.assertNotNull(jsonResponse);
        Assert.assertFalse(jsonResponse.isEmpty());
        Assert.assertEquals(jsonResponse,  avatarReferences, "Имена файлов-аватаров пользователей не совпадают");

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
        //сравниваем отсортированный jsonResponse, с тем, который получили
        Assert.assertEquals(jsonResponse.stream().sorted().collect(Collectors.toList()), jsonResponse);

    }

}
