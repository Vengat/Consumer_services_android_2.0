package com.example.vengatr.consumer_services_android_20;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by vengat.r on 7/24/2015.
 */
public class UnableToReachServerFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Create empty image view");
        View view = inflater.inflate(R.layout.fragment_unable_to_reach_server, container, false);
        TextView textView = (TextView) view.findViewById(R.id.unreachable_server_text);
        return view;
    }
}
