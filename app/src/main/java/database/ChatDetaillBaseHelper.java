package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gurki.androidlabs.ChatDetail;

import java.util.ArrayList;
import java.util.List;

import database.ChatDetailDbSchema.ChatDetailTable;

import static database.ChatDetailDbSchema.ChatDetailTable.Cols.CHAT;
import static database.ChatDetailDbSchema.ChatDetailTable.Cols.UUID;

/**
 * Created by gurki on 2018-10-12.
 */

public class ChatDetaillBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DataBase_NAME = "cmessage.db";
    protected static final String ACTIVITY_NAME = "ChatDetailBaseHelper";

    public ChatDetaillBaseHelper(Context mContext) {
        super(mContext, DataBase_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        db.execSQL("create table " + ChatDetailTable.NAME + "(" +
                " id_ integer primary key autoincrement, " +
                ChatDetailTable.Cols.UUID + ", " +
                ChatDetailTable.Cols.CHAT +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL("Drop Table if Exists " + ChatDetailTable.NAME);
        onCreate(db);
    }

    public void insertMessage(String message) {

    }

    public List<ChatDetail> getAllMessage() {
        final List<ChatDetail> chatDetails = new ArrayList<>();
        final SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(ChatDetailTable.NAME, null, null, null, null, null, null);

        if (cursor != null) {
            Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount() );
            for(int i=0;i<cursor.getColumnCount();i++){
                Log.i(ACTIVITY_NAME, "Cursor’s  column Name =" + cursor.getColumnName(i) );
            }
            while (cursor.moveToNext()) {
                String uuid = cursor.getString(cursor.getColumnIndex(UUID));
                String chat = cursor.getString(cursor.getColumnIndex(CHAT));
                ChatDetail chatDetail = new ChatDetail(chat, java.util.UUID.fromString(uuid));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + chat );


                chatDetails.add(chatDetail);
            }
        }

        return chatDetails;
    }
}


