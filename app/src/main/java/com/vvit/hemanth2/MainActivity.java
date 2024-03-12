package com.vvit.hemanth2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems= new ArrayList<>();
    Toolbar toolbar;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);
        fab=findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog());
        loadData();
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        classAdapter=new ClassAdapter(this,classItems);
        recyclerView.setAdapter(classAdapter);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        setToolbar();
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        if (user==null){
            Intent intent= new Intent(getApplicationContext(),TeacherLogin.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("Range")
    private void loadData() {
        Cursor cursor = dbHelper.getClassTable();
        classItems.clear();
        while (cursor.moveToNext()){
            int id= cursor.getInt(cursor.getColumnIndex(DbHelper.C_ID));
            String className = cursor.getString(cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY));
            String subjectName = cursor.getString(cursor.getColumnIndex(DbHelper.SUBJECT_NAME_KEY));
            classItems.add(new ClassItem(id,className,subjectName));

        }
    }


    private void setToolbar() {
        toolbar =findViewById(R.id.toolbar);
        TextView title =toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle =toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back =toolbar.findViewById(R.id.back);
        ImageButton save =toolbar.findViewById(R.id.save);
        ImageButton logout =toolbar.findViewById(R.id.log_out);
        title.setText("Attendance App");
        subtitle.setVisibility(View.GONE);
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(v -> onBackPressed());
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(getApplicationContext(),TeacherLogin.class);
            startActivity(intent);
            finish();
        });
    }

    private void gotoItemActivity(int position) {
        Intent intent =new Intent(this,StudentActivity.class);
        intent.putExtra("className",classItems.get(position).getClassName());
        intent.putExtra("subjectName",classItems.get(position).getSubjectName());
        intent.putExtra("position",position);
        intent.putExtra("cid",classItems.get(position).getCid());
        startActivity(intent);
    }

    private void showDialog(){
        MyDialog dialog =new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener((className,subjectName)->addclass(className,subjectName));
    }

    private void addclass(String className, String subjectName) {
        long cid = dbHelper.addClass(className,subjectName);
        ClassItem classItem=new ClassItem(cid,className,subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog =new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setListener((className,subjectName)->updateClass(position,className,subjectName));
    }

    private void updateClass(int position, String className, String subjectName) {
        dbHelper.updateClass(classItems.get(position).getCid(),className,subjectName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getCid());
        classItems.remove(position);
        classAdapter.notifyItemRemoved(position);
    }
}