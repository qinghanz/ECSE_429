import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class APITest {
    OkHttpClient client = new OkHttpClient();

    @Test
    public void testGeneralSetup() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 200);
        }
    }

    @Test
    public void todoGetAllRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/todos")
                .build();

        Response response = client.newCall(request).execute();

        assertEquals(200, response.code());
    }

}
