package com.example.smd_a1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CredentialsDialog extends AppCompatDialogFragment {

    EditText phoneNo;
    EditText Name;
    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View v = layoutInflater.inflate(R.layout.credentials_dialog,null);

        builder.setView(v)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String p = phoneNo.getText().toString();
                        String n = Name.getText().toString();



                        listener.getPhoneAndName(p,n);
                    }
                });

        phoneNo = v.findViewById(R.id.editTextPhone);
        Name = v.findViewById(R.id.editTextTextPersonName);


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try
        {
            listener = (DialogListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString());
        }

    }

    public interface DialogListener
    {
        public void getPhoneAndName(String phone,String name);
    }


}
