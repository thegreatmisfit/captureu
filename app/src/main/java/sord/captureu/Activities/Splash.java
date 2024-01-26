package sord.captureu.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import sord.captureu.HelperClasses.DatabaseHelper;
import sord.captureu.R;

public class Splash extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_layout);

        Thread n=new Thread(){ public void run(){
            try{sleep(1500);}catch(Exception e){e.printStackTrace();}
            if(!new DatabaseHelper(getApplication()).if_pass_exists()){
                Intent intent=new Intent(getApplicationContext(),CreatePassword.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }

        }};
        n.start();
    }

}
