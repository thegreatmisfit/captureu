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

import sord.captureu.AsyncHelpers.FetchData;
import sord.captureu.HelperClasses.DatabaseHelper;
import sord.captureu.R;

@SuppressLint("ValidFragment")
public class EditEntry extends android.support.v4.app.DialogFragment {
    @Nullable
    private Button edit_entry_dialog_edit,edit_entry_dialog_cancel;
    private EditText edit_entry_dialog_title,edit_entry_dialog_data;
    private String title, data;
    private int id,lines;
    private Typeface typeface;
    private TextView edit_entry_dialog_head;

    @SuppressLint("ValidFragment")
    public EditEntry(String title, String data, int id, int lines){
        this.title=title;
        this.data=data;
        this.id=id;
        this.lines=lines;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.edit_entry_layout,null);
        typeface= Typeface.createFromAsset(getContext().getAssets(),"lato_regular.ttf");
        edit_entry_dialog_head=view.findViewById(R.id.edit_entry_dialog_head);
        edit_entry_dialog_head.setTypeface(typeface);
        edit_entry_dialog_title=(EditText)view.findViewById(R.id.edit_entry_dialog_title);
        edit_entry_dialog_title.setTypeface(typeface);
        edit_entry_dialog_data=(EditText)view.findViewById(R.id.edit_entry_dialog_data);
        edit_entry_dialog_data.setTypeface(typeface);
        edit_entry_dialog_edit=(Button)view.findViewById(R.id.edit_entry_dialog_edit);
        edit_entry_dialog_cancel=(Button)view.findViewById(R.id.edit_entry_dialog_cancel);
        edit_entry_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        edit_entry_dialog_title.setText(title);
        edit_entry_dialog_data.setText(data);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        edit_entry_dialog_edit.setOnClickListener(new View.OnClickListener() {

            JSONObject entry= new JSONObject();
            @Override
            public void onClick(View view) {
                String cap=edit_entry_dialog_title.getText().toString().substring(0,1).toUpperCase();

                try {
                    entry.put("cap",cap);
                    entry.put("title",edit_entry_dialog_title.getText().toString());
                    entry.put("data",edit_entry_dialog_data.getText().toString());

                    new DatabaseHelper(getActivity()).update_data_entry(entry.toString(),cap,id,getActivity().getApplication());
                    Toast.makeText(getActivity(), "Entry Edited Successfully!!", Toast.LENGTH_SHORT).show();
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
