package com.vvit.hemanth2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class Login extends AppCompatActivity {
    CardView teacher,student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        teacher=findViewById(R.id.Teacher_login);
        student=findViewById(R.id.student_report);
        teacher.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this,TeacherLogin.class);
            startActivity(intent);
        });
        student.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this,StudentPercentage.class);
            startActivity(intent);
        });
    }
}