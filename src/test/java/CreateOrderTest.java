import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final List<String> color;
    private int track;
    private int statusCode;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[] getColor() {
        return new Object[][] {
                {List.of()},
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Page.URL;
    }

    @Test
    @DisplayName("Создание заказа с цветом самоката")
    public void testCreateOrder() {
        Response response = new Page().Order(color);
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        track = response.jsonPath().getInt("track");
        assertTrue(track != 0);
    }
}
