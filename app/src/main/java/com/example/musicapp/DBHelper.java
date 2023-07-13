package com.example.musicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    Context c;
    public  static final String DBName ="Login.db";
    public static final int DBVersion =1;
    public DBHelper(Context c) {
    super(c,DBName,null, DBVersion);
        this.c=c;

    }



    @Override
    public void onCreate(SQLiteDatabase mydb) {

              mydb.execSQL("create table users (username text primary key,password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase mydb, int i, int i1) {
             mydb.execSQL("drop table if exists users");

    }
    public Boolean insertData (String username ,String password)
    {
        SQLiteDatabase mydb =this.getWritableDatabase();
        ContentValues contentvalue =new ContentValues();
        contentvalue.put("username",username);
        contentvalue.put("password",password);
        long reslult= mydb.insert("users",null,contentvalue);
        if(reslult == -1)
        {
            return false;

        }else{
            return true;
        }



    }
    public Boolean checkusers(String username)
    {
        SQLiteDatabase mydb =this.getWritableDatabase();
        Cursor cursor =mydb.rawQuery("select * from users where username =?" ,new String[] {username});
                if (cursor.getCount()>0){
                    return true;
                }
                    else{
                        return false;
                    }
    }
    public Boolean checkuserspassword(String username,String password)
    {
        SQLiteDatabase mydb =this.getWritableDatabase();
        Cursor cursor =mydb.rawQuery("select * from users where username =? and password=?" ,new String[] {username,password});
        if (cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }
}
