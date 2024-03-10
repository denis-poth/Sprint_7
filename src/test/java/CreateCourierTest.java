import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateCourierTest {
    private Courier courier;
    private int statusCode;

    @Before
    public void setUp() {
        RestAssured.baseURI = Page.URL;
    }

    @Test
    @DisplayName("Создание курьера со всеми полями")
    public void testCreateCourier() {
        courier = new Courier("ssssss", "1234", "Jackie");
        Response response = new Page().createCourier(courier);
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String expectedBody = "{\"ok\":true}";
        assertEquals(expectedBody, response.getBody().asString());
    }

    @Test
    @DisplayName("Создание курьера с занятым логином")
    public void testCreateDoubleCourier() {
        courier = new Courier("ssssss", "1234", "Jackie");
        Response response = new Page().createCourier(courier);
        Response responseSecond = new Page().createCourier(courier);
        statusCode = response.getStatusCode();
        assertEquals(409, responseSecond.getStatusCode());
        JsonPath jsonPath = responseSecond.getBody().jsonPath();
        String actualMessage = jsonPath.getString("message");
        String expectedMessage = "Этот логин уже используется";
        assertTrue("Ожидаемое сообщение об ошибке не найдено", actualMessage.contains("Этот логин уже используется"));
    }


    @Test
    @DisplayName("Создание курьера без логина")
    public void testCreateCourierWithoutLogin() {
        courier = new Courier("", "1234", "Jackie");
        Response response = new Page().createCourier(courier);
        statusCode = response.getStatusCode();
        assertEquals(400, statusCode);
        JsonPath jsonPath = response.getBody().jsonPath();
        String actualMessage = jsonPath.getString("message");
        String expectedMessage = "Недостаточно данных для создания учетной записи";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        courier = new Courier("ssssss", "", "Jackie");
        Response response = new Page().createCourier(courier);
        statusCode = response.getStatusCode();
        assertEquals(400, statusCode);
        JsonPath jsonPath = response.getBody().jsonPath();
        String actualMessage = jsonPath.getString("message");
        String expectedMessage = "Недостаточно данных для создания учетной записи";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Создание курьера без имени")
    public void testCreateCourierWithoutFirstName() {
        courier = new Courier("ssssss", "1234", "");
        Response response = new Page().createCourier(courier);
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String expectedBody = "{\"ok\":true}";
        assertEquals(expectedBody, response.getBody().asString());
    }

    @After
    public void tearDown() {
        if (statusCode == 201) {
            Page scooterAPI = new Page();
            scooterAPI.deleteCourier(courier.getLogin(), courier.getPassword());
        }
    }
}


