package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.gurki.androidlabs.ChatDetail;

import static database.ChatDetailDbSchema.ChatDetailTable.Cols.*;

/**
 * Created by gurki on 2018-10-12.
 */

public class ChatDetailCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ChatDetailCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ChatDetail getChatDetail(){

        String uuid = getString(getColumnIndex(UUID));
        String chat = getString(getColumnIndex(CHAT));

        ChatDetail chatDetail=new ChatDetail(CHAT, java.util.UUID.fromString(uuid));
        return chatDetail;
    }
}
