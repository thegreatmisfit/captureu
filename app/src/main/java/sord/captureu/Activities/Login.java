package sord.captureu.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sord.captureu.HelperClasses.EncryDecry;
import sord.captureu.R;

public class Login extends Activity {
    private EditText login_pass_text;
    private Button login_pass_button;
    private TextView login_title_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"lato_light.ttf");

        login_title_text=findViewById(R.id.login_title_text);
        login_title_text.setTypeface(typeface);
        login_pass_text=findViewById(R.id.login_pass_text);
        login_pass_button=findViewById(R.id.login_pass_button);
        login_pass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(login_pass_text.getText().toString().length()>=4){
                        if(new EncryDecry().make_login(login_pass_text.getText().toString(),getApplication())){
                            Intent intent=new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        Toast.makeText(getApplication(),"Incorrect Password Entered", Toast.LENGTH_LONG).show();
                    }
                    else
                    Toast.makeText(getApplication(),"The password has to be atleast 4 characters long!!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
