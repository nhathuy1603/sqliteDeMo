package com.example.sqlitedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.PrimitiveIterator;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private ListView lvdatas;
    private Button btnadds,xoa,btnupdate;
    private EditText editname, editphone,editid;

    private ArrayAdapter<user> adapter;
    private ArrayList<user> userlist = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initdata();
        createCategory();
        lvdatas = findViewById(R.id.lvdata);
        editname = findViewById(R.id.editname);
        editphone = findViewById(R.id.editphone);
        editid = findViewById(R.id.id);
        btnadds = findViewById(R.id.btnAdd);


        btnupdate = findViewById(R.id.btnupdate);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                loaddata();
            }
        });
        btnadds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertrow();
                loaddata();
            }
        });
        xoa= findViewById(R.id.btnXoa);
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteContact();
                loaddata();
            }
        });
        adapter = new ArrayAdapter<user>(this,0,userlist){

            public View getView(int position,View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item,null);
                TextView tvname =convertView.findViewById(R.id.tvname);
                TextView tvphone =convertView.findViewById(R.id.tvphone);
                TextView tvid = convertView.findViewById(R.id.tvid);

                user u = userlist.get(position);
                tvid.setText(u.getId());
                tvname.setText(u.getName());
                tvphone.setText(u.getTheloai());
                return convertView;
            }
        };


        lvdatas.setAdapter(adapter);

        loaddata();
    }

    private void initdata(){
        db= openOrCreateDatabase("QuanLyMayTinh.db",MODE_PRIVATE,null);

        String sql ="CREATE TABLE IF NOT EXISTS MayTinh (id text primary key ,name text, theloai text references createCategory(maCategory) on delete cascade )";
        db.execSQL(sql);
    }

    private void createCategory(){
        String sql ="CREATE TABLE IF NOT EXISTS Category (id text primary key ,name text, maCategory text )";
        db.execSQL(sql);
    }
    private void isertCategory(){

        String sql = "INSERT INTO Category (id,name,maCategory) values ('001','Categoty1','Acer')," +
                "('002','Categoty2','Thinkpab'),('003','Categoty3','asus')";
        db.execSQL(sql);
    }
    private void insertrow(){
        String id = editid.getText().toString();
        String name = editname.getText().toString();
        String theloai = editphone.getText().toString();
        String sql = "INSERT INTO MayTinh (id,name,theloai) values ('" + id +"','" + name +"','" + theloai +"')";
        db.execSQL(sql);
    }
    public void deleteContact()
    {
        String id = editid.getText().toString();
        db.delete("MayTinh","id=?",new String[]{id});
    }

    public boolean update()
    {
        String id = editid.getText().toString();
        String name = editname.getText().toString();
        String theloai = editphone.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("theloai",theloai);
        db.update("MayTinh", cv, "id =?", new String[]{id});
        return true;
    }


    private void loaddata(){
         userlist.clear();
        String sql = "SELECT * FROM MayTinh";
        Cursor cursor =db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String id =cursor.getString(0);
            String name = cursor.getString(1);
            String thloai = cursor.getString(2);
             user u = new user();
             u.setId(id);
             u.setName(name);
             u.setTheloai(thloai);
            userlist.add(u);
            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
    }
}