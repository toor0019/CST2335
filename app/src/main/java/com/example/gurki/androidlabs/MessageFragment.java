package com.example.gurki.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by gurki on 2018-11-14.
 */

public class MessageFragment extends Fragment {
    private boolean isTablet;
    private TextView message_TextView;
    private TextView messsage_ID_TextView;
    private Button mButton;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_chatwindow,container,false);
       isTablet = getActivity().findViewById(R.id.framelayout) != null;
       message_TextView = (TextView)v.findViewById(R.id.message_text);
       messsage_ID_TextView =(TextView)v.findViewById(R.id.message_ID_text);

        Bundle b= getArguments();
        String message= b.getString("message");
        long id = b.getInt("id");
        mButton = (Button)v.findViewById(R.id.delete_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage(id);
            }
        });
        message_TextView.setText(message);
       messsage_ID_TextView.setText(""+id);
       return v;
    }

    public void deleteMessage(long id){

        if(isTablet){
            ((ChatWindow)getActivity()).deleteMessage(id);
            ((ChatWindow)getActivity()).getFragmentManager().popBackStack();
        }else{
            Intent intent = new Intent();
            intent.putExtra("id",id);
            ((MessageDetails)getActivity()).setResult(Activity.RESULT_OK,intent);
            getActivity().finish();
        }

        }
}
