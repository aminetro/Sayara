package com.example.asus.mapstations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Asus on 11/11/2016.
 */

public class HomeFragment extends Fragment {
    TextView text;
    //Button bt;
    public static String PREFRENCE_FILNAME = "com.example.asus.mapstations";
    SharedPreferences share;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        text = (TextView) v.findViewById(R.id.textViewH);
        //bt = (Button) v.findViewById(R.id.BThome);
        share= this.getActivity().getSharedPreferences(PREFRENCE_FILNAME, Context.MODE_PRIVATE);

        if(share.getString("marque","")!=""){
            text.setText("YOUR CHOSEN CAR"
                    +"\n"+"\n"+"Marque : "+share.getString("marque","")
                    +"\n"+"\n"+"Carburant : "+share.getString("carburant","")
                    +"\n"+"\n"+"Nombre de cylindre : "+share.getInt("cylindre",0)
                    +"\n"+"\n"+"Nom de vehicule : "+share.getString("vehicule","")
                    +"\n"+"\n"+"Consommation 100Km/L : "+share.getFloat("consomation",0));
            //bt.setText("Change Vehicule");
        }else {
            text.setText("You Have Not Chosen A Vehicule Yet");
            //bt.setText("Choose Vehicule");
        }

        return v;
    }

}
