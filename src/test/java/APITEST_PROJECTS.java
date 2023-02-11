import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;

public class APITEST_PROJECTS {
    OkHttpClient client = new OkHttpClient();

    @Test
    public void todoGetAllProjects() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertNotNull(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void projectHeadRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void projectsPostRequest() throws Exception {
        String title = "new Project";
        boolean completed = true;
        boolean active = true;
        String description = "test";

        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("completed", completed);
        obj.put("active", active);
        obj.put("description", description);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/projects")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        System.out.println(responseBody);

        String responseTitle = (String) responseJson.get("title");
        String responseDescription = (String) responseJson.get("description");

        assertEquals(title, responseTitle);
        assertEquals(description, responseDescription);
    }

    @Test
    public void todoGetIDRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/todos/2")
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(200, response.code());

        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        JSONObject jsonObject = new JSONObject(responseJson);
        JSONArray todos = (JSONArray) jsonObject.get("todos");

        for (Object todoObject : todos) {
            JSONObject todo = (JSONObject) todoObject;
            String title = (String) todo.get("title");
            String description = (String) todo.get("description");
            assertEquals("file paperwork", title);
            assertEquals("", description);
        }
    }

    @Test
    public void todoPostIDRequest() throws Exception {
        String title = "Random Changed Project";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/todos/1")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        String responseTitle = (String) responseJson.get("title");
        assertEquals(title, responseTitle);
    }

    @Test
    public void todoPutIDRequest() throws Exception {
        String title = "Random Changed Project Again";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/todos/1")
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        String responseTitle = (String) responseJson.get("title");
        assertEquals(title, responseTitle);
    }

    @Test
    public void todoDeleteRequest() throws Exception {
        String title = "Random Project to Delete";
        boolean doneStatus = false;
        String description = "This project must be deleted";

        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("doneStatus", doneStatus);
        obj.put("description", description);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/todos")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(201, response.code());
        String responseBody = response.body().string();
        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);
        String todoId = (String) responseJson.get("id");

        Request deleteRequest = new Request.Builder()
                .url("http://localhost:4567/todos/" + todoId)
                .delete()
                .build();
        Response deleteResponse = client.newCall(deleteRequest).execute();
        assertEquals(200, deleteResponse.code());
    }
}
