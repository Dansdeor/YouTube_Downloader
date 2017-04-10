package com.dan.youtube;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

class GetJson extends AsyncTask<String,Void,Map> {
    @Override
    protected Map doInBackground(String... params) {
        Map<String, String> JsonMap = null;
        try {
            String line;
            URL Url = new URL(params[0]);
            URLConnection urlConnection = Url.openConnection();
            urlConnection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null)
                stringBuffer.append(line);
            JsonMap = new ObjectMapper().readValue(stringBuffer.toString(), new TypeReference<HashMap<String, Object>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonMap;
    }
}
