import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import static io.restassured.RestAssured.given;

public class Page {
    public static final String URL = "http://qa-scooter.praktikum-services.ru";
    public static final String CREATE_COURIER = "/api/v1/courier";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
    public static final String DELETE_COURIER = "/api/v1/courier/";
    public static final String ORDER = "/api/v1/orders";
    private RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .setRelaxedHTTPSValidation()
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new ErrorLoggingFilter())
                .build();
    }
    protected Response createCourier(Courier courier) {
        return given()
                .spec(baseRequestSpec())
                .body(courier)
                .post(CREATE_COURIER)
                .thenReturn();
    }
    protected void deleteCourier(String login, String password) {
        Response response = courierLogin(login, password);
        int id = response.jsonPath().getInt("id");
        given()
                .spec(baseRequestSpec())
                .when()
                .delete(DELETE_COURIER + id)
                .thenReturn();
    }

    protected Response courierLogin(String login, String password) {
        return given()
                .spec(baseRequestSpec())
                .body("{ \"login\": \"" + login + "\", \"password\": \"" + password + "\" }")
                .when()
                .post(COURIER_LOGIN)
                .thenReturn();
    }

    protected Response Order(List<String> color) {
        Orders order = orderColor(color);
        return given()
                .spec(baseRequestSpec())
                .body(order)
                .post(ORDER)
                .thenReturn();
    }

    protected Response getOrderList() {
        return given()
                .spec(baseRequestSpec())
                .get(ORDER)
                .thenReturn();
    }

    protected Orders orderColor(List<String> color) {
        String firstName = "Naruto";
        String lastName = "Uchiha";
        String address = "Konoha, 142 apt.";
        int metroStation = 4;
        String phone = "+7 800 355 35 35";
        int rentTime = 5;
        String deliveryDate = "2024-03-03";
        String comment = "Saske, come back to Konoha";
        return new Orders (firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}
