import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TestRequestResponse {
    private HashMap<String,String> hmap;
    static Logger logger;
    private String id;
    private String token;


    @BeforeClass
    public void setup() throws IOException, FileNotFoundException
    {
        String currentDir = System.getProperty("user.dir");
        System.setProperty("REL_PATH",currentDir);
        PropertyConfigurator.configure(currentDir+"\\src\\main\\java\\log4j.properties");
        logger = Logger.getLogger(" TEST REST API ");
        hmap= getTestData.readUrls("resturl");
    }


     @Test(priority = 1)
    public void getUserList(){
        logger.info("In method getUserList()");
        logger.info("Testcase to get the list of users");
         String url=hmap.get("GET");
         logger.info("URL is "+url);
         given().
         when().
                 get(url).
         then().
                 statusCode(200);
    }


    @Test(dataProvider="createUser",priority = 0)
    public void create_User(String username,String job)
    {
        logger.info("In create_User() method");
        logger.info("Testcase to create user");
        String url=hmap.get("CREATE");
        RestAssured.baseURI  =url;
        logger.info("URL is "+url);
        HashMap<String,String> hmap=new HashMap<String,String>();
        hmap.put("name",username);
        hmap.put("job",job);
        GsonBuilder gsonMapBuilder = new GsonBuilder();
        Gson gsonObject = gsonMapBuilder.create();
        String JSONObject = gsonObject.toJson(hmap);
        Response resp = given().
                            contentType("application/json").
                            body(JSONObject).
                        when().
                            post();

        Assert.assertEquals(201,resp.statusCode());
        Assert.assertEquals(username,resp.jsonPath().getString("name"));
        Assert.assertEquals(job,resp.jsonPath().getString("job"));
        id= resp.jsonPath().getString("id");

    }

    @Test(priority = 5)
    public void getUser(){
        logger.info("In getUser() method");
        logger.info("Testcase to get the user details using user_id");
        String url=hmap.get("GETSINGLE")+"2";
        logger.info("URL is "+url);
        given().
        when().
               get(url).
        then().
               statusCode(200);
    }

   @Test(priority = 7)
    public  void delete(){
        logger.info("In delete() method");
        logger.info("Testcase to delete user");
        String url=hmap.get("DELETE")+id;
       RestAssured.baseURI = url;
       logger.info("URL is "+url);
        given().
        when().
            delete().
        then().
                statusCode(204);
    }

   @Test(dataProvider = "loginData",priority = 3)
    public  void success_register(String username,String password){
        logger.info("In success_register() method");
        logger.info("Testcase to register user");
       String url=hmap.get("REGISTER");
        RestAssured.baseURI  =url;
        logger.info("URL is "+url);
       HashMap<String,String> hmap=new HashMap<String,String>();
       hmap.put("email",username);
       hmap.put("password",password);
       GsonBuilder gsonMapBuilder = new GsonBuilder();
       Gson gsonObject = gsonMapBuilder.create();
       String JSONObject = gsonObject.toJson(hmap);
        Response resp=given().
                         contentType("application/json").
                         body(JSONObject).
                      when().
                      post();

        Assert.assertEquals(200,resp.statusCode());
        Assert.assertEquals("4",resp.jsonPath().getString("id"));
        token= resp.jsonPath().getString("token");
    }

    @Test(priority = 2)
    public void unsuccess_register(){
        logger.info("In unsuccess_register() method");
        logger.info("Testcase to register user/invalid case");
        String url=hmap.get("REGISTER");
        RestAssured.baseURI=url;
        logger.info("URL is "+url);
        given().
               contentType("application/json").
               body("{\"email\":\"hhh@d\"}").
        when().
               post().
        then().
                statusCode(400).
                body("error",equalTo("Missing password"));

    }

   @Test(dataProvider = "loginData",priority = 6)
    public  void login(String username,String password){
        logger.info("In login() method");
        logger.info("Testcase to login");
        String url=hmap.get("LOGIN");
        RestAssured.baseURI  =url;
       logger.info("URL is "+url);
        HashMap<String,String> hmap=new HashMap<String,String>();
        hmap.put("email",username);
        hmap.put("password",password);
        GsonBuilder gsonMapBuilder = new GsonBuilder();
        Gson gsonObject = gsonMapBuilder.create();
        String JSONObject = gsonObject.toJson(hmap);

        given().
                contentType("application/json").
                body(JSONObject).
        when().
                post().
        then().
                statusCode(200).
                body("token",equalTo(token));
    }

    @Test(priority = 4)
    public  void loginFailure(){
        logger.info("In login() method");
        logger.info("Testcase to login/invalid");
        String url=hmap.get("LOGIN");
        RestAssured.baseURI=url;
        logger.info("URL is "+url);
        given().
                contentType("application/json").
                body("{\"email\":\"peter@klaven\"}").
        when().
                post().
        then().
                statusCode(400).
                body("error",equalTo("Missing password"));

    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() throws IOException,FileNotFoundException{
        Object[][] obj= getTestData.readData("RegisterAndLogin");
        return obj;
    }

    @DataProvider(name = "createUser")
    public Object[][] createUser() throws IOException,FileNotFoundException{
        Object[][] obj= getTestData.readData("create_User");
        return obj;
    }


}
