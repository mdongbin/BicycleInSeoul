package com.example.bicycleinseoul;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;

public class MapFragment extends Fragment {
    private MapView mapView;

    private static MapFragment mMapFragment;

    public static MapFragment initMapFragment(){
        mMapFragment = new MapFragment();

        return mMapFragment;
    }

    public static MapFragment getMapFragment(){
        return mMapFragment;
    }

    HashMap<Integer, ArrayList<String>> result = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = new MapView(getContext());
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);



        return view;
    }

    public void getAPIdata(){
        Bundle bundle = getArguments();
        if(bundle != null){
            result = (HashMap<Integer, ArrayList<String>>)bundle.getSerializable("bundle1");
            Log.e("######", result.get(0).get(0));
        }
    }
}
