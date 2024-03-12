package com.vvit.hemanth2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class SheetListActivity extends AppCompatActivity {
    private ListView sheetList;
    private ArrayAdapter adapter;
    public ArrayList<String> listItems=new ArrayList<>();
    private long cid;
    Toolbar toolbar;
    private String className,subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);
        className=getIntent().getStringExtra("className");
        subjectName=getIntent().getStringExtra("subjectName");
        cid =getIntent().getLongExtra("cid",-1);
        loadListItems();
        setToolBar();
        sheetList =findViewById(R.id.sheetList);
        adapter=new ArrayAdapter<>(this,R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);
        sheetList.setOnItemClickListener(((parent, view, position, id) -> opensheetActivity(position)));
    }

    private void setToolBar() {
        toolbar =findViewById(R.id.toolbar);
        TextView title =toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle =toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back =toolbar.findViewById(R.id.back);
        ImageButton save =toolbar.findViewById(R.id.save);
        save.setVisibility(View.INVISIBLE);
        title.setText(className);
        subtitle.setText(subjectName);
        back.setOnClickListener(v -> onBackPressed());
        ImageButton logout =toolbar.findViewById(R.id.log_out);
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(getApplicationContext(),TeacherLogin.class);
            startActivity(intent);
            finish();
        });
    }

    private void opensheetActivity(int position) {
        long[] idArray=getIntent().getLongArrayExtra("idArray");
        int[] rollArray=getIntent().getIntArrayExtra("rollArray");
        String[] nameArray=getIntent().getStringArrayExtra("nameArray");
        Intent intent =new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("month",listItems.get(position));
        intent.putExtra("className",className);
        intent.putExtra("subjectName",subjectName);
        startActivity(intent);
    }

    private void loadListItems() {
        Cursor cursor =new DbHelper(this).getDistinctMonths(cid);
        while (cursor.moveToNext()){
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DbHelper.DATE_KEY));
            listItems.add(date.substring(3));
        }
    }
}