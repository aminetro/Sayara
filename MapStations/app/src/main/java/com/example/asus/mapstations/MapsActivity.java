package com.example.asus.mapstations;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.JsonReader;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.alertdialogpro.AlertDialogPro;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements RoutingListener,OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    public static String PREFRENCE_FILNAME = "com.example.asus.mapstations";
    SharedPreferences share;
    private String stationName;
    List<Station> locationList1;List<Station> locationList2;
    int stat=0;
    LatLng CurrentLocation;
    private double lon, lat;
    protected LatLng end;
    private GoogleMap mMap;
    private Context context;
    private InputStream inshell;
    private InputStream inagile;
    private LocationManager locationManager;
    Vehicule vv;
    String selectedinfo="";
    private static final int[] COLORS = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};


    private List<Polyline> polylines;

    final MarkerOptions mp = new MarkerOptions();
    final MarkerOptions wanttogo = new MarkerOptions();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        share= getSharedPreferences(PREFRENCE_FILNAME, Context.MODE_PRIVATE);
        vv=new Vehicule();
        polylines = new ArrayList<>();
        locationManager = (LocationManager) this.getSystemService(context.LOCATION_SERVICE);
        inshell = context.getResources().openRawResource(R.raw.stationshell);
        inagile = context.getResources().openRawResource(R.raw.stationagile);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        android.location.Location location;
        stationName = "";
        lon = 0;
        lat = 0;
        // Add a marker in Sydney and move the camera


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        String locationProvider = LocationManager.NETWORK_PROVIDER;

        location = locationManager.getLastKnownLocation(locationProvider);
        CurrentLocation = new LatLng(location.getLatitude(),location.getLongitude());


       // GPSTracker gpsTracker = new GPSTracker(context);
        // CurrentLocation = new LatLng(gpsTracker.getLocation().getLatitude(), gpsTracker.getLocation().getLongitude());
        //locationManager.requestLocationUpdates(locationProvider, 0, 0,  locationListener);
        mMap.addMarker(mp.position(CurrentLocation).title("You Are Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.myposition)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentLocation, 14));




        try {
            stat=1;
            locationList1=readJsonStream(inshell);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            stat=2;
            locationList2=readJsonStreamAgile(inagile);

        } catch (IOException e) {
            System.out.println("ooooooooooooooooooooooooo");
            e.printStackTrace();
        }



        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Marker mm = null;
                mm.setPosition(mp.getPosition());
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                animateMarker(mm,ll,false);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){


            @Override
            public void onMapLongClick(LatLng latLng) {


                mMap.addMarker(wanttogo.position(latLng));
                end=latLng;

                sendRequest();
                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.DRIVING)
                        .waypoints(mp.getPosition(),end)
                        .build();
                routing.execute();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                    end=marker.getPosition();
                    System.out.println(marker.getPosition().latitude+"ssssssss"+marker.getPosition().longitude);
                    for(int count=0;count<locationList2.size();count++){
                        System.out.println(locationList2.get(count).getLat()+"ssssssss"+locationList2.get(count).getLon());
                        if(marker.getPosition().latitude==locationList2.get(count).getLon() && marker.getPosition().longitude==locationList2.get(count).getLat()){
                            System.out.println("ssssssss"+locationList2.get(count).getInformation());
                            selectedinfo=locationList2.get(count).getInformation();
                        }
                    }

                    sendRequest();
                    Routing routing = new Routing.Builder()
                            .travelMode(Routing.TravelMode.DRIVING)
                            .waypoints(mp.getPosition(),end)
                            .build();
                    routing.execute();

                    System.out.println("kkkkkkkkkkkkkkkkkkk");


                return false;
            }
        });




    }






    public List<Station> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Station> readMessagesArray(JsonReader reader) throws IOException {
        List<Station> messages = new ArrayList<Station>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public Station readMessage(JsonReader reader) throws IOException {

        String place = "aa";
        double lon=0;
        double lat=0;

        reader.beginObject();
        reader.hasNext();
        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.equals(" station ")) {
                place = reader.nextString();
            } else if (name.equals("lon")) {
                lon = reader.nextDouble();
            } else if (name.equals("lat")) {
                lat = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        LatLng loca = new LatLng(lat,lon);
        if(lat!=0)
            mMap.addMarker(new MarkerOptions().position(loca).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.shell)));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(loca));
        return new Station(place, lon, lat);
    }





    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int j) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng((mp.getPosition().latitude+end.latitude)/2,(mp.getPosition().longitude+end.longitude)/2));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

        mMap.moveCamera(center);


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();

        int colorIndex = 0 % COLORS.length;

        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(COLORS[colorIndex]));
        polyOptions.width(10 + 0 * 3);
        polyOptions.addAll(arrayList.get(0).getPoints());
        Polyline polyline = mMap.addPolyline(polyOptions);
        polylines.add(polyline);

        vv.setMarque(share.getString("marque",""));

        vv.setCarburant(share.getString("carburant",""));

        vv.setCylindre(share.getInt("cylindre",0));

        vv.setNom(share.getString("vehicule",""));

        vv.setConsomation(share.getFloat("consomation",0));
        System.out.println("yyyyyyy "+vv.getConsomation()+" "+(float)arrayList.get(0).getDistanceValue()+" "+(float)arrayList.get(0).getDistanceValue());
        DecimalFormat df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits ( 2 ) ; //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits ( 2 ) ;
        df.setDecimalSeparatorAlwaysShown ( true ) ;
        System.out.println ( Double.parseDouble(df.format ( -4.327)));
        String msg =((float)arrayList.get(0).getDistanceValue())/1000+"Km - "+ ((arrayList.get(0).getDurationValue()/60)+1)+"mn\n\nConsommation - "+Double.parseDouble(df.format ((vv.getConsomation()*(float)arrayList.get(0).getDistanceValue())/1000/100))+" L";

