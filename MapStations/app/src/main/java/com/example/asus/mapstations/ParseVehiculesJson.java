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

public class ParseVehiculesJson {


    public static List<Vehicule> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public static List<Vehicule> readMessagesArray(JsonReader reader) throws IOException {
        List<Vehicule> messages = new ArrayList<Vehicule>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public static Vehicule readMessage(JsonReader reader) throws IOException {

        String marque="";
        String carburant="";
        int cylindre=0;
        String vehicule="";
        double consomation=0;
        reader.beginObject();
        reader.hasNext();
        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.equals("marque")) {
                marque = reader.nextString();
            } else if (name.equals("carburant")) {
                carburant = reader.nextString();
            } else if (name.equals("cylindre")) {
                cylindre=reader.nextInt();
            } else if (name.equals("véhicule")) {
                vehicule= reader.nextString();
            } else if (name.equals("consommation l/100km")) {
                consomation=reader.nextDouble();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        System.out.println(marque+"//"+carburant+"//"+cylindre+"//"+vehicule+"//"+consomation+"//");
        return new Vehicule(marque,carburant,cylindre,vehicule,consomation);
    }




}
