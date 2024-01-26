package sord.captureu.Dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.zip.CheckedOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sord.captureu.HelperClasses.EncryDecry;
import sord.captureu.R;

@SuppressLint("ValidFragment")
public class AddPassword extends DialogFragment {
    @Nullable
    private EditText add_key_text,add_key_text2;
    private TextView head_add_pass;
    private Button add_key_cancel,add_key_add;
    private int passNo;
    private Context ctx;
    private Typeface typeface;
    @SuppressLint("ValidFragment")
    public AddPassword(int passNo, Context ctx){
        this.passNo=passNo;
        this.ctx=ctx;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_password_dialog,null);
        typeface= Typeface.createFromAsset(getContext().getAssets(),"lato_regular.ttf");
        head_add_pass=view.findViewById(R.id.head_add_pass);
        head_add_pass.setTypeface(typeface);
        add_key_text=view.findViewById(R.id.add_key_text);
        add_key_text.setTypeface(typeface);
        add_key_text2=view.findViewById(R.id.add_key_text2);
        add_key_text2.setTypeface(typeface);
        add_key_cancel=view.findViewById(R.id.add_key_cancel);
        add_key_add=view.findViewById(R.id.add_key_add);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        add_key_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        add_key_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    try {
                        new EncryDecry().addExtraPass(add_key_text.getText().toString(),passNo,ctx);
                        Toast.makeText(getActivity(), "Passphrase Added!!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }
    private boolean validate(){
        boolean flag=false;
        if(!add_key_text.getText().toString().equals(add_key_text2.getText().toString())){
            Toast.makeText(getActivity(), "Passphrase and retype Passphrase entries don't match!!", Toast.LENGTH_SHORT).show();
            flag=false;
        }
        else if(add_key_text.getText().toString().length()<4){
            Toast.makeText(getActivity(), "Passphrase has to be atleast 4 characters long!!", Toast.LENGTH_SHORT).show();
            flag=false;
        }
        else flag=true;

        return flag;
    }
}
