import okhttp3.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;

public class APITest_categories {
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
    public void todoGetAllCategoryRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/categories")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertNotNull(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void todoCategoryHeadRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/categories")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void categoryPostRequest() throws Exception {
        String title = "Random Project";
        String description =  "This project must be completed by monday";

        JSONObject obj = new JSONObject();

        obj.put("title", "Random Project");
        obj.put("description", "This project must be completed by monday");

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/categories")
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

    @Test
    public void categoryGetIDRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/2")
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(200, response.code());

        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        JSONObject jsonObject = new JSONObject(responseJson);
        JSONArray todos = (JSONArray) jsonObject.get("categories");

        for (Object todoObject : todos) {
            JSONObject todo = (JSONObject) todoObject;
            String title = (String) todo.get("title");
            String description = (String) todo.get("description");
            assertEquals("Home", title);
            assertEquals("", description);
        }
    }

    @Test
    public void categoryPostIDRequest() throws Exception {
        String title = "Random Changed Project";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1")
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
    public void categoryPutIDRequest() throws Exception {
        String title = "Random Changed Project Again";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1")
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
    public void deleteCategoryRequest() throws Exception {
        String title = "Random Project to Delete";
        //boolean doneStatus = false;
        String description = "This project must be deleted";

        JSONObject obj = new JSONObject();
        obj.put("title", title);
        //obj.put("doneStatus", doneStatus);
        obj.put("description", description);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/todos/1/categories")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(201, response.code());
        String responseBody = response.body().string();
        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);
        String todoId = (String) responseJson.get("id");

        Request deleteRequest = new Request.Builder()
                .url("http://localhost:4567/todos/1/categories/" + todoId)
                .delete()
                .build();
        Response deleteResponse = client.newCall(deleteRequest).execute();
        assertEquals(200, deleteResponse.code());
    }

    @Test
    public void categoryGetAllTodos() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1/todos")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertNotNull(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void categoryPostIDTodos() throws Exception {
        String title = "Office Work";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1/todos")
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
    public void todoDeleteOfRequest() throws Exception {
        String title = "Office Work";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1/todos")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        assertEquals(201, response.code());
        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);
        String todoId = (String) responseJson.get("id");

        Request deleteRequest = new Request.Builder()
                .url("http://localhost:4567/categories/1/todos/" + todoId)
                .delete()
                .build();
        Response deleteResponse = client.newCall(deleteRequest).execute();
        assertEquals(200, deleteResponse.code());
    }

    @Test
    public void categoryGetAllProjects() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1/projects")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertNotNull(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void todoCategoryProjectsHeadRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1/projects")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void categoryPostProjectIDTodos() throws Exception {
        String title = "Office Work Project";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1/projects")
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
    public void categoriesDeleteOfRequest() throws Exception {
        String title = "Office Work Project";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/categories/1/projects")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        assertEquals(201, response.code());
        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);
        String todoId = (String) responseJson.get("id");

        Request deleteRequest = new Request.Builder()
                .url("http://localhost:4567/categories/1/projects/" + todoId)
                .delete()
                .build();
        Response deleteResponse = client.newCall(deleteRequest).execute();
        assertEquals(200, deleteResponse.code());
    }
}
