package com.example.asus.mapstations;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 29/10/2016.
 */

public class ParseAgileJson {


    public static List<Station> readJsonStreamAgile(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArrayAgile(reader);
        } finally {
            reader.close();
        }
    }

    public static List<Station> readMessagesArrayAgile(JsonReader reader) throws IOException {
        List<Station> messages = new ArrayList<Station>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessageAgile(reader));
        }
        reader.endArray();
        return messages;
    }

    public static Station readMessageAgile(JsonReader reader) throws IOException {

        String info;
        double lon;
        double lat;
        info = "";
        lon=0;
        lat=0;
        reader.beginObject();
        reader.hasNext();
        while (reader.hasNext()) {
            info = "";
            lon=0;
            lat=0;
            String name = reader.nextName();
            if (name.equals("lon")) {
                lon = reader.nextDouble();
            } else if (name.equals("lat")) {
                lat = reader.nextDouble();
            } else if (name.equals("cafeteria")) {
                if(reader.nextString()=="oui"){
                    info="Cafeteria";
                }
            } else if (name.equals("lavage")) {
                if(reader.nextString()=="oui"){
                    info+="\nLavage";
                }
            } else if (name.equals("24h/24")) {
                if(reader.nextString()=="oui"){
                    info+="\n24h/24";
                }
            } else if (name.equals("vidange")) {
                if(reader.nextString()=="oui"){
                    info+="\nVidange";
                }
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new Station(lon, lat,info);
    }




}
