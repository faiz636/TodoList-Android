package com.example.muhammadfaizankhan.todo_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
            final Firebase myFirebaseRef = new Firebase("https://mytodoapp1.firebaseio.com/");
//        myFirebaseRef.child("message").setValue(new Todo("adsfasdf", false));

        final List<Todo> data = new ArrayList<Todo>();

        final CustomAdapter listCustomAdaptor_todo = new CustomAdapter(true);

        ListView listView_todo = (ListView) findViewById(R.id.listView_todo);
        listView_todo.setAdapter(listCustomAdaptor_todo);

        final CustomAdapter listCustomAdaptor_done = new CustomAdapter(false);

        ListView listView_done = (ListView) findViewById(R.id.listView_done);
        listView_done.setAdapter(listCustomAdaptor_done);

        listCustomAdaptor_done.setOtherAdapter(listCustomAdaptor_todo);
        listCustomAdaptor_todo.setOtherAdapter(listCustomAdaptor_done);

        Button button = (Button) findViewById(R.id.add_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e = (EditText) findViewById(R.id.add_editText);
                String s = e.getText().toString();
                if (s.length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
                } else {
                    Todo newTodoObject = new Todo(s, false);
                    e.setText("");
                    data.add(newTodoObject);
                    Map<String, Object> obj = new HashMap<String, Object>();
                    obj.put("dateTime", newTodoObject.getDateTime());
                    obj.put("description", newTodoObject.getDescription());
                    obj.put("done", newTodoObject.isDone());
                    Firebase ref = myFirebaseRef.child("todo").push();
                    ref.setValue(obj);
                    newTodoObject.setKey(ref.getKey());
                    //todo:duplication of row due to ChildEventListener and following line
//                    listCustomAdaptor_todo.addTodoObject(newTodoObject);
                    Toast.makeText(MainActivity.this, "Object Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myFirebaseRef.child("todo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("onChildAdded", "event called");
                Map<String, Object> obj = (HashMap<String, Object>) dataSnapshot.getValue();
                boolean done = (boolean) obj.get("done");
                String description = (String) obj.get("description");
//                GregorianCalendar dateTime = GregorianCalendar.getInstance().setTimeInMillis(Long.parseLong( obj.get("dateTime").toString() ) );//setTimeInMillis(Long.parseLong((String)obj.get("dateTime")));
//                Log.i("convertingDateObj",obj.get("dateTime").toString());
                Todo newTodoData = new Todo(description, done);
                newTodoData.setKey(dataSnapshot.getKey());
                if (done) {
                    listCustomAdaptor_done.addTodoObject(newTodoData);
                } else {
                    listCustomAdaptor_todo.addTodoObject(newTodoData);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("onChildChanged", dataSnapshot.toString());
                Map<String, Object> obj = (HashMap<String, Object>) dataSnapshot.getValue();
                boolean done = (boolean) obj.get("done");
                String key = (String) dataSnapshot.getKey();
                Log.i("got", key);
                if (done) {
                    listCustomAdaptor_todo.removeAddFromAdapter(key, done);
                } else {
                    listCustomAdaptor_done.removeAddFromAdapter(key, done);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("onChildRemoved", dataSnapshot.toString());
                Map<String, Object> obj = (HashMap<String, Object>) dataSnapshot.getValue();
                boolean done = (boolean) obj.get("done");
                String key = (String) dataSnapshot.getKey();
                Log.i("onChildRemoved", key);
                if (!done) {
                    listCustomAdaptor_todo.removeFromAdapter(key, done);
                } else {
                    listCustomAdaptor_done.removeFromAdapter(key, done);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter {

        List<Todo> todoListDataForView;// = Util.getTodoListDummyData();
        CustomAdapter otherAdapter;
        boolean done;

        CustomAdapter(boolean done) {
            this.done = done;
            todoListDataForView = new ArrayList<Todo>();
            //Util.getTodoListDummyData(!done);
        }

        public void setOtherAdapter(CustomAdapter customAdapter) {
            otherAdapter = customAdapter;
        }

        @Override
        public int getCount() {
            return todoListDataForView.size();
        }

        @Override
        public Todo getItem(int position) {
            return todoListDataForView.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Log.i("getView ", "generating view");
            Log.i("getView:position ", "" + position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, parent, false);
            }

            TextView desctiptionView = (TextView) convertView.findViewById(R.id.description);
            TextView dateView = (TextView) convertView.findViewById(R.id.date);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox1);
            Button button = (Button) convertView.findViewById(R.id.remove);

            Todo data = todoListDataForView.get(position);

            desctiptionView.setText(data.getDescription());
            dateView.setText(data.getDateTime().getTime().toString());
            checkBox.setChecked(data.isDone());

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("oncheckedListener: ", " checked");
                    Todo a = todoListDataForView.get(position);
                    a.setDone(done);
                    todoListDataForView.remove(position);
                    notifyDataSetChanged();
                    Firebase myFirebaseRef = new Firebase("https://mytodoapp1.firebaseio.com/");
                    Map<String, Object> hash = new HashMap<String, Object>();
                    hash.put("done", done);
                    Log.i("ChildUpdate", "updatingChildValue" + a.getKey());
                    myFirebaseRef.child("todo").child(a.getKey()).updateChildren(hash);//setValue(new HashMap<String, Boolean>().put("done",done));
//                    myFirebaseRef.child("todo").child(a.getKey()).setValue(new HashMap<String, Boolean>().put("done",done));
                    if (otherAdapter != null)
                        otherAdapter.addTodoObject(a);
                    Toast.makeText(MainActivity.this, "event" + position, Toast.LENGTH_SHORT).show();
                }

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }

            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Todo a = todoListDataForView.get(position);
                    todoListDataForView.remove(position);
                    notifyDataSetChanged();
                    Firebase myFirebaseRef = new Firebase("https://mytodoapp1.firebaseio.com/");
                    Log.i("ChildUpdate", "updatingChildValue" + a.getKey());
                    myFirebaseRef.child("todo").child(a.getKey()).removeValue();//setValue(new HashMap<String, Boolean>().put("done",done));

                }
            });

            return convertView;
        }

        public void addTodoObject(Todo obj) {
            Log.i("addTodoObject", "adding object to list");
            todoListDataForView.add(obj);
            notifyDataSetChanged();
        }

        public boolean removeAddFromAdapter(String key, boolean done) {
//            Log.i("key:",key);
            for (int i = 0, len = getCount(); i < len; i++) {
                Todo obj = todoListDataForView.get(i);
                Log.i("key:", obj.getKey());
                if (obj.getKey().equals(key)) {
                    Log.i("removing", obj.getKey());
                    todoListDataForView.remove(i);
                    obj.setDone(done);
                    otherAdapter.addTodoObject(obj);
                    notifyDataSetChanged();
                    return true;
                }
            }
            return false;
        }

        public boolean removeFromAdapter(String key, boolean done) {
//            Log.i("key:",key);
            for (int i = 0, len = getCount(); i < len; i++) {
                Todo obj = todoListDataForView.get(i);
                Log.i("key:", obj.getKey());
                if (obj.getKey().equals(key)) {
                    Log.i("removing", obj.getKey());
                    todoListDataForView.remove(i);
                    notifyDataSetChanged();
                    return true;
                }
            }
            return false;
       }
    }
}