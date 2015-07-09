package com.example.vengatr.consumer_services_android_20;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by vengat.r on 7/9/2015.
 */
public class EmptyFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Create empty image view");
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewNoJobs);
        imageView.setImageResource(R.drawable.take_order);
        return view;
    }
}
