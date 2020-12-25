package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import com.wechat.task.EvnHelperTask;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1、基础脚本，分别执行了，创建、修改、查询、删除接口并进行了校验
 * 2、进行了优化，方法间进行解耦，每个方法可以独立行
 * 3、进行了优化，使用时间戳命名法避免入参重复造成的报错。
 * 4、进行了优化，每次方法执行前后都对历史数据进行清理，确保每次执行脚本数据环境一致。
 * 5、进行了优化，对脚本进行了分层，减少了重复代码，提高了代码复用率，并减少了维护成本。
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_04_layer {
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


    @DisplayName("创建部门")
    @Order(1)
    @Test
    void createDepartment(){
        String name = "广州研发中心"+System.currentTimeMillis();
        String creatEnName="en_name"+ System.currentTimeMillis();
        Response response = DepartMentObject.creatDepartMent(name,creatEnName,accessToken);
        String departmentId = response.path("id")!=null ? response.path("id").toString():null;
        assertEquals("0",response.path("errcode").toString());
    }


    @DisplayName("修改部门")
    @Order(2)
    @Test
    void updateDepartment(){
        String departmentId = DepartMentObject.creatDepartMent(accessToken);
        String updateName = "广州研发中心修改"+System.currentTimeMillis();
        Response updateResponse = DepartMentObject.updateDepartMent(departmentId,updateName,accessToken);
        assertEquals("0",updateResponse.path("errcode").toString());
    }


    @DisplayName("查询部门")
    @Order(3)
    @Test
    void listDepartment(){
        String departmentId = DepartMentObject.creatDepartMent(accessToken);

        Response response = DepartMentObject.listDepartMent(departmentId,accessToken);
        assertEquals("0",response.path("errcode").toString());
        assertEquals(departmentId,response.path("department.id[0]").toString());
    }


    @DisplayName("删除部门")
    @Order(4)
    @Test
    void deleteDepartment(){
        String departmentId = DepartMentObject.creatDepartMent(accessToken);

        Response response =  DepartMentObject.deletDepartMent(departmentId,accessToken);
        assertEquals("0",response.path("errcode").toString());
    }
}
