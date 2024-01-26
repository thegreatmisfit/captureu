package sord.captureu.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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

import sord.captureu.AsyncHelpers.FetchData;
import sord.captureu.HelperClasses.DatabaseHelper;
import sord.captureu.R;

@SuppressLint("ValidFragment")
public class ViewEntry extends android.support.v4.app.DialogFragment{
    @Nullable
    private TextView view_entry_dialog_title,view_entry_dialog_data;
    private Button view_entry_dialog_delete_button,view_entry_dialog_edit_button;
    private String title, data;
    private int id,lines;
    @SuppressLint("ValidFragment")
    public ViewEntry(String title, String data, int id,int lines){
        this.title=title;
        this.data=data;
        this.id=id;
        this.lines=lines;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_entry_dialog,null);
        view_entry_dialog_title=(TextView)view.findViewById(R.id.view_entry_dialog_title);
        view_entry_dialog_data=(TextView)view.findViewById(R.id.view_entry_dialog_data);
        view_entry_dialog_delete_button=(Button)view.findViewById(R.id.view_entry_dialog_delete_button);
        view_entry_dialog_edit_button=(Button)view.findViewById(R.id.view_entry_dialog_edit_button);
        view_entry_dialog_title.setText(title);
        view_entry_dialog_data.setText(data);
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"lato_regular.ttf");
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        view_entry_dialog_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new DatabaseHelper(getContext()).deleteEntry(id);
                        new FetchData(getContext(),lines).execute("getList");
                        dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
                builder.setTitle("Delete Secure Entry");
                builder.setMessage("Are you sure you want to delete this Secure Entry??");
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });
        view_entry_dialog_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                new EditEntry(title,data,id,lines).show(fragmentManager,"Edit Fragment");
                dismiss();
            }
        });

        return view;
    }
}
