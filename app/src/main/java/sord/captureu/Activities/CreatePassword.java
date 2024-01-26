package sord.captureu.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sord.captureu.HelperClasses.EncryDecry;
import sord.captureu.R;

public class CreatePassword extends Activity {
    private EditText cr_pass_1,cr_pass_11,cr_pass_2,cr_pass_22,cr_pass_3,cr_pass_33;
    private Button cr_pass_confirm;
    private EncryDecry encryDecry;
    private AlertDialog.Builder builder;
    private Typeface typeface;
    private TextView cr_pass_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.create_password_layout);
        typeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"lato_regular.ttf");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        cr_pass_head=findViewById(R.id.cr_pass_head);
        cr_pass_head.setTypeface(typeface);
        cr_pass_1=findViewById(R.id.cr_pass_1);
        cr_pass_1.setTypeface(typeface);
        cr_pass_11=findViewById(R.id.cr_pass_11);
        cr_pass_11.setTypeface(typeface);
        cr_pass_2=findViewById(R.id.cr_pass_2);
        cr_pass_2.setTypeface(typeface);
        cr_pass_22=findViewById(R.id.cr_pass_22);
        cr_pass_22.setTypeface(typeface);
        cr_pass_3=findViewById(R.id.cr_pass_3);
        cr_pass_3.setTypeface(typeface);
        cr_pass_33=findViewById(R.id.cr_pass_33);
        cr_pass_33.setTypeface(typeface);
        cr_pass_confirm=findViewById(R.id.cr_pass_confirm);
        encryDecry=new EncryDecry();
        builder=new AlertDialog.Builder(this);
        cr_pass_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(validateInputs()) {
                        encryDecry.createHash(cr_pass_1.getText().toString().toCharArray());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public boolean validateInputs() throws NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, JSONException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        if(cr_pass_1.getText().toString().equals("")&&cr_pass_11.getText().toString().equals("")&&
                cr_pass_2.getText().toString().equals("")&&cr_pass_22.getText().toString().equals("")&&
                cr_pass_3.getText().toString().equals("")&&cr_pass_33.getText().toString().equals("")) {
                builder.setTitle("Passphrase!!!");
                builder.setMessage("You need to fill atleast one passphrase!!");
                final AlertDialog dialog = builder.create();
                dialog.show();
                return false; }

                if(!cr_pass_1.getText().toString().equals(cr_pass_11.getText().toString())){
                    builder.setTitle("Passphrase-1!!!");
                    builder.setMessage("Passphrase-1 and Retype Passphrase-1 doesn't match!!");
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
                if(!cr_pass_2.getText().toString().equals(cr_pass_22.getText().toString())){
                    builder.setTitle("Passphrase-2!!!");
                    builder.setMessage("Passphrase-2 and Retype Passphrase-2 doesn't match!!");
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
                if(!cr_pass_3.getText().toString().equals(cr_pass_33.getText().toString())){
                    builder.setTitle("Passphrase-3!!!");
                    builder.setMessage("Passphrase-3 and Retype Passphrase-3 doesn't match!!");
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }


                if(cr_pass_1.getText().toString().length()<4&&!cr_pass_1.getText().toString().equals("")){
                    builder.setTitle("Passphrase-1!!!");
                    builder.setMessage("Passphrase has to have atleast 8 characters!!");
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
                if(cr_pass_2.getText().toString().length()<4&&!cr_pass_2.getText().toString().equals("")){
                    builder.setTitle("Passphrase-2!!!");
                    builder.setMessage("Passphrase has to have atleast 8 characters!!");
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
                if(cr_pass_3.getText().toString().length()<4&&!cr_pass_3.getText().toString().equals("")){
                    builder.setTitle("Passphrase-3!!!");
                    builder.setMessage("Passphrase has to have atleast 8 characters!!");
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }

            else {
            String args="";
                if(cr_pass_1.getText().toString().equals("")){
                    args=args+"0";
                }
                else{
                    args=args+"1";
                }
                    if(cr_pass_2.getText().toString().equals("")){
                        args=args+"0";
                    }
                    else{
                        args=args+"1";
                    }
                    if(cr_pass_3.getText().toString().equals("")){
                        args=args+"0";
                    }
                    else{
                        args=args+"1";
                    }
                encryDecry.doTheThing(cr_pass_1.getText().toString(),cr_pass_2.getText().toString(),cr_pass_3.getText().toString(),args,getApplication());
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
                }
    }
}
