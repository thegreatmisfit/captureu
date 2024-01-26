package sord.captureu.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;

import sord.captureu.AsyncHelpers.FetchData;
import sord.captureu.Dialogs.AddEntry;
import sord.captureu.Dialogs.SetLines;
import sord.captureu.R;

public class MainActivity extends AppCompatActivity{

    private Button main_add_entry_button;
    private ListView main_list_view;
    private AddEntry addEntry;
    private Calendar pauseInst, resumeInst;
    private long pauseTime, resumeTime;
    private boolean onHO=false;
    private int lines;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.main_activity_layout);
        Toolbar toolbar =(Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);



        main_list_view=(ListView)findViewById(R.id.main_list_view);
        main_list_view=(ListView)findViewById(R.id.main_list_view);
        main_add_entry_button=(Button)findViewById(R.id.main_add_entry_button);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        if(pref.getBoolean("firstTime",true)){
            editor.putInt("lines",3);
            lines=3;
            editor.putBoolean("firstTime",false);
            editor.commit();
        }
        else {
            int num=pref.getInt("lines",0);
            lines=num;
        }

        new FetchData(this,lines).execute("getList");
        main_add_entry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEntry=new AddEntry(lines);
                addEntry.show(getFragmentManager(),"add_event");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseInst=Calendar.getInstance();
        pauseTime=pauseInst.getTimeInMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        main_list_view.setAdapter(null);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        if(pref.getBoolean("firstTime",true)){
            editor.putInt("lines",3);
            lines=3;
            editor.putBoolean("firstTime",false);
            editor.commit();
        }
        else {
            int num=pref.getInt("lines",0);
            lines=num;
        }

        new FetchData(this,lines).execute("getList");
        resumeInst=Calendar.getInstance();
        resumeTime=resumeInst.getTimeInMillis();
        if(onHO) {
            if (resumeTime - pauseTime > 60000) {
                Intent intent=new Intent(this,Login.class);
                startActivity(intent);
                ((GG) getApplication()).setKey("nope");
                ((GG) getApplication()).setPass(0);
                finish();
            }
        }
        onHO=true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_settings:
                Intent intent=new Intent(this,Settings.class);
                startActivity(intent);

                return true;
            case R.id.menu_options:
                Intent intent1=new Intent(this,Options.class);
                startActivity(intent1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