/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        new DialogAlert(msg);
*/
        //DialogFragment newFragment = new DialogAlert(msg);
        //newFragment.show(getSupportFragmentManager(), "Shortest Route");

        if((float)arrayList.get(0).getDistanceValue()/1000 != 0){
            AlertDialogPro.Builder builder = new AlertDialogPro.Builder(this);
            if(selectedinfo==""){

                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
/*
                builder.setTitle("Shortest Route").
                        setMessage(msg).
                        setNegativeButton("OK", null).
                        show();*/
            }else{
                Toast.makeText(getApplicationContext(),msg+ "\n\n" + selectedinfo,Toast.LENGTH_LONG).show();
                /*
                builder.setTitle("Shortest Route").
                        setMessage(msg + "\n\n" + selectedinfo).
                        setNegativeButton("OK", null).
                        show();*/
            }

        }

        //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        selectedinfo="";

        // Start marker


    }

    public void sendRequest()
    {
        if(Util.Operations.isOnline(this))
        {
            route(mp.getPosition(),end);
        }
        else
        {
            Toast.makeText(this,"No internet connectivity",Toast.LENGTH_SHORT).show();
        }
    }


    public void route(LatLng start,LatLng end)
    {
        if(start==null || end==null)
        {
            System.out.println("nonononono");
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .build();
            routing.execute();
        }
    }




    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        end=marker.getPosition();
        System.out.println("ssssss");

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .waypoints(mp.getPosition(),marker.getPosition())
                .build();
        routing.execute();
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("tttttttt"+connectionResult.toString());
    }








    public List<Station> readJsonStreamAgile(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArrayAgile(reader);
        } finally {
            reader.close();
        }
    }

    public List<Station> readMessagesArrayAgile(JsonReader reader) throws IOException {
        List<Station> messages = new ArrayList<Station>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessageAgile(reader));
        }
        reader.endArray();
        return messages;
    }

    public Station readMessageAgile(JsonReader reader) throws IOException {

        String info;String caf;String lava;String vid;String shop;
        double lon;
        double lat;
        info = "";caf = "";lava = "";vid = "";shop="";
        lon=0;
        lat=0;
        reader.beginObject();
        reader.hasNext();
        while (reader.hasNext()) {

            String name = reader.nextName();

            if (name.equals("lon")) {
                lon = reader.nextDouble();
            } else if (name.equals("lat")) {

                lat = reader.nextDouble();
            } else if (name.equals("shop")) {
                shop=reader.nextString();
            } else if (name.equals("cafeteria")) {
                caf=reader.nextString();
            } else if (name.equals("lavage")) {
                lava=reader.nextString();
            } else if (name.equals("vidange")) {
                vid=reader.nextString();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(shop.equals("oui")){
            info = info + " Shop";
        }
        if(caf.equals("oui")){
            info = info + "Cafeteria ";
        }

        if(lava.equals("oui")){
            info = info + "Lavage ";
        }

        if(vid.equals("oui")){
            info = info + "Vidange ";
        }


        System.out.println("hhhhhhh "+info);
        LatLng loca = new LatLng(lat,lon);
        if(lat!=0)
            mMap.addMarker(new MarkerOptions().position(loca).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.agile)));
        Station ss=new Station(lon, lat,info);

        return ss;
    }
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

}
