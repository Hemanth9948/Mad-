package com.vvit.hemanth2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String className,subjectName;
    private int Days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        className=getIntent().getStringExtra("className");
        subjectName=getIntent().getStringExtra("subjectName");
        showTable();
        setToolBar();
    }

    private void setToolBar() {toolbar =findViewById(R.id.toolbar);
        TextView title =toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle =toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back =toolbar.findViewById(R.id.back);
        ImageButton save =toolbar.findViewById(R.id.save);
        title.setText(className);
        subtitle.setText(subjectName);
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(v -> onBackPressed());
        ImageButton logout =toolbar.findViewById(R.id.log_out);
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(getApplicationContext(),TeacherLogin.class);
            startActivity(intent);
            finish();
        });
    }

    private void showTable() {
        DbHelper dbHelper=new DbHelper(this);
        TableLayout tableLayout=findViewById(R.id.tableLayout);
        long[] idArray=getIntent().getLongArrayExtra("idArray");
        int[] rollArray=getIntent().getIntArrayExtra("rollArray");
        String[] nameArray=getIntent().getStringArrayExtra("nameArray");
        String month=getIntent().getStringExtra("month");
        int DAY_IN_MONTH = getDAYINMONTH(month);
        Days=DAY_IN_MONTH;
        int rowSize=idArray.length+1;
        TableRow[] rows =new TableRow[rowSize];
        TextView[] rolls_tvs=new TextView[rowSize];
        TextView[] name_tvs=new TextView[rowSize];
        TextView[][] status_tvs =new TextView[rowSize][DAY_IN_MONTH+1];
        for (int i=0;i<rowSize;i++){
            rolls_tvs[i]=new TextView(this);
            name_tvs[i]=new TextView(this);
            for (int j=1;j<= DAY_IN_MONTH;j++){
                status_tvs[i][j]=new TextView(this);
            }
        }
        rolls_tvs[0].setText("Roll");
        rolls_tvs[0].setTypeface(rolls_tvs[0].getTypeface(), Typeface.BOLD);
        name_tvs[0].setText("Name");
        name_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);
        for (int i=1;i<= DAY_IN_MONTH;i++){
            status_tvs[0][i].setText(String.valueOf(i));
        }
        for (int i=1;i<rowSize;i++){
            rolls_tvs[i].setText(String.valueOf(rollArray[i-1]));
            name_tvs[i].setText(nameArray[i-1]);
            for (int j=1;j<= DAY_IN_MONTH;j++) {
                String day =String.valueOf(j);
                status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(), Typeface.BOLD);
                if (day.length()==1)day="0"+day;
                String date = day+"."+month;
                String status = dbHelper.getStatus(idArray[i-1],date);
                status_tvs[i][j].setText(status);
            }
        }
        for (int i=0;i<rowSize;i++){
            rows[i]=new TableRow(this);
            if (i%2==0)
                rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            else rows[i].setBackgroundColor(Color.parseColor("#E4E4E4"));
            rolls_tvs[i].setPadding(16,16,16,16);
            name_tvs[i].setPadding(16,16,16,16);
            rows[i].addView(rolls_tvs[i]);
            rows[i].addView(name_tvs[i]);
            for (int j=1;j<= DAY_IN_MONTH;j++) {
                status_tvs[i][j].setPadding(16,16,16,16);
                rows[i].addView(status_tvs[i][j]);
                if (status_tvs[i][j].getText().toString().equals("P")){
                    status_tvs[i][j].setBackgroundColor(Color.parseColor("#3300c500"));
                }
                else if(status_tvs[i][j].getText().toString().equals("A")){
                    status_tvs[i][j].setBackgroundColor(Color.parseColor("#33FF0000"));
                }
            }
            tableLayout.addView(rows[i]);
            }
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);
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