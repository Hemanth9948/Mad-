package com.vvit.hemanth2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;

    private String className;
    private int p,a=0;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems=new ArrayList<>();
    private DbHelper dbHelper;
    private long cid;
    private MyCalendar calendar;
    private TextView subtitle;
    private static int pr,ab;

    public static int present() {
        return pr;
    }

    public static int absent() {
        return ab;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_view);
        calendar=new MyCalendar();
        dbHelper =new DbHelper(this);
        Intent intent = getIntent();
        className=intent.getStringExtra("className");
        subjectName=intent.getStringExtra("subjectName");
        position =intent.getIntExtra("position",-1);
        cid = intent.getLongExtra("cid",-1);
        setToolbar();
        loadData();
        recyclerView =findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> changeStatus(position));
        loadStatusData();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.add_student:
                    showAddStudentDialog();
                    return true;
                case R.id.show_Calendar:
                    showCalendar();
                    return true;
                case R.id.select_all:
                    select();
                    return true;
                case R.id.show_attendance_sheet:
                    openSheetList();
                    return true;

            }
            return true;
        });
    }

    private void select() {
        for (int i=0;i<studentItems.size();i++) {
            String status = studentItems.get(i).getStatus();
            if (status!="P") status = "P";
            else status="A";
            studentItems.get(i).setStatus(status);
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("Range")
    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()){
            long sid= cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            int roll= cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            String Name = cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid,roll,Name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status=studentItems.get(position).getStatus();
        if (status.equals("P")){
            status="A";
        }
        else {
            status="P";
        }
        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar =findViewById(R.id.toolbar);
        TextView title =toolbar.findViewById(R.id.title_toolbar);
        subtitle =toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back =toolbar.findViewById(R.id.back);
        ImageButton save =toolbar.findViewById(R.id.save);
        save.setOnClickListener(v -> saveStatus());
        title.setText(className);
        subtitle.setText(subjectName+" | "+calendar.getDate());
        back.setOnClickListener(v -> onBackPressed());
        ImageButton logout =toolbar.findViewById(R.id.log_out);
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(getApplicationContext(),TeacherLogin.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveStatus() {
        MyDialog dialog= new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.SAVE_DIALOG);
        for (StudentItem studentItem : studentItems){
            String status =studentItem.getStatus();
            if (status!="P") {status="A";
            }
           long value= dbHelper.addStatus(studentItem.getSid(),cid,calendar.getDate(),status);
           if(value==-1)dbHelper.updateStatus(studentItem.getSid(),calendar.getDate(),status);
        }
        for (int i=0,p=0,a=0;i<studentItems.size();i++){
            String status =studentItems.get(i).getStatus();
            if (status=="P") {;
                p=p+1;
            }
            else if(status!="P"){
                a=a+1;
            }
            pr=p;
            ab=a;
        }
    }

    private void loadStatusData(){
        for (StudentItem studentItem : studentItems){
            String status =dbHelper.getStatus(studentItem.getSid(),calendar.getDate());
            if (status!=null)studentItem.setStatus(status);
            else studentItem.setStatus(" ");
            adapter.notifyDataSetChanged();

        }

    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }
        else if (menuItem.getItemId()==R.id.show_Calendar){
            showCalendar();
        }
        else if (menuItem.getItemId()==R.id.show_attendance_sheet){
            openSheetList();
        }
        return true;
    }

    private void openSheetList() {
        long[] idArray= new long[studentItems.size()];
        int[] rollArray= new int[studentItems.size()];
        String[] nameArray= new String[studentItems.size()];
        for (int i=0;i<idArray.length;i++)
            idArray[i]= studentItems.get(i).getSid();
        for (int i=0;i<rollArray.length;i++)
            rollArray[i]= studentItems.get(i).getRoll();
        for (int i=0;i<nameArray.length;i++)
            nameArray[i]= studentItems.get(i).getName();
        Intent intent =new Intent(this,SheetListActivity.class);
        intent.putExtra("cid",cid);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("className",className);
        intent.putExtra("subjectName",subjectName);
        startActivity(intent);

    }

    private void showCalendar() {

        calendar.show(getSupportFragmentManager()," ");
        calendar.setOnCalendarOkClickListener((this::onCalendarOkClicked));
    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calendar.setDate(year,month,day);
        subtitle.setText(subjectName+" | "+calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        MyDialog dialog =new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
                    
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog =new MyDialog(studentItems.get(position).getRoll(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.updateStudent(studentItems.get(position).getSid(),name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private void addStudent(String roll_string, String name) {
        int roll=Integer.parseInt(roll_string);
        long sid= dbHelper.addStudent(cid,roll,name);
        StudentItem studentItem =new StudentItem(sid,roll,name);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }
}