package com.example.smd_a1;

import java.util.ArrayList;

public interface FirebaseDAO {

    public void saveConversation(Person p);
    public void saveMessage(Message m);

    public ArrayList<Person> getContacts();
    public ArrayList<Message> getMessages();
}
