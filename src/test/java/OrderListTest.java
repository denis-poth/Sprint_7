import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Page.URL;
    }

    @Test
    @DisplayName("Возвращение списка заказов")
    public void testOrderList(){
        Response response = new Page().getOrderList();
        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(String.valueOf(responseBody));
        List<Map<String, Object>> orders = jsonPath.getList("orders");
        assertTrue(orders.size() > 0);
    }
}
