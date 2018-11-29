package com.example.seoil.daily.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.seoil.daily.R;
import com.example.seoil.daily.model.ListItem;

import java.util.ArrayList;
import java.util.Collections;

import static android.media.CamcorderProfile.get;

// 수정 페이지 어댑터
public class MRecyclerAdapter extends RecyclerView.Adapter<MItemViewHolder> implements RItemTouchHelperCallback.OnItemMoveListener{
    ArrayList<ListItem> mItems;
    Context mContext;

    public MRecyclerAdapter(Context context, ArrayList<ListItem> items){
        mContext = context;
        mItems = items;
    }

    @Override
    public MItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new MItemViewHolder(view);
    }
    @Override // 아이템 클릭시 이벤트
    public void onBindViewHolder(final MItemViewHolder holder, final int position) {
        holder.mContent.setText(position+1 + ". " + mItems.get(position).getContent());
        holder.mFinish.setChecked(false);
        holder.mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 체크박스 클릭 // 체크박스 관리 0:미달성 1:달성 2: 미달성+제거선택 3:달성+제거선택
                if(holder.mFinish.isChecked()){
                    if(mItems.get(position).getFinish() == 0) mItems.get(position).setFinish(2);
                    if(mItems.get(position).getFinish() == 1) mItems.get(position).setFinish(3);
                }
                else{
                    if(mItems.get(position).getFinish() == 2) mItems.get(position).setFinish(0);
                    if(mItems.get(position).getFinish() == 3) mItems.get(position).setFinish(1);
                }
            }
        });
        holder.mlist_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 일정 클릭
                final View vw = view;
                AlertDialog.Builder at = new AlertDialog.Builder(vw.getContext()).setTitle("일정 변경");
                final EditText edit_insert = new EditText(vw.getContext());
                edit_insert.setText(mItems.get(position).getContent());
                edit_insert.setSelection(edit_insert.length());
                at.setView(edit_insert)
                        .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String txt_content = edit_insert.getText().toString();
                                //Toast.makeText(getApplicationContext(),txt_insert,Toast.LENGTH_LONG).show();
                                DBHelper helper = new DBHelper(vw.getContext());
                                SQLiteDatabase db = helper.getWritableDatabase();
                                db.execSQL("update tb_daily set content = '" + txt_content + "' where _id = " + mItems.get(position).getUid());
                                db.close();
                                ListItem listItem= new ListItem(mItems.get(position).getUid(), txt_content, mItems.get(position).getFinish());
                                mItems.set(position,listItem);
                                notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("취소",null)
                        .show();
                Log.i("클릭정보","그냥 클릭");
            }
        });
    }

    @Override // 일정 갯수
    public int getItemCount() {
        return mItems.size();
    }

    @Override //일정 순서 변경
    public boolean onItemMove(int fromPosition, int toPosition) {

        Log.i("모션정보", "onItmeMove 접속");
        Collections.swap(mItems, fromPosition, toPosition);
        /*
        int tmp_id = mItems.get(fromPosition).getUid();
        mItems.get(fromPosition).setUid(mItems.get(toPosition).getUid());
        mItems.get(toPosition).setUid(tmp_id);
        */
        /* 순서 바뀔때 마다 db반영 -> 빨리하면 db가 밀리는 오류
        String tmp_content = mItems.get(fromPosition).getContent();
        int tmp_tofinish = mItems.get(toPosition).getFinish();
        int tmp_fromfinish = mItems.get(fromPosition).getFinish();
        tmp_tofinish %= 2;
        tmp_fromfinish %= 2;
        if(p_to != mItems.get(toPosition).getUid()){
            p_to = mItems.get(toPosition).getUid();
            p_a = toPosition;
        }
        DBHelper helper = new DBHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update tb_daily set content = '" + mItems.get(toPosition).getContent() + "', finish = " + tmp_tofinish + " where _id = " + mItems.get(fromPosition).getUid());
        db.execSQL("update tb_daily set content = '" + tmp_content + "', finish = " + tmp_fromfinish + " where _id = " + mItems.get(p_a).getUid());
        p_a = fromPosition;
        db.close();
        */

        notifyItemMoved(fromPosition,toPosition);
        Log.i("변경 로그",fromPosition+ " / " + toPosition + " / ");
        return true;
    }
}

class MItemViewHolder extends RecyclerView.ViewHolder {
    protected TextView mContent;
    protected CheckBox mFinish;
    protected ConstraintLayout mlist_box;

    public MItemViewHolder(final View itemView) {
        super(itemView);
        mContent = (TextView) itemView.findViewById(R.id.item_content);
        mFinish = (CheckBox) itemView.findViewById(R.id.item_finish);
        mlist_box = (ConstraintLayout) itemView.findViewById(R.id.list_box);
    }
}