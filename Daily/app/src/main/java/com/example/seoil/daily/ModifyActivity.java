package com.example.seoil.daily;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.seoil.daily.Adapter.DBHelper;
import com.example.seoil.daily.Adapter.MRecyclerAdapter;
import com.example.seoil.daily.Adapter.RItemTouchHelperCallback;
import com.example.seoil.daily.model.ListItem;

import java.util.ArrayList;
import java.util.Collections;
// 수정 페이지
public class ModifyActivity extends AppCompatActivity{
    private MRecyclerAdapter adapter;
    private ArrayList<ListItem> mItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        setRecyclerView();

        Button btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DBHelper helper = new DBHelper(ModifyActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();

                for(ListItem mitem : mItems){
                    if(mitem.getFinish() == 1){
                        db.execSQL("delete from tb_daily where _id = " + mitem.getUid());
                    }
                }
                db.close();
                adapter.notifyDataSetChanged();
                setData();

            }
        });
        //final CheckBox chk_all = (CheckBox) findViewById(R.id.chk_all);
        //chk_all.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
         //       adapter.setAllCheck(chk_all.isChecked());
          //      adapter.notifyItemRangeChanged(0,adapter.getItemCount());
           // }
        //}); 전체선택 체크박스
    }

    private void setRecyclerView(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new MRecyclerAdapter(this, mItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RItemTouchHelperCallback mCallback = new RItemTouchHelperCallback(adapter);
        ItemTouchHelper mitemTouchHelper = new ItemTouchHelper(mCallback);
        mitemTouchHelper.attachToRecyclerView(recyclerView);

        setData();
    }
    private void setData(){
        mItems.clear();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_daily order by _id asc",null);
        while(cursor.moveToNext()){
            mItems.add(new ListItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), 0));
        }
        db.close();
        adapter.notifyDataSetChanged();
    }
    public void onBackPressed(){
        Intent intent2 = new Intent();
        intent2.putExtra("result", "OK");
        setResult(2222, intent2);
        finish();
    }
}
