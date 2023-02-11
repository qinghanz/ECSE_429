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
    public void getAllProjects() throws Exception {
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
    public void projectsGetIDRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1")
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(200, response.code());

        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        JSONObject jsonObject = new JSONObject(responseJson);
        JSONArray projects = (JSONArray) jsonObject.get("projects");

        for (Object projectObject : projects) {
            JSONObject project = (JSONObject) projectObject;
            String title = (String) project.get("title");
            String description = (String) project.get("description");

            // should i be using the Booleans?
            assertEquals("Testing Changed Project #2", title);
            assertEquals("", description);
        }
    }

    @Test
    public void projectHead_IDRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        assertEquals(200, response.code());
    }

    @Test
    public void projectPostIDRequest() throws Exception {
        String title = "Testing Changed Project";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1")
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
    public void projectPutIDRequest() throws Exception {
        String title = "Testing Changed Project #2";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1")
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        String responseTitle = (String) responseJson.get("title");
        assertEquals(title, responseTitle);
    }


    // Creating project to then delete
    @Test
    public void projectsPostRequest_toDelete() throws Exception {
        String title = "Project to delete";
        boolean completed = true;
        boolean active = true;
        String description = "this will be deleted";

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

    // deleting the previously created project
    @Test
    public void todoDeleteRequest() throws Exception {

        Request deleteRequest = new Request.Builder()
                .url("http://localhost:4567/projects/8")
                .delete()
                .build();
        Response deleteResponse = client.newCall(deleteRequest).execute();
        assertEquals(200, deleteResponse.code());
    }

    @Test
    public void projectsGetID_TaskRequest() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1/tasks")
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(200, response.code());

        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        JSONObject jsonObject = new JSONObject(responseJson);
        JSONArray projects = (JSONArray) jsonObject.get("projects");

        for (Object projectObject : projects) {
            JSONObject project = (JSONObject) projectObject;
            String title = (String) project.get("title");
            String description = (String) project.get("description");

            assertEquals("Testing Changed Project #2", title);
            assertEquals("", description);
        }
    }

    @Test
    public void projectHeadRequest_ID() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1/tasks")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        assertEquals(200, response.code());
    }




    // ------------------------------------------------------------------------------------------------------------------------------------------------

}
