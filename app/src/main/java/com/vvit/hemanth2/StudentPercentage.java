package com.vvit.hemanth2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class StudentPercentage extends AppCompatActivity {
    Button button;
    EditText classname1;
    EditText student_name1;
    EditText student_roll1;
    EditText date1;
    String classname,student_name,student_roll,date;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_percentage);
        classname1= findViewById(R.id.classesname);
        student_name1= findViewById(R.id.studentesname);
        student_roll1= findViewById(R.id._studentroll_);
        date1= findViewById(R.id._date_);
        button=findViewById(R.id.percentage);
        setToolbar();
        MyCalendar calendar= new MyCalendar();
        date1.setText(calendar.getDate().substring(3));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classname=classname1.getText().toString();
                student_name=student_name1.getText().toString();
                student_roll=student_roll1.getText().toString();
                date=date1.getText().toString();
                Intent intent =new Intent(StudentPercentage.this,Percentage.class);
                intent.putExtra("classname",classname);
                intent.putExtra("student_name",student_name);
                intent.putExtra("student_roll",student_roll);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });

    }

    private void setToolbar() {
        toolbar =findViewById(R.id.toolbar);
        TextView title =toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle =toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back =toolbar.findViewById(R.id.back);
        ImageButton save =toolbar.findViewById(R.id.save);
        ImageButton logout =toolbar.findViewById(R.id.log_out);
        title.setText("Attendance App");
        MyCalendar calendar= new MyCalendar();
        subtitle.setText(calendar.getDate());
        save.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.INVISIBLE);
        back.setOnClickListener(v -> onBackPressed());
    }
}