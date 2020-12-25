package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Demo_06_02_concurent {

    static String accessToken;
    //    static String departmentId;
    @BeforeAll
    public static void getAccessToken(){
        accessToken = TokenHelper.getAccessToken();
    }

//    @BeforeEach
//    @AfterEach
//    void clearDepartment(){
//        EvnHelperTask.clearDpartMentTask(accessToken);
//    }


    @DisplayName("创建部门")
    @Test
    @RepeatedTest(10)
    void createDepartment(){
        String backendStr = Thread.currentThread().getId()+System.currentTimeMillis()+"";
        String name = "广州研发中心"+backendStr;
        String creatEnName="en_name"+ backendStr;
        Response response = DepartMentObject.creatDepartMent(name,creatEnName,accessToken);
        String departmentId = response.path("id")!=null ? response.path("id").toString():null;
        assertEquals("0",response.path("errcode").toString());
    }


    @DisplayName("修改部门")
    @Test
    @RepeatedTest(10)
    void updateDepartment(){
        String backendStr = Thread.currentThread().getId()+System.currentTimeMillis()+"";
        String name = "广州研发中心"+backendStr;
        String creatEnName="en_name"+ backendStr;
        Response response = DepartMentObject.creatDepartMent(name,creatEnName,accessToken);
        String departmentId = response.path("id")!=null ? response.path("id").toString():null;

        String updateName = "广州研发中心修改"+backendStr;
        Response updateResponse = DepartMentObject.updateDepartMent(departmentId,updateName,accessToken);
        assertEquals("0",updateResponse.path("errcode").toString());
    }
}
