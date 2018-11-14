package com.example.gurki.androidlabs;

import java.util.Date;
import java.util.UUID;

/**
 * Created by gurki on 2018-10-10.
 */

public class ChatDetail {

    private String mChat;
    private UUID mID;


    public String getChat() {
        return mChat;
    }

    public void setChat(String chat) {
        mChat = chat;
    }

    public UUID getID() {
        return mID;
    }

    public void setID(UUID ID) {
        mID = ID;
    }


    public ChatDetail(String chat){
        setChat(chat);

        setID(UUID.randomUUID());
    }

    public ChatDetail(String chat,UUID uuid){
        setChat(chat);
        setID(uuid);
    }

}
