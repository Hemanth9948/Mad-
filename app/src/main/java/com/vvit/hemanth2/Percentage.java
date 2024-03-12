package com.vvit.hemanth2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class Percentage extends AppCompatActivity {
    public String classname;
    public String student_name;
    FloatingActionButton fab;
    public String student_roll;
    public  String date;
    public  String date1;
    public long cid,ccid;
    public long sid,ssid,sv=0;
    TextView cl,st,ro,da;
    private int p,a=0;
    private String name;
    public ArrayList<Long> listItems=new ArrayList<>();
    public ArrayList<String> listItems1=new ArrayList<>();
    public ArrayList<Long> listItems2=new ArrayList<>();
    public ArrayList<Long> listItems9=new ArrayList<>();
    public ArrayList<String> listItems3=new ArrayList<>();
    public ArrayList<String> listItems4=new ArrayList<>();
    public ArrayList<String> listItems7=new ArrayList<>();
    public ArrayList<String> listItems8=new ArrayList<>();
    public ArrayList<Long> listItems5=new ArrayList<>();
    public ArrayList<Long> listItems6=new ArrayList<>();
    ProgressBar progressBar;
    public int per;
    public int days=0;
    public float pa;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage);
        Intent intent =getIntent();
        classname=intent.getStringExtra("classname");
        student_name= intent.getStringExtra("student_name");
        student_roll=intent.getStringExtra("student_roll");
        date=intent.getStringExtra("date");
       fab=findViewById(R.id.floatin_button);
       fab.setOnClickListener(v -> showDialog());
        loadListItems();
        textView=findViewById(R.id.text_view);
        cl=findViewById(R.id._class_name1_);
        st=findViewById(R.id._student_name1_);
        ro=findViewById(R.id._student_roll1_);
        da=findViewById(R.id.date2);
        progressBar =findViewById(R.id.progress_bar);
        percentage();


    }

    private void showDialog() {
        Days dialog =new Days();
        dialog.show(getSupportFragmentManager(),Days.Days_ADD_DIALOG);
        dialog.setListener((day)->addDays(day));
    }

    public  void addDays(String day) {
        days= Integer.parseInt(day);
        p=0;a=0;
        caluclate();
        percentage();
    }


    private void percentage() {
        progressBar.setProgress(per);
        textView.setText(per+"%");
        cl.setText(classname);
        st.setText(student_name);
        ro.setText(student_roll);
        da.setText(date);
    }


    @SuppressLint("Range")
    private void loadListItems() {
        Cursor cursor = new DbHelper(this).getCID();
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY));
            ccid = cursor.getLong(cursor.getColumnIndex(DbHelper.C_ID));
            listItems1.add(name);
            listItems.add(ccid);
        }
        for (int i=0;i<listItems1.size();i++){
            if(listItems1.get(i).equals(classname)) {
                cid =(long) listItems.get(i);
            }
        }


        Cursor cursors1= new DbHelper(this).getSID();
        while (cursors1.moveToNext()) {
            name = cursors1.getString(cursors1.getColumnIndex(DbHelper.STUDENT_NAME_KEY));
            ssid = cursors1.getLong(cursors1.getColumnIndex(DbHelper.S_ID));
            ccid = cursors1.getLong(cursors1.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            listItems3.add(name);
            listItems2.add(ssid);
            listItems9.add(ccid);
        }
        for (int i=0;i<listItems2.size();i++){
            sv= Long.parseLong(student_roll);
            if(listItems3.get(i).equals(student_name)){
                if (listItems9.get(i).equals(sv)){

                    sid= (long) listItems2.get(i);
                }

            }

        }
        Cursor cursorse = new DbHelper(this).getReport();
        while (cursorse.moveToNext()) {
            name = cursorse.getString(cursorse.getColumnIndex(DbHelper.STATUS_KEY));
            date1 = cursorse.getString(cursorse.getColumnIndex(DbHelper.DATE_KEY));
            date1=date1.substring(3);
           ssid =cursorse.getLong(cursorse.getColumnIndex(DbHelper.S_ID));
           ccid =cursorse.getLong(cursorse.getColumnIndex(DbHelper.C_ID));
           listItems6.add(ccid);
           listItems5.add(ssid);
           listItems4.add(name);
           listItems8.add(date1);
        }
        for (int i=0;i<listItems4.size();i++){
            cid=Long.parseLong(String.valueOf(cid));
            sid=Long.parseLong(String.valueOf(sid));
            if (listItems6.get(i).equals(cid)){
                if (listItems5.get(i).equals(sid)){
                    if (listItems8.get(i).equals(date)) {
                        listItems7.add(listItems4.get(i));
                    }
                }
            }
        }
        days= getDAYINMONTH(date);
        days= days-4;
        caluclate();
    }
    public void caluclate() {
        for (int i=0;i<listItems7.size();i++){
            if(listItems7.get(i).equals("P")){
                p=p+1;
            }
            else { a=a+1;}
        }

        pa=days;
        pa=p/pa;
        pa=pa*100;
        per=(int) pa;
        Toast.makeText(this, p+"", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, days+"", Toast.LENGTH_SHORT).show();

    }
    private int getDAYINMONTH(String month) {
        int monthIndex =Integer.valueOf(month.substring(0,1));
        int year = Integer.valueOf(month.substring(4));
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthIndex);
        calendar.set(Calendar.YEAR,year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}