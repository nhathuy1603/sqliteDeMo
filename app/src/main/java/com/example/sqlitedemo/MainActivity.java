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
    private SQLiteDatabase db_156;
    private ListView lvdatas_156;
    private Button btnadds_156,xoa_156,btnupdate_156;
    private EditText editname_156, editphone_156,editid_156;

    private ArrayAdapter<user> adapter;
    private ArrayList<user> userlist = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initdata();
        lvdatas_156 = findViewById(R.id.lvdata);
        editname_156 = findViewById(R.id.editname);
        editphone_156 = findViewById(R.id.editphone);
        editid_156 = findViewById(R.id.id);
        btnadds_156 = findViewById(R.id.btnAdd);


        btnupdate_156 = findViewById(R.id.btnupdate);
        btnupdate_156.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                loaddata();
            }
        });
        btnadds_156.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertrow();
                loaddata();
            }
        });
        xoa_156= findViewById(R.id.btnXoa);
        xoa_156.setOnClickListener(new View.OnClickListener() {
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


        lvdatas_156.setAdapter(adapter);

        loaddata();
    }

    private void initdata(){
        db_156= openOrCreateDatabase("QuanLySinhVien.db",MODE_PRIVATE,null);

        String sql ="CREATE TABLE IF NOT EXISTS SinhVien (id text primary key ,name text, theloai text references createCategory(maCategory) on delete cascade )";
        db_156.execSQL(sql);
    }


    private void insertrow(){
        String id = editid_122.getText().toString();
        String name = editname_122.getText().toString();
        String theloai = editphone_122.getText().toString();
        String sql = "INSERT INTO SinhVien (id,name,theloai) values ('" + id +"','" + name +"','" + theloai +"')";
        db_156.execSQL(sql);
    }
    public void deleteContact()
    {
        String id = editid_156.getText().toString();
        db_156.delete("SinhVien","id=?",new String[]{id});
    }

    public boolean update()
    {
        String id = editid_156.getText().toString();
        String name = editname_156.getText().toString();
        String theloai = editphone_156.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("theloai",theloai);
        db_156.update("SinhVien", cv, "id =?", new String[]{id});
        return true;
    }


    private void loaddata(){
         userlist.clear();
        String sql = "SELECT * FROM SinhVien";
        Cursor cursor =db_156.rawQuery(sql,null);
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
