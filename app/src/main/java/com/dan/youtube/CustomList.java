package com.dan.youtube;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<Video> videos;
    private boolean checked = true;
    public CustomList(Activity context, ArrayList<Video> videos, String[] names) {
        super(context, R.layout.list_single, names);
        this.context = context;
        this.videos = videos;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single, null, true);
        final CheckBox checkBox = ((CheckBox)rowView.findViewById(R.id.listBox));
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        imageView.setImageBitmap(videos.get(position).getImage());
        txtTitle.setText(videos.get(position).getName());
        checkBox.setChecked(checked);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =((TextView)((View)v.getParent()).findViewById(R.id.txt)).getText().toString();
                for(int i=0 ;i<videos.size();i++)
                {
                    if(videos.get(i).getName().equals(text))
                    {
                        boolean adgf= ((CheckBox)v).isChecked();
                        videos.get(i).setChecked(((CheckBox)v).isChecked());
                        return;
                    }
                }
            }
        });
        return rowView;
    }
}