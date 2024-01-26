package sord.captureu.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import sord.captureu.Dialogs.SetLines;
import sord.captureu.R;

public class Options extends AppCompatActivity implements SetLines.EditDialogListener{
    private TextView options_lines;
    private LinearLayout options_lines_select;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        options_lines=findViewById(R.id.options_lines);
        options_lines_select=findViewById(R.id.options_lines_select);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        if(pref.getBoolean("firstTime",true)){
            editor.putInt("lines",3);
            options_lines.setText("3");
            editor.putBoolean("firstTime",false);
            editor.commit();
        }
        else {
            int n=pref.getInt("lines", 0);
            String num=Integer.toString(n);
            options_lines.setText(num);
        }
        options_lines_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetLines setLines=new SetLines(getApplicationContext(),Integer.parseInt(options_lines.getText().toString()));
                setLines.show(getSupportFragmentManager(),"SelectNumber");
            }
        });
    }



    @Override
    public void updateResult(int num) {
        options_lines.setText(Integer.toString(num));
    }
}
