package com.example.smd_a1;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private boolean isConnected = false;
    private List<Person> personArrayList;
    private ConDbSave conDbSave;
    private String OwnPhoneNum;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Context c;
    private ImageView wifi_status;

    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        //Toast.makeText(context, "" + isConnected, Toast.LENGTH_SHORT).show();
        Offline_Online_Retrieval();
    }

    private void Offline_Online_Retrieval()
    {
        if(isConnected)
        {
            int i = 0;
            for(Person p : personArrayList)
            {
                if(p.getStatus_online() == 0)
                {
                    personArrayList.get(i).setStatus_online(1);
                    p.setStatus_online(1);
                    conDbSave.UpdateConversation(p);
                    String PushKey = databaseReference.push().getKey();
                    if (OwnPhoneNum != null && PushKey != null) {
                        databaseReference.child(OwnPhoneNum).child(PushKey).child("Phone").setValue(p.getPhone());
                    }
                }
                i++;
            }

        }
        wifi_status.setImageBitmap(Return_WifiStatus());

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Bitmap Return_WifiStatus()
    {
        Drawable drawable;
        if(isConnected)
        {
            drawable = c.getResources().getDrawable(R.drawable.ic_baseline_signal_wifi_4_bar_24);
        }
        else
        {
            drawable = c.getResources().getDrawable(R.drawable.ic_baseline_signal_wifi_off_24);
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }



    public void getParas(List<Person> people,ConDbSave conDbSave1,String phoneNum,ImageView status)
    {
        this.personArrayList = people;
        this.conDbSave = conDbSave1;
        this.OwnPhoneNum = phoneNum;
        this.wifi_status = status;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
