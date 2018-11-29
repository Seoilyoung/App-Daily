package com.example.seoil.daily;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.seoil.daily.Adapter.DBHelper;
import com.example.seoil.daily.Adapter.RItemTouchHelperCallback;
import com.example.seoil.daily.Adapter.RecylcerAdapter;
import com.example.seoil.daily.databinding.ActivityMainBinding;
import com.example.seoil.daily.model.ListItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ActivityMainBinding mainBinding;
    private RecylcerAdapter adapter;
    private ArrayList<ListItem> mItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainBinding.txtDate.setText(new SimpleDateFormat("yyyy년 MM월 dd일 (E)").format(new Date()));
        mainBinding.swipeRefreshLayout.setOnRefreshListener(this);
        setRecyclerView();
        mainBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder at = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("일정 추가");
                final EditText edit_insert = new EditText(MainActivity.this);
                        at.setView(edit_insert)
                        .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String txt_content = edit_insert.getText().toString();
                                //Toast.makeText(getApplicationContext(),txt_insert,Toast.LENGTH_LONG).show();
                                DBHelper helper = new DBHelper(MainActivity.this);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                db.execSQL("insert into tb_daily (content, finish) values ('" + txt_content + "',0)");
                                Cursor INDEX= db.rawQuery("select max(_id) from tb_daily",null);
                                INDEX.moveToFirst();
                                mItems.add(new ListItem(Integer.parseInt(INDEX.getString(0)), txt_content,0));
                                db.close();
                                INDEX.close();
                                adapter.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("취소",null)
                        .show();
            }
        });
        mainBinding.fabModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                startActivityForResult(intent,1111);
            }
        });
    }

    private void setRecyclerView(){
        mainBinding.recyclerView.setHasFixedSize(true);

        adapter = new RecylcerAdapter(this, mItems);
        mainBinding.recyclerView.setAdapter(adapter);

        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setData();
    }
    private void setData(){
        mItems.clear();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_daily order by finish asc, _id asc",null);
        while(cursor.moveToNext()){
            mItems.add(new ListItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2))));
        }

        db.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(){
        mainBinding.txtDate.setText(new SimpleDateFormat("yyyy년 MM월 dd일 (E)").format(new Date()));
        setData();
        adapter.notifyDataSetChanged();
        mainBinding.swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1111 &&resultCode == 2222){
            setData();
        }
    }
}
