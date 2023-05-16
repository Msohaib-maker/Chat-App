package com.example.smd_a1;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.My_Msg_Holder> {

    private ArrayList<Message> messagesList;
    private Context context;
    private String SenderPhone;
    private String ReceiverPhone;


    public MessageAdapter(ArrayList<Message> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    public MessageAdapter(ArrayList<Message> messagesList, Context context,String senderPhone,String RecPhone) {
        this.messagesList = messagesList;
        this.context = context;
        this.SenderPhone = senderPhone;
        this.ReceiverPhone = RecPhone;
    }

    public ArrayList<Message> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(ArrayList<Message> messagesList) {
        this.messagesList = messagesList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if(messagesList.get(position).getSenderNum().equals(this.SenderPhone))
        {
            return 0;
        }

        return 1;
    }

    @NonNull
    @Override
    public My_Msg_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reciever_message_holder,null);
        View v1 = LayoutInflater.from(context).inflate(R.layout.sender_message_holder,null);

        if(viewType == 0)
        {
            return new My_Msg_Holder(v1);
        }

        return new My_Msg_Holder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull My_Msg_Holder holder, int position) {
        holder.Username.setText(messagesList.get(position).getName());
        holder.msg.setText(messagesList.get(position).getContent());
        holder.date.setText(messagesList.get(position).getTime().toString());

    }


    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    class My_Msg_Holder extends RecyclerView.ViewHolder
    {

        TextView Username;
        TextView msg;
        TextView date;

        public My_Msg_Holder(@NonNull View itemView) {
            super(itemView);
            Username = itemView.findViewById(R.id.text_id);
            msg = itemView.findViewById(R.id.text_id2);
            date = itemView.findViewById(R.id.date_id);
        }
    }
}
