package com.example.bicycleinseoul;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.daum.android.map.coord.MapCoordLatLng;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;

public class MapFragment extends Fragment {
    private static MapView mapView;

    private static MapFragment mMapFragment = null;

    public static MapFragment initMapFragment(){
        mMapFragment = new MapFragment();

        return mMapFragment;
    }

    public static MapFragment getMapFragment(){
        return mMapFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = new MapView(getContext());
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        return view;
    }

    public static void setMarker() {
        ArrayList<String[]> arrayList = new ArrayList<>();
        arrayList = DataManager.getLocation();

        for(int i=0; i<arrayList.size(); i++){
            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(arrayList.get(i)[0]), Double.parseDouble(arrayList.get(i)[1]));

            marker.setItemName("marker!");
            marker.setTag(i);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView.addPOIItem(marker);
        }


    }

}
