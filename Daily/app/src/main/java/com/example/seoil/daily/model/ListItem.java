package com.example.seoil.daily.model;


public class ListItem {
    private int _id;
    private String content;
    private int finish;

    public ListItem(int _id, String content, int finish){
        this._id = _id;
        this.content = content ;
        this.finish = finish;
    }

    public int getUid(){return _id;}
    public void setUid(int _id){this._id = _id;}
    public String getContent(){
        return content;
    }
    public void setContent(String content){this.content = content;}
    public int getFinish(){ return finish; }
    public void setFinish(int finish){this.finish = finish;}
}
