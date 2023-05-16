package com.example.smd_a1;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyMessageModel extends ViewModel {
    private ArrayList<Message> messages;

    public ArrayList<Message> getMessages()
    {
        if(messages == null)
        {
            messages = new ArrayList<>();
        }
        return messages;
    }
}
