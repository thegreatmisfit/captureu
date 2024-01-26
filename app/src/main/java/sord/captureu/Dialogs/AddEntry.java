package sord.captureu.Dialogs;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sord.captureu.AsyncHelpers.FetchData;
import sord.captureu.HelperClasses.DatabaseHelper;
import sord.captureu.R;

@SuppressLint("ValidFragment")
public class AddEntry extends DialogFragment {
    private Button add_entry_dialog_add,add_entry_dialog_cancel;
    private EditText add_entry_dialog_title,add_entry_dialog_data;
    private int lines;
    private TextView head_add_dialog;
    @SuppressLint("ValidFragment")
    public AddEntry(int lines){
        this.lines=lines;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_entry_dialog,null);
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"lato_regular.ttf");
        add_entry_dialog_title=view.findViewById(R.id.add_entry_dialog_title);
        add_entry_dialog_title.setTypeface(typeface);
        add_entry_dialog_data=view.findViewById(R.id.add_entry_dialog_data);
        add_entry_dialog_data.setTypeface(typeface);

        head_add_dialog=view.findViewById(R.id.head_add_dialog);
        head_add_dialog.setTypeface(typeface);
        add_entry_dialog_add=view.findViewById(R.id.add_entry_dialog_add);
        add_entry_dialog_cancel=view.findViewById(R.id.add_entry_dialog_cancel);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        add_entry_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        add_entry_dialog_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title= add_entry_dialog_title.getText().toString();

                String data=add_entry_dialog_data.getText().toString();
                JSONObject entry= new JSONObject();
                try {
                    String cap=add_entry_dialog_title.getText().toString().substring(0,1).toUpperCase();
                    entry.put("cap",cap);
                    entry.put("title",title);
                    entry.put("data",data);
                    new DatabaseHelper(getActivity()).insert_data_entry(entry.toString(),cap, getActivity().getApplication());
                    Toast.makeText(getActivity(),"Entry Added Successfully!!", Toast.LENGTH_LONG).show();
                    new FetchData(getContext(),lines).execute("getList");
                    dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}
