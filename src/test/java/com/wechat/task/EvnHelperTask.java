package com.wechat.task;

import com.wechat.apiobject.DepartMentObject;

import java.util.ArrayList;

public class EvnHelperTask {
    public static void clearDpartMentTask(String accessToken){
        ArrayList<Integer> departmentList = DepartMentObject.listDepartMent("1",accessToken).path("department.id");
        for(int departmentId : departmentList){
            if(departmentId == 1){
                continue;
            }

            DepartMentObject.deletDepartMent(departmentId+"",accessToken);
        }
    }
}
