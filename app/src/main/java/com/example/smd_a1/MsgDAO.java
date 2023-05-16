package com.example.smd_a1;

import java.util.List;

public interface MsgDAO {

    public void save(Message m,String Person_Id);
    public void save(List<Message> messageList,String Person_Id);
    public List<Message> get(String Person_Id);

}
