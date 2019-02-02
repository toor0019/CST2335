package com.example.gurki.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private Boolean isTablet;
    private Fragment mFragment;
    private List<ChatDetail> listChatDetails;
    private ChatDetaillBaseHelper dbHelper;
    private static final int MESSAGE_DETAILS_REQUEST_CODE=50;
    ChatAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listview = (ListView) findViewById(R.id.listview);

        chat_Text = (EditText) findViewById(R.id.chat_text);
        send_Button = (Button) findViewById(R.id.send_button);
        isTablet = findViewById(R.id.framelayout) != null;
        dbHelper =  new ChatDetaillBaseHelper(this);

        listChatDetails = dbHelper.getAllMessage();

         messageAdapter =new ChatAdapter( this );
        listview.setAdapter (messageAdapter);

        send_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values=ChatDetailLab.getChatDetailLab(ChatWindow.this)
                        .getContentValues(new ChatDetail(chat_Text.getText().toString()));
                dbHelper.getWritableDatabase().insert(ChatDetailDbSchema.ChatDetailTable.NAME,null,values);
               upDateUI();
            }
        });

        listview.setOnItemClickListener(((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isTablet){
                    Bundle b = new Bundle();
                    String temp=listChatDetails.get(position).getChat();
                    b.putString("message",listChatDetails.get(position).getChat());
                    b.putInt("id",position+1);


                    FragmentManager fm = getFragmentManager();
                  mFragment = fm.findFragmentById(R.id.framelayout_message);

                    if (mFragment == null) {
                        mFragment = new MessageFragment();
                        mFragment.setArguments(b);
                        fm.beginTransaction()
                                .add(R.id.framelayout, mFragment)
                                .commit();
                    }
                }else{
                    Intent intent = MessageDetails.newIntent(ChatWindow.this,listChatDetails.get(position).getChat(),position+1);
                    startActivityForResult(intent,MESSAGE_DETAILS_REQUEST_CODE);
                }
            }
        })));

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

                return dbHelper.getID(position);
            }

    }

    public void onDestroy(){
        super.onDestroy();
        dbHelper.close();
    }
    public void deleteMessage(long id){
        dbHelper.deleteMessage(id);
        upDateUI();
    }

    public void upDateUI(){
        listChatDetails = dbHelper.getAllMessage();
        messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount() & getView()
        chat_Text.setText("");
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MESSAGE_DETAILS_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                long id = data.getIntExtra("id",-1);
                Log.i("ChatWindow",""+id);
                deleteMessage(id);
            }
        }
    }
}
