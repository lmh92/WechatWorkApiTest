package com.wechat.department;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_01_base {
    private static final Logger logger = LoggerFactory.getLogger(Demo_01_base.class);
    static String accessToken;
    static String departmentId;
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
        String body = "{\n" +
                "   \"name\": \"广州研发中心\",\n" +
                "   \"parentid\": 1,\n" +
                "}";
       Response response = given().contentType("application/json")
                .body(body)
                .queryParam("access_token",accessToken)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all()
                .extract().response();
       departmentId = response.path("id").toString();
        System.out.println(departmentId);
    }


    @DisplayName("修改部门")
    @Order(2)
    @Test
    void updateDepartment(){
        String body = "{\n" +
                "   \"id\": "+departmentId+",\n" +
                "   \"name\": \"广州研发中心修改\",\n" +
                "}";
        Response response = given().contentType(ContentType.JSON)
                .queryParam("access_token",accessToken)
                .body(body)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then().log().all()
                .extract().response();
       assertEquals("0",response.path("errcode").toString());
    }


    @DisplayName("查询部门")
    @Order(3)
    @Test
    void listDepartment(){
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
