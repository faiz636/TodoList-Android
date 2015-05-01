package com.example.muhammadfaizankhan.todo_list;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Faizan Khan on 13/04/2015.
 */
public class Util {
    public static List<Todo> getTodoListDummyData(boolean done){
        List<Todo> listData =new ArrayList<Todo>();
        Log.i("Util:getTodoListDumm: ", "generating dummy data");

        for (int i = 0; i < 2; i++) {
            listData.add(new Todo("list item :" + i,done));
//            Log.i("Util:getTodoListDumm: ", " obj created: "+i);
        }
        return listData;
    }
}
