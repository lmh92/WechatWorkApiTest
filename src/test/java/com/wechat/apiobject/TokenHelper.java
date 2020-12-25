package com.wechat.apiobject;

import static io.restassured.RestAssured.given;

public class TokenHelper {
    public static String getAccessToken(){
        String accessToken = given().log().all()
                .param("corpid","ww257df1f7abf7c10c")
                .param("corpsecret","fiLyFCotGF_xpOSxCFJ9Cy8mup5KOscBKmDD4Obez1k")
                .when()
                .get(" https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then()
                .log().all()
                .extract().response().path(  "access_token");
        return accessToken;
    }
}
