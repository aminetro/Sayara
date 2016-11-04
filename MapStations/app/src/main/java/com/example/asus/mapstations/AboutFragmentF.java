package com.example.asus.mapstations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class AboutFragmentF extends Fragment {

    RoundImage roundedImage1,roundedImage2;
    ImageView amine,houda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_aboutf,container,false);

        amine=(ImageView)v.findViewById(R.id.profile_image);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.amine);
        roundedImage1 = new RoundImage(bm);
        amine.setImageDrawable(roundedImage1);

        houda=(ImageView)v.findViewById(R.id.profile1_image);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.houda);
        roundedImage2 = new RoundImage(bm2);
        houda.setImageDrawable(roundedImage2);


        return v;
    }

}
