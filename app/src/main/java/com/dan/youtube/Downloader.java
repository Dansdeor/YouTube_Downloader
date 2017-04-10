package com.dan.youtube;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

class Downloader implements Runnable {
    private static String Path;
    private URL url;

    public Downloader(URL url, String Path) {
        this.url = url;
        this.Path = Path;
    }

    @Override
    public void run() {
        try {
            int count;
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(Path);
            byte[] data = new byte[1024];
            while ((count = input.read(data)) != -1)
                output.write(data, 0, count);
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}