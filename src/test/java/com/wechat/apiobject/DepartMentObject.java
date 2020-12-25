package com.wechat.apiobject;


import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartMentObject {
    public static Response creatDepartMent(String name,String enName, String accessToken){
        String createBody = "{\n" +
                "   \"name\": \""+name+"\",\n" +
                "   \"name_en\": \""+enName+"\",\n" +
                "   \"parentid\": 1,\n" +
                "}";
        Response response = given().contentType("application/json")
                .body(createBody)
                .queryParam("access_token",accessToken)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().log().all()
                .extract().response();
        return response;
    }

    public static String creatDepartMent(String accessToken){
        String name = "广州研发中心"+System.currentTimeMillis();
        String creatEnName="en_name"+ System.currentTimeMillis();
        Response response = creatDepartMent(name,creatEnName,accessToken);
        String departmentId = response.path("id")!=null ? response.path("id").toString():null;
        return departmentId;
    }

    public static Response listDepartMent(String departmentId,String accessToken){
        Response response = given().log().all()
                .param("access_token",accessToken)
                .param("id",departmentId)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
                .then().log().all()
                .extract().response();
        return response;

    }

    public static Response deletDepartMent(String departmentId,String accessToken){
        Response response =  given().log().all()
                .param("access_token",accessToken)
                .param("id",departmentId)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then().log().all()
                .extract().response();
        assertEquals("0",response.path("errcode").toString());
        return response;
    }

    public static Response updateDepartMent(String departmentId,String updateName,String accessToken){
        String upDatebody = "{\n" +
                "   \"id\": "+departmentId+",\n" +
                "   \"name\": \""+updateName+"\",\n" +
                "}";
        Response updateResponse = given().contentType(ContentType.JSON)
                .queryParam("access_token",accessToken)
                .body(upDatebody)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then().log().all()
                .extract().response();
        return updateResponse;
    }
}
