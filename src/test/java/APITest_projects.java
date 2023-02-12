import okhttp3.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;

public class APITest_projects {
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

    // return all the instances of project
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

    // headers for all the instances of project
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

    // we should be able to create project without a ID using the fi eld values in the body of the message
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


    // return a specifi c instances of project using a id
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
            assertEquals("Testing Changed Project", title);
            assertEquals("", description);
        }
    }

    // headers for a specifi c instances of project using a id
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

    // amend a specifi c instances of project using a id with a body containing the fi elds to amend
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


    // amend a specifi c instances of project using a id with a body containing the fi elds to amend
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


    // amend a specifi c instances of project using a id with a body containing the fi elds to amend
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

    // delete a specific instances of project using a id
    @Test
    public void todoDeleteRequest() throws Exception {
        Request deleteRequest = new Request.Builder()
                .url("http://localhost:4567/projects/10")
                .delete()
                .build();
        Response deleteResponse = client.newCall(deleteRequest).execute();
        assertEquals(200, deleteResponse.code());
    }

    // return all the todo items related to project, with given id, by the relationship named tasks
    @Test
    public void getTasksProjectsbyID() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1/tasks")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertNotNull(response.body().string());
        assertEquals(200, response.code());
    }

    // headers for the todo items related to project, with given id, by the relationship named tasks
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

    //create an instance of a relationship named tasks between project instance :id and the todo instance represented by theid in the body of the message
    @Test
    public void PostProjectsID() throws Exception {
        String title = "Office Work Project";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1/tasks")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        String responseTitle = (String) responseJson.get("title");
        assertEquals(title, responseTitle);
    }

    // delete the instance of the relationship named tasks between project and todo using the :id




    //return all the category items related to project, with given id, by the relationship named categories
    @Test
    public void projecsGetAllProjects() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/3/categories")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertNotNull(response.body().string());
        assertEquals(200, response.code());
    }

    // headers for the category items related to project, with given id, by the relationship named categories
    @Test
    public void projectHead_IDRequest2() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/3/categories")
                .head()
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        assertEquals(200, response.code());
    }

    //create an instance of a relationship named categories between project instance :id and the category instancerepresented by the id in the body of the message
    @Test
    public void PostProjectsID2() throws Exception {
        String title = "the 429 TA is awesome";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/projects/1/categories")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        String responseTitle = (String) responseJson.get("title");
        assertEquals(title, responseTitle);
    }
}
