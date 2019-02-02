package com.example.gurki.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

public class MessageDetails extends Activity {


    private static final String MESSAGE="message";
    private static final String MESSAGE_ID="id";
    private Fragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_details);
        String message = (String)getIntent().getSerializableExtra(MESSAGE);
        int ID = (int)getIntent().getSerializableExtra(MESSAGE_ID);
        Bundle b = new Bundle();
        b.putString("message",message);
        b.putInt("id",ID);


        FragmentManager fm = getFragmentManager();
         mFragment = fm.findFragmentById(R.id.framelayout_message);
        if (mFragment == null) {
            mFragment = new MessageFragment();
            mFragment.setArguments(b);
            fm.beginTransaction()
                    .add(R.id.framelayout_message, mFragment)
                    .commit();
        }

    }

    public static Intent newIntent(Context mContext,String message,int ID){
        Intent intent = new Intent(mContext,MessageDetails.class);
        intent.putExtra(MESSAGE,message);
        intent.putExtra(MESSAGE_ID,ID);
        return intent;
    }



}
