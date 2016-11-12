package com.example.asus.mapstations;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChooseVehiculeFragment extends Fragment {

    public static String PREFRENCE_FILNAME = "com.example.asus.mapstations";
    SharedPreferences share;

    String selectedMarque;String selectedCarburant;String selectedCylindre;String selectedVehicule;double selectedConso;
    Button marque,carburant,cylindre,vehicule,continu;
    Vehicule vv =new Vehicule();
    List<String> marquesList;
    List<String> carburantList;
    List<String> cylindreList;
    List<String> vehiculeList;
    List<Double> ConsosList;
    List<Vehicule> locationList1;private InputStream inVehicule;
    CharSequence marques[] = new CharSequence[] {};
    CharSequence carburants[] = new CharSequence[] {};
    CharSequence cylindres[] = new CharSequence[] {};
    CharSequence vehicules[] = new CharSequence[] {};
    private Handler mHandler;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_choose_vehicule,container,false);

        mHandler = new Handler();

        inVehicule = v.getContext().getResources().openRawResource(R.raw.listevehicules);

        share= this.getActivity().getSharedPreferences(PREFRENCE_FILNAME, Context.MODE_PRIVATE);

        marque=(Button)v.findViewById(R.id.editMarque);
        carburant=(Button)v.findViewById(R.id.editCarburant);
        cylindre=(Button)v.findViewById(R.id.editCylindre);
        vehicule=(Button)v.findViewById(R.id.editVehicule);

        if(share.getString("marque","")!=""){

            vv.setMarque(share.getString("marque",""));

            vv.setCarburant(share.getString("carburant",""));

            vv.setCylindre(share.getInt("cylindre",0));

            vv.setNom(share.getString("vehicule",""));

            vv.setConsomation(share.getFloat("consomation",0));

            marque.setText(vv.getMarque());
            carburant.setText(vv.getCarburant());
            cylindre.setText(vv.getCylindre()+"");
            vehicule.setText(vv.getNom());
        }


        try {
            ParseVehiculesJson pv=new ParseVehiculesJson();
            locationList1=pv.readJsonStream(inVehicule);
        } catch (IOException e) {
            e.printStackTrace();
        }

        marquesList=new ArrayList<String>();
        carburantList=new ArrayList<String>();
        cylindreList=new ArrayList<String>();
        vehiculeList=new ArrayList<String>();
        ConsosList=new ArrayList<Double>();
        List<Integer> cylindreList2=new ArrayList<Integer>();
        for(int i=1;i<locationList1.size();i++){

            if(!(marquesList.contains(locationList1.get(i).getMarque())) && locationList1.get(i).getMarque()!="")
                marquesList.add(locationList1.get(i).getMarque());
            if(!(carburantList.contains(locationList1.get(i).getCarburant())) && locationList1.get(i).getCarburant()!="")
                carburantList.add(locationList1.get(i).getCarburant());
            if(!(cylindreList.contains(locationList1.get(i).getCylindre()+""))  && locationList1.get(i).getCylindre()!=0){
                cylindreList2.add(locationList1.get(i).getCylindre());
                cylindreList.add(locationList1.get(i).getCylindre()+"");

            }
        }
        int passage=0;
        boolean perm=true;
        while (perm){
            perm = false;
            passage ++;

            for(int k=0;k<cylindreList.size()-passage;k++){
                if( cylindreList2.get(k) > cylindreList2.get(k+1)){
                    perm=true;
                    String cc=cylindreList.get(k);int xx=cylindreList2.get(k);
                    cylindreList.set(k,cylindreList.get(k+1));cylindreList2.set(k,cylindreList2.get(k+1));
                    cylindreList.set(k+1,cc);cylindreList2.set(k+1,xx);


                }
            }
        }


        marques= marquesList.toArray(new CharSequence[marquesList.size()]);
        carburants= carburantList.toArray(new CharSequence[carburantList.size()]);
        cylindres= cylindreList.toArray(new CharSequence[cylindreList.size()]);






        marque.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choisir votre marque");
                builder.setItems(marques, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedMarque=marquesList.get(which);
                        marque.setText(marquesList.get(which));
                    }
                });
                builder.setNegativeButton("OK",null).show();
            }

        });



        carburant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choisir votre carburant");
                builder.setItems(carburants, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCarburant=carburantList.get(which);
                        carburant.setText(carburantList.get(which));
                    }
                });
                builder.setNegativeButton("OK",null).show();
            }

        });





        cylindre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choisir votre cylindre");
                builder.setItems(cylindres, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCylindre=cylindreList.get(which);
                        cylindre.setText(cylindreList.get(which));
                    }
                });
                builder.setNegativeButton("OK",null).show();
            }

        });


        vehicule.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                vehiculeList=new ArrayList<String>();

                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());


                for(int i=0;i<locationList1.size();i++){
                   if((locationList1.get(i).getMarque().equals(selectedMarque) && locationList1.get(i).getCarburant().equals(selectedCarburant) && (locationList1.get(i).getCylindre() + "").equals(selectedCylindre))){
                        ConsosList.add(locationList1.get(i).getConsomation());
                        vehiculeList.add(locationList1.get(i).getNom());
                    }
                }
                vehicules=vehiculeList.toArray(new CharSequence[vehiculeList.size()]);
                if(vehiculeList.size()!=0){
                    builder.setTitle("Choisir votre Vehicule");
                    builder.setItems(vehicules, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedVehicule=vehiculeList.get(which);
                            vehicule.setText(vehiculeList.get(which));



                            selectedConso=ConsosList.get(which);
                            SharedPreferences.Editor editor=share.edit();

                            editor.putString("marque", selectedMarque);
                            editor.putString("carburant", selectedCarburant);
                            editor.putInt("cylindre",Integer.parseInt(selectedCylindre));
                            editor.putString("vehicule", selectedVehicule);
                            editor.putFloat("consomation",(float) selectedConso);

                            editor.commit();
                            //Toast.makeText(v.getContext(),"Votre Consomation Moyenne pour 100Km = "+ConsosList.get(which),Toast.LENGTH_LONG).show();

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(v.getContext());
                            builder2.setTitle("Votre Consomation Moyenne pour 100Km = "+ConsosList.get(which)+"L");
                            builder2.setNegativeButton("OK",null).show();
                                }
                            });
                    builder.setNegativeButton("OK",null).show();

                }else{

                    builder.setTitle("Pas de vehicule avec ces criteres").setNegativeButton("OK",null).show();
                }

            }

        });






        return v;
    }







    public List<Vehicule> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Vehicule> readMessagesArray(JsonReader reader) throws IOException {
        List<Vehicule> messages = new ArrayList<Vehicule>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public Vehicule readMessage(JsonReader reader) throws IOException {

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
        if(cylindre==0){
            return null;
        }else {
             return new Vehicule(marque, carburant, cylindre, vehicule, consomation);
        }
    }

}
