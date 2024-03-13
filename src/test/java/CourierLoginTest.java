import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CourierLoginTest {
    private Courier courier;
    private int statusCode;

    @Before
    public void setUp() {
        RestAssured.baseURI = Page.URL;
        courier = new Courier("ssssss", "1111", "black");
        Response response = new Page().createCourier(courier);
        statusCode = response.getStatusCode();
    }

    @Test
    @DisplayName("Логин курьера с валидными данными")
    public void testLoginValidCourier() {
        Response responseSecond = new Page().courierLogin(courier.getLogin(), courier.getPassword());
        assertEquals(200, responseSecond.getStatusCode());
        assertNotNull(responseSecond.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Логин курьера с пустым полем login")
    public void testCourierLoginWithoutLogin() {
        Response responseSecond = new Page().courierLogin("", courier.getPassword());
        assertEquals(400, responseSecond.getStatusCode());
        JsonPath jsonPath = responseSecond.getBody().jsonPath();
        String actualMessage = jsonPath.getString("message");
        String expectedMessage = "Недостаточно данных для входа";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Логин курьера с пустым полем password")
    public void testLoginCourierWithoutPassword() {
        Response responseSecond = new Page().courierLogin(courier.getLogin(), "");
        assertEquals(400, responseSecond.getStatusCode());
        JsonPath jsonPath = responseSecond.getBody().jsonPath();
        String actualMessage = jsonPath.getString("message");
        String expectedMessage = "Недостаточно данных для входа";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Логин курьера с невалидными данными в поле login")
    public void testLoginCourierWithInvalidLogin() {
        Response responseSecond = new Page().courierLogin("sssssssssssssssssssss", courier.getPassword());
        assertEquals(404, responseSecond.getStatusCode());
        JsonPath jsonPath = responseSecond.getBody().jsonPath();
        String actualMessage = jsonPath.getString("message");
        String expectedMessage = "Учетная запись не найдена";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Логин курьера с невалидными данными в поле password")
    public void testLoginCourierWithInvalidPassword() {
        Response responseSecond = new Page().courierLogin(courier.getLogin(), "0000");
        assertEquals(404, responseSecond.getStatusCode());
        JsonPath jsonPath = responseSecond.getBody().jsonPath();
        String actualMessage = jsonPath.getString("message");
        String expectedMessage = "Учетная запись не найдена";
        assertEquals(expectedMessage, actualMessage);
    }

    @After
    public void tearDown() {
        if (statusCode == 201) {
            Page scooterAPI = new Page();
            scooterAPI.deleteCourier(courier.getLogin(), courier.getPassword());
        }
    }
}