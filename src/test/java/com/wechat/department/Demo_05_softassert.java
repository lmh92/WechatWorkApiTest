package com.wechat.department;

import com.wechat.apiobject.DepartMentObject;
import com.wechat.apiobject.TokenHelper;
import com.wechat.task.EvnHelperTask;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 1、基础脚本，分别执行了，创建、修改、查询、删除接口并进行了校验
 * 2、进行了优化，方法间进行解耦，每个方法可以独立行
 * 3、进行了优化，使用时间戳命名法避免入参重复造成的报错。
 * 4、进行了优化，每次方法执行前后都对历史数据进行清理，确保每次执行脚本数据环境一致。
 * 5、进行了优化，对脚本进行了分层，减少了重复代码，提高了代码复用率，并减少了维护成本。
 * 6、进行了优化，因为要覆盖不同的入参组合，以数据驱动的方式将入参从代码剥离。
 * 7、进行了优化，使用Junit5提供的Java 8 lambdas的断言方法，增加了脚本的容错性。
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo_05_softassert {
    static String accessToken;
    //    static String departmentId;
    @BeforeAll
    public static void getAccessToken(){
        accessToken = TokenHelper.getAccessToken();
    }

    @BeforeEach
    @AfterEach
    @Test
    void clearDepartment(){
        EvnHelperTask.clearDpartMentTask(accessToken);
    }


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
        String creatName= "name"+System.currentTimeMillis();
        String creatEnName="en_name"+System.currentTimeMillis();
        Response createResponse = DepartMentObject.creatDepartMent(creatName,creatEnName,accessToken);
        String departmentId = createResponse.path("id").toString()!=null?createResponse.path("id").toString():null;

        Response response = DepartMentObject.listDepartMent(departmentId,accessToken);
        assertEquals("0",response.path("errcode").toString());
        assertEquals(departmentId,response.path("department.id[0]").toString());
        assertAll("查询返回值校验",
                ()->assertEquals("1",response.path("errcode")),
                ()->assertEquals(departmentId+"x",response.path("department.id[0]").toString()),
                ()->assertEquals(creatName+"x",response.path("department.name[0]").toString()),
                ()->assertEquals(creatEnName+"x",response.path("department.name_en[0]").toString())
        );
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
