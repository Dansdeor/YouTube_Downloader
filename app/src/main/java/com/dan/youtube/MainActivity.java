package com.dan.youtube;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String Path;
    String KEY = "";//You need to add your google api key
    String playlistURL = "https://www.googleapis.com/youtube/v3/playlistItems?maxResults=50&part=snippet&key="+ KEY;
    String VideoURL = "https://www.googleapis.com/youtube/v3/videos?part=snippet&key=" + KEY;
    ArrayList<Video> videos;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = ((ListView) findViewById(R.id.listView));
        videos = new ArrayList();
        Path = Environment.getExternalStorageDirectory().getPath()+"/"+Environment.DIRECTORY_MUSIC;
    }

    public void onClickLoad(View view) {
        if (view.getId() == R.id.load) {
            int index;
            ((CheckBox) findViewById(R.id.checkBox)).setChecked(true);
            String id = ((EditText)findViewById(R.id.url)).getText().toString();
            if ((index = id.lastIndexOf("&list=")) != -1) {
                PlayListView(playlistURL + "&playlistId=" + id.substring(index + "&list=".length()));
            } else if ((index = id.lastIndexOf("?v=")) != -1) {
                PlayListView(VideoURL + "&id=" + id.substring(index + "?v=".length()));
            } else if ((index = id.lastIndexOf("/")) != -1) {
                    PlayListView(VideoURL + "&id=" + id.substring(index + 1));
            } else
                ((CheckBox) findViewById(R.id.checkBox)).setChecked(false);
        }
    }

    public void onClickCheckBox(View view) {
        Boolean checked = ((CheckBox) view).isChecked();
        ((CustomList) listView.getAdapter()).setChecked(checked);
        for (int i = 0; i < videos.size(); i++) {
            View item = listView.getChildAt(i);
            if (item != null)
                ((CheckBox) item.findViewById(R.id.listBox)).setChecked(checked);
            videos.get(i).setChecked(checked);
        }
    }

    public void onClickDownload(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Download started", Toast.LENGTH_SHORT).show();
            try {
                for (int i = 0; i < videos.size(); i++) {
                    Video video = videos.get(i);
                    if (video.isChecked()) {
                        Map song = new GetJson().execute("http://www.youtubeinmp3.com/fetch/?format=JSON&video=http://www.youtube.com/watch?v=" + video.getID()).get();
                        if (song != null) {
                            URL url = new URL((String) song.get("link"));
                            Thread t = new Thread(new Downloader(url, Path + "/" + video.getName() + ".mp3"));
                            t.start();
                            t.join();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getApplicationContext(), "Download done", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void onClickSettings(View view) {
        if (view.getId() == R.id.settings)
            startActivity(new Intent(this, Settings.class));
    }

    public void PlayListView(String url) {
        Map json;
        int results;
        try {
            videos.clear();
            json = new GetJson().execute(url).get();
            results = Integer.parseInt(((Map) json.get("pageInfo")).get("totalResults").toString());
            do {
                ArrayList items = (ArrayList) json.get("items");
                for (int i = 0; i < items.size(); i++) {
                    Video video = new Video(((Map) ((Map) items.get(i)).get("snippet")));
                    new Thread(video).start();
                    videos.add(video);
                }
                results -= 50;
                if (results > 0) {
                    json = new GetJson().execute(url + "&pageToken=" + json.get("nextPageToken").toString()).get();
                }
            } while (results > 0);
            String[] names = new String[videos.size()];
            for (int i = 0; i < names.length; i++) {
                names[i] = videos.get(i).getName();
            }
            CustomList adapter = new CustomList(this, videos, names);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}