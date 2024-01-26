package sord.captureu.AsyncHelpers;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sord.captureu.Dialogs.ViewEntry;
import sord.captureu.HelperClasses.EncryDecry;
import sord.captureu.R;

public class EntryAdapter extends ArrayAdapter{

    private List list=new ArrayList();
    private Context context;
    private ViewEntry viewEntry;
    private int lines;

    public EntryAdapter(@NonNull Context context, int resource, int lines) {
        super(context, resource);
        this.lines=lines;
    }
    public void add(Entries entry){
        list.add(entry);
    }
    public int getCount(){
        return list.size();
    }

    public Object getItem(int position){
        return list.get(position);
    }

    static class EntryHolder{
        TextView list_item_alpha,list_item_title,list_item_data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        EntryHolder entryHolder;
        LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row=layoutInflater.inflate(R.layout.list_item,parent,false);
        entryHolder=new EntryHolder();
        entryHolder.list_item_alpha=(TextView)row.findViewById(R.id.list_item_alpha);
        entryHolder.list_item_data=(TextView)row.findViewById(R.id.list_item_data);
        entryHolder.list_item_title=(TextView)row.findViewById(R.id.list_item_title);
        row.setTag(entryHolder);

        final Entries entries=(Entries)getItem(position);

        Typeface typefacea=Typeface.createFromAsset(getContext().getAssets(),"lato_light.ttf");
        Typeface typefaceb=Typeface.createFromAsset(getContext().getAssets(),"lato_regular.ttf");

        entryHolder.list_item_data.setLines(lines);
        entryHolder.list_item_title.setText(entries.getTitle());
        entryHolder.list_item_title.setTypeface(typefaceb);
        entryHolder.list_item_data.setText(entries.getData());
        entryHolder.list_item_data.setTypeface(typefaceb);
        entryHolder.list_item_alpha.setText(entries.getCap());
        entryHolder.list_item_alpha.setTypeface(typefacea);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEntry=new ViewEntry(entries.getTitle(),entries.getData(),entries.getId(),lines);
                context=getContext();
                FragmentManager manager= ((AppCompatActivity)context).getSupportFragmentManager();
                viewEntry.show(manager,"view Entry");
            }
        });
        return row;
    }
}
