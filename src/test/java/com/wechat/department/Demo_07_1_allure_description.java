package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import com.wechat.task.EvnHelperTask;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Epic企业微信接口测试用例")
@Feature("Feature部门相关功能测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_07_1_allure_description {
    static String accessToken;
    //    static String departmentId;
    @BeforeAll
    public static void getAccessToken(){
        accessToken = TokenHelper.getAccessToken();
    }

    @BeforeEach
    @AfterEach
    void clearDepartment(){
        EvnHelperTask.clearDpartMentTask(accessToken);
    }


    @Description("Description这个测试方法会测试创建部门的功能-入参数据驱动")
    @Story("Story创建部门测试")
    @DisplayName("创建部门")
    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/data/createDepartment.csv", numLinesToSkip = 1)
    void createDepartment(String name,String name_en, String returncode){
//        String name = "广州研发中心"+System.currentTimeMillis();
        Response response = DepartMentObject.creatDepartMent(name,name_en,accessToken);
        String departmentId = response.path("id")!=null ? response.path("id").toString():null;
        assertEquals(returncode,response.path("errcode").toString());
    }


    @Description("Description这个测试方法会测试修改部门的功能")
    @Story("Story修改部门测试")
    @DisplayName("修改部门")
    @Order(2)
    @Test
    void updateDepartment(){
        String departmentId = DepartMentObject.creatDepartMent(accessToken);
        String updateName = "广州研发中心修改"+System.currentTimeMillis();
        Response updateResponse = DepartMentObject.updateDepartMent(departmentId,updateName,accessToken);
        assertEquals("0",updateResponse.path("errcode").toString());
    }


    @Description("Description这个测试方法会测试查询部门的功能")
    @Story("Story查询部门测试")
    @DisplayName("查询部门")
    @Order(3)
    @Test
    void listDepartment(){
        String departmentId = DepartMentObject.creatDepartMent(accessToken);

        Response response = DepartMentObject.listDepartMent(departmentId,accessToken);
        assertEquals("0",response.path("errcode").toString());
        assertEquals(departmentId,response.path("department.id[0]").toString());
    }


    @Description("Description这个测试方法会测试删除部门的功能")
    @Story("Story删除部门测试")
    @DisplayName("删除部门")
    @Order(4)
    @Test
    void deleteDepartment(){
        String departmentId = DepartMentObject.creatDepartMent(accessToken);

        Response response =  DepartMentObject.deletDepartMent(departmentId,accessToken);
        assertEquals("0",response.path("errcode").toString());
    }
}
