package com.example.smd_a1;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class saveFirebaseDAO implements FirebaseDAO{

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String ChatRoomId;
    private String ownPhone;

    public saveFirebaseDAO()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void SetPhoneNo(String phone)
    {
        this.ownPhone = phone;
    }

    public void getChatRoomId(String chatRoomId)
    {
        this.ChatRoomId = chatRoomId;
    }

    @Override
    public void saveConversation(Person p) {
        databaseReference = firebaseDatabase.getReference("Users");

        String key = databaseReference.push().getKey();
        assert key != null;
        databaseReference.child(ownPhone)
                .child(key).child("Phone").setValue(p.getPhone());
    }

    @Override
    public void saveMessage(Message m) {
        databaseReference = firebaseDatabase.getReference("ChatRoom");
        String key = databaseReference.push().getKey();

        assert key != null;
        databaseReference.child(ChatRoomId)
                .child(key).setValue(m);
    }

    @Override
    public ArrayList<Person> getContacts() {
        return null;
    }

    @Override
    public ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("ChatRoom");

        databaseReference.child(ChatRoomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Message m = dataSnapshot.getValue(Message.class);
                            messages.add(m);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return messages;
    }
}
