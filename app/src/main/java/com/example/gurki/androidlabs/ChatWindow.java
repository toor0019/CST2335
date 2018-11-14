package com.example.gurki.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.ChatDetailDbSchema;
import database.ChatDetaillBaseHelper;

public class ChatWindow extends Activity {

    private ListView listview;
    private EditText chat_Text;
    private Button send_Button;

    private List<ChatDetail> listChatDetails;
    private ChatDetaillBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listview = (ListView) findViewById(R.id.listview);
        chat_Text = (EditText) findViewById(R.id.chat_text);
        send_Button = (Button) findViewById(R.id.send_button);

        dbHelper =  new ChatDetaillBaseHelper(this);

        listChatDetails = dbHelper.getAllMessage();

        ChatAdapter messageAdapter =new ChatAdapter( this );
        listview.setAdapter (messageAdapter);

        send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values=ChatDetailLab.getChatDetailLab(ChatWindow.this)
                        .getContentValues(new ChatDetail(chat_Text.getText().toString()));
                dbHelper.getWritableDatabase().insert(ChatDetailDbSchema.ChatDetailTable.NAME,null,values);
                listChatDetails = dbHelper.getAllMessage();
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount() & getView()
                chat_Text.setText("");
            }
        });



    }

    private class ChatAdapter extends ArrayAdapter<ChatDetail>{

            private ChatAdapter(Context mContext){
                super(mContext,0);
            }

            public int getCount(){
                return listChatDetails.size();
            }

            public ChatDetail getItem(int position){
                return listChatDetails.get(position);
            }

            public View getView(int position, View convertView, ViewGroup parent){

                LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

                View result = null ;

                if(position%2 == 0) {
                    result = inflater.inflate(R.layout.chat_row_incoming, null);
                }
                else {

                    result = inflater.inflate(R.layout.chat_row_outgoing, null);
                }
                TextView message = (TextView)result.findViewById(R.id.message_text);

                message.setText( listChatDetails.get(position).getChat() ); // get the string at position

                return result;

            }

            public long getItemId(int position){
                return position;
            }

    }

    public void onDestroy(){
        super.onDestroy();
        dbHelper.close();
    }


}
