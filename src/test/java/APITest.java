import java.io.IOException;
import java.util.HashMap;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;

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
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertNotNull(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void todoHeadRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/todos")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void todoPostRequest() throws Exception {
        String title = "Random Project";
        boolean doneStatus = false;
        String description =  "This project must be completed by monday";

        JSONObject obj = new JSONObject();

        obj.put("title", "Random Project");
        obj.put("doneStatus", doneStatus);
        obj.put("description", "This project must be completed by monday");

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/todos")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

            JSONParser parser = new JSONParser();
            JSONObject responseJson = (JSONObject) parser.parse(responseBody);

            String responseTitle = (String) responseJson.get("title");
            String responseDescription = (String) responseJson.get("description");

            assertEquals(title, responseTitle);
            assertEquals(description, responseDescription);
        }
}
