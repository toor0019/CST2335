package com.example.gurki.androidlabs;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.ChatDetailCursorWrapper;
import database.ChatDetailDbSchema;
import database.ChatDetaillBaseHelper;

import static database.ChatDetailDbSchema.ChatDetailTable.*;

/**
 * Created by gurki on 2018-10-12.
 */

public class ChatDetailLab {

    private static ChatDetailLab sChatDetailLab;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;
    protected static final String ACTIVITY_NAME = "LoginActivity";
    private ChatDetailLab(Context mContext){

      this.mContext = mContext.getApplicationContext();
      mSQLiteDatabase = new ChatDetaillBaseHelper(this.mContext).getWritableDatabase();

    }

    public static ChatDetailLab getChatDetailLab(Context mContext){
        if(sChatDetailLab == null){
            sChatDetailLab = new ChatDetailLab(mContext);
        }
        return sChatDetailLab;
    }

    public List<ChatDetail> getChatDetails() {

        List<ChatDetail> list= new ArrayList<ChatDetail>();
        ChatDetailCursorWrapper chatDetailCursorWrapper = queryChatDetail(null,null);
        try{
            chatDetailCursorWrapper.moveToFirst();
            while (!chatDetailCursorWrapper.isAfterLast()){
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + chatDetailCursorWrapper.getString( chatDetailCursorWrapper.getColumnIndex( Cols.CHAT) ) );
                Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + chatDetailCursorWrapper.getColumnCount() );
                list.add(chatDetailCursorWrapper.getChatDetail());
                chatDetailCursorWrapper.moveToNext();
            }
        }finally {
            chatDetailCursorWrapper.close();
        }
        return list;
    }

    public ChatDetail getChatDetail(UUID uuid){
            ChatDetailCursorWrapper chatDetailCursorWrapper = queryChatDetail(Cols.UUID,new String[]{uuid.toString()});
        try {
            if (chatDetailCursorWrapper.getCount() == 0) {
                return null;
            }
            chatDetailCursorWrapper.moveToFirst();
            return chatDetailCursorWrapper.getChatDetail();
        } finally {
            chatDetailCursorWrapper.close();
        }
    }

    public void addChatDetail(ChatDetail chatDetail){
            ContentValues values = getContentValues(chatDetail);
            mSQLiteDatabase.insert(ChatDetailDbSchema.ChatDetailTable.NAME,null,values);
    }

    public static ContentValues getContentValues(ChatDetail chatDetail){
        ContentValues values = new ContentValues();
        values.put(Cols.UUID,chatDetail.getID().toString());
        values.put(Cols.CHAT,chatDetail.getChat());

        return values;
    }

    public void upDateChatDetail(ChatDetail chatDetail){
        String uuid = chatDetail.getID().toString();
        ContentValues values= getContentValues(chatDetail);
        mSQLiteDatabase.update(ChatDetailDbSchema.ChatDetailTable.NAME,values,
                Cols.UUID+" = ?",
                new String[]{uuid});
    }

    private ChatDetailCursorWrapper queryChatDetail(String whereClause, String[] whereArgs){

        Cursor cursor = mSQLiteDatabase.query(
                ChatDetailDbSchema.ChatDetailTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ChatDetailCursorWrapper(cursor);
    }
}
