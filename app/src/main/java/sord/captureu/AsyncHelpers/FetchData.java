package sord.captureu.AsyncHelpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.ListView;

import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sord.captureu.HelperClasses.DatabaseHelper;
import sord.captureu.HelperClasses.EncryDecry;
import sord.captureu.R;

public class FetchData extends AsyncTask<String,Entries,String> {
    private Context context;
    private Activity activity;
    private DatabaseHelper databaseHelper;
    private EncryDecry encryDecry;
    private EntryAdapter entryAdapter;
    private Entries entries;
    private ListView listView;
    private ProgressDialog progressDialog;
    private int lines;
    public FetchData(Context context,int lines){
        this.context=context;
        this.lines=lines;
    }

    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please wait");
        progressDialog.setTitle("Processing");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected String doInBackground(String... strings) {
        activity=(Activity)context;
        listView=(ListView)activity.findViewById(R.id.main_list_view);
        if(strings[0]=="getList"){
          databaseHelper=new DatabaseHelper(activity);
          encryDecry=new EncryDecry();
            Cursor cursor=databaseHelper.getList();
            int id;
            entryAdapter=new EntryAdapter(context, R.layout.list_item, lines);
            String cap,title,data;
            JSONObject object;
            while (cursor.moveToNext()){

                try {
                    id=cursor.getInt(0);
                    object=new JSONObject(encryDecry.decrypt_with_master(cursor.getString(2),context));
                    cap=cursor.getString(1);
                    title=object.getString("title");
                    data=object.getString("data");
                    entries=new Entries(id,cap,title,data);
                    publishProgress(entries);

                } catch (Exception e) {
                }
            }
            return "entered";
        }
        return null;
    }



    @Override
    protected void onPostExecute(String result) {
        if(result.equals("entered")) {
            listView.setAdapter(entryAdapter);
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(Entries... entry) {
        entryAdapter.add(entry[0]);
    }
}
