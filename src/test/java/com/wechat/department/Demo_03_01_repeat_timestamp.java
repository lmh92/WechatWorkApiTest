package com.wechat.department;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1、基础脚本，分别执行了，创建、修改、查询、删除接口并进行了校验
 * 2、进行了优化，方法间进行解耦，每个方法可以独立行
 * 3、使用时间戳命名法避免入参重复造成的报错。
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_03_01_repeat_timestamp {
    static String accessToken;
//    static String departmentId;
    @BeforeAll
    public static void getAccessToken(){
        accessToken = given().log().all()
                .param("corpid","ww257df1f7abf7c10c")
                .param("corpsecret","fiLyFCotGF_xpOSxCFJ9Cy8mup5KOscBKmDD4Obez1k")
                .when()
                .get(" https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then()
                .log().all()
                .extract().response().path(  "access_token");
    }


    @DisplayName("创建部门")
    @Order(1)
    @Test
    void createDepartment(){
        String name = "广州研发中心"+System.currentTimeMillis();
        String createBody = "{\n" +
                "   \"name\": \""+name+"\",\n" +
                "   \"parentid\": 1,\n" +
                "}";
        Response response = given().contentType("application/json")
                .body(createBody)
                .queryParam("access_token",accessToken)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all()
                .extract().response();
        String departmentId = response.path("id")!=null ? response.path("id").toString():null;
        System.out.println(departmentId);
    }


    @DisplayName("修改部门")
    @Order(2)
    @Test
    void updateDepartment(){
        String name = "广州研发中心"+System.currentTimeMillis();
        String createBody = "{\n" +
                "   \"name\": \""+name+"\",\n" +
                "   \"parentid\": 1,\n" +
                "}";
        Response createResponse = given().contentType("application/json")
                .body(createBody)
                .queryParam("access_token",accessToken)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all()
                .extract().response();
        String departmentId = createResponse.path("id")!=null ? createResponse.path("id").toString():null;

        String upDatebody = "{\n" +
                "   \"id\": "+departmentId+",\n" +
                "   \"name\": \"广州研发中心修改"+System.currentTimeMillis()+"\",\n" +
                "}";
        Response updateResponse = given().contentType(ContentType.JSON)
                .queryParam("access_token",accessToken)
                .body(upDatebody)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then().log().all()
                .extract().response();
        assertEquals("0",updateResponse.path("errcode").toString());
    }


    @DisplayName("查询部门")
    @Order(3)
    @Test
    void listDepartment(){
        String name = "广州研发中心"+System.currentTimeMillis();
        String createBody = "{\n" +
                "   \"name\": \""+name+"\",\n" +
                "   \"parentid\": 1,\n" +
                "}";
        Response createResponse = given().contentType("application/json")
                .body(createBody)
                .queryParam("access_token",accessToken)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all()
                .extract().response();
        String departmentId = createResponse.path("id")!=null ? createResponse.path("id").toString():null;

        Response response = given().log().all()
                .param("access_token",accessToken)
                .param("id",departmentId)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
                .then().log().all()
                .extract().response();
        assertEquals("0",response.path("errcode").toString());
        assertEquals(departmentId,response.path("department.id[0]").toString());
    }


    @DisplayName("删除部门")
    @Order(4)
    @Test
    void deleteDepartment(){
        String name = "广州研发中心"+System.currentTimeMillis();
        String createBody = "{\n" +
                "   \"name\": \""+name+"\",\n" +
                "   \"parentid\": 1,\n" +
                "}";
        Response createResponse = given().contentType("application/json")
                .body(createBody)
                .queryParam("access_token",accessToken)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all()
                .extract().response();
        String departmentId = createResponse.path("id")!=null ? createResponse.path("id").toString():null;

        Response response =  given().log().all()
                .param("access_token",accessToken)
                .param("id",departmentId)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then().log().all()
                .extract().response();
        assertEquals("0",response.path("errcode").toString());
    }
}
