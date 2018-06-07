package com.example.seoil.daily.Adapter;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seoil.daily.R;
import com.example.seoil.daily.model.ListItem;

import java.util.ArrayList;
import java.util.Collections;

public class RecylcerAdapter extends RecyclerView.Adapter<ItemViewHolder>{

    ArrayList<ListItem> mItems;
    Context mContext;

    public RecylcerAdapter(Context context, ArrayList<ListItem> items){
        mContext = context;
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.mContent.setText(position+1 + ". " + mItems.get(position).getContent());
        if(mItems.get(position).getFinish() == 0) {
            holder.mFinish.setChecked(false);
        }
        else {
            holder.mFinish.setChecked(true);
        }
            holder.mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper helper = new DBHelper(mContext);
                SQLiteDatabase db = helper.getWritableDatabase();
                if(holder.mFinish.isChecked()){
                    db.execSQL("update tb_daily set finish = 1 where _id = " + mItems.get(position).getUid());
                }
                else{
                    db.execSQL("update tb_daily set finish = 0 where _id = " + mItems.get(position).getUid());
                }
                db.close();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder{
    protected TextView mContent;
    protected CheckBox mFinish;
    public ItemViewHolder(final View itemView) {
        super(itemView);
        mContent = (TextView) itemView.findViewById(R.id.item_content);
        mFinish = (CheckBox) itemView.findViewById(R.id.item_finish);
    }
}