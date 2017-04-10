package com.dan.youtube;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.net.URL;
import java.util.Map;

class Video implements Runnable {

    private Map item;
    private String name;
    private String ID;
    private Bitmap image;
    private boolean checked = true;

    public Video(Map item) {
        this.item = item;
    }

    @Override
    public void run() {
        try {
            name = (String) item.get("title");
            ID = (String) ((Map) item.get("resourceId")).get("videoId");
            URL url = new URL(((Map) ((Map) item.get("thumbnails")).get("default")).get("url").toString());
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getID() {
        return ID;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}