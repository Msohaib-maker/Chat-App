package com.example.smd_a1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Conversation_Adapter extends RecyclerView.Adapter<Conversation_Adapter.The_Holder> {


    private List<Person> personList;
    private Context context;
    private AdapterClickListener clickListener;

    public Conversation_Adapter(List<Person> personList, Context context,AdapterClickListener clickListener) {
        this.personList = personList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public The_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_holder,null);
        return new The_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull The_Holder holder, int position) {
        holder.Name.setText(personList.get(position).getName());
        holder.Phone.setText(personList.get(position).getPhone());
        //holder.picture.setImageBitmap(personList.get(position).getPhoto1());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class The_Holder extends RecyclerView.ViewHolder
    {
        ImageView picture;
        TextView Name;
        TextView Phone;
        public The_Holder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.person_pic_id);
            Name = itemView.findViewById(R.id.person_name_id);
            Phone = itemView.findViewById(R.id.receive_phone_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   clickListener.Click(getAdapterPosition());
                }
            });
        }
    }
}
