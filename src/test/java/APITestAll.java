import okhttp3.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.Random;
//import com.fasterxml.jackson.databind.ObjectMapper;
import javax.xml.parsers.ParserConfigurationException;

@TestMethodOrder(Random.class)
public class APITestAll {
    OkHttpClient client = new OkHttpClient();

    // TODO TESTS

    @Test
    public void testGeneralSetup() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(response.code(), 200);
        }
    }

    ////////////////////////////////////////////////////////////////////// // Section 1: TODOS /////////////////////////////////////////////////////////////////////////////


    // return all the instances of todo
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

    // headers for all the instances of todo
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

    // we should be able to create todo without a ID using the field values in the body of the message
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

    // return a specific instances of todo using a id
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

    // amend a specific instances of todo using a id with a body containing the fields to amend
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

    // amend a specific instances of todo using a id with a body containing the fields to amend
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

    // delete a specific instances of todo using a id
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

    // delete a specific instances of todo using a id
    @Test
    public void todoDeleteCategoryRequest() throws Exception {
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

    // return all the category items related to todo, with given id, by the relationship named categories
    @Test
    public void todoGetAllTasksOf() throws Exception {
        Request request = new Request.Builder()
                .url("http://localhost:4567/todos/1/tasksof")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertNotNull(response.body().string());
        assertEquals(200, response.code());
    }


    // create an instance of a relationship named categories between todo instance :id and the category instance represented by the id in the body of the message
    @Test
    public void todoPostIDTaskOfRequest() throws Exception {
        String title = "Office Work";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/todos/1/tasksof")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);

        String responseTitle = (String) responseJson.get("title");
        assertEquals(title, responseTitle);
    }

    // delete the instance of the relationship named categories between todo and category using the :id
    @Test
    public void todoDeleteOfRequest() throws Exception {
        String title = "Office Work";

        JSONObject obj = new JSONObject();

        obj.put("title", title);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());

        Request request = new Request.Builder()
                .url("http://localhost:4567/todos/1/tasksof")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        assertEquals(201, response.code());
        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);
        String todoId = (String) responseJson.get("id");

        Request deleteRequest = new Request.Builder()
                .url("http://localhost:4567/todos/1/tasksof/" + todoId)
                .delete()
                .build();
        Response deleteResponse = client.newCall(deleteRequest).execute();
        assertEquals(200, deleteResponse.code());
    }


    ////////////////////////////////////////////////////////////////////// // Section 2: PROJECTS /////////////////////////////////////////////////////////////////////////////

    // PROJECT TESTS

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
                .url("http://localhost:4567/projects/2")
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
            assertEquals("Office Work Project", title);
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

    // amend a specific instances of project using a id with a body containing the fi elds to amend
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


    // amend a specific instances of project using a id with a body containing the fi elds to amend
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


    // amend a specific instances of project using a id with a body containing the fi elds to amend
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
    public void projectDeleteRequest() throws Exception {
        String title = "Random Project to Delete";
        boolean completed = false;
        boolean active = false;
        String description = "This project must be deleted";

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
        assertEquals(201, response.code());
        String responseBody = response.body().string();
        JSONParser parser = new JSONParser();
        JSONObject responseJson = (JSONObject) parser.parse(responseBody);
        String todoId = (String) responseJson.get("id");

        Request deleteRequest = new Request.Builder()
                .url("http://localhost:4567/projects/" + todoId)
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

    ////////////////////////////////////////////////////////////////////// // Section 3: CATEGORIES /////////////////////////////////////////////////////////////////////////////

    // return all the instances of category
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

    // headers for all the instances of category
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

    // we should be able to create category without a ID using the field values in the body of the message
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

    // return a specific instances of category using a id
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


    // amend a specific instances of category using a id with a body containing the fields to amend
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

    // amend a specific instances of category using a id with a body containing the fields to amend
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

    // delete a specific instances of category using a id
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


    // return all the project items related to category, with given id, by the relationship named projects
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

    // create an instance of a relationship named projects between category instance :id and the project instance represented by the id in the body of the message
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

    // delete the instance of the relationship named projects between category and project using the :id
    @Test
    public void categoryDeleteOfRequest() throws Exception {
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

    // return all the todo items related to category, with given id, by the relationship named todos
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

    // headers for the todo items related to category, with given id, by the relationship named todos
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

    // create an instance of a relationship named todos between category instance :id and the todo instance represented by the id in the body of the message
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

    // delete the instance of the relationship named todos between category and todo using the :id
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

