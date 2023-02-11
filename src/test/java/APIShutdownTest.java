import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;

public class APIShutdownTest {
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
    public void testDocumentation() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/docs")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 200);
        }
    }
}
