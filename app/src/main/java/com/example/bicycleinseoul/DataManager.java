package com.example.bicycleinseoul;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {

    public static HashMap<Integer, ArrayList<String>> parsingValue1 = new HashMap<>();
    public static HashMap<Integer, ArrayList<String>> parsingValue2 = new HashMap<>();

    public static ArrayList<String[]> value_location = new ArrayList<>();
    public static ArrayList<String> value_totalCount = new ArrayList<>();
    public static ArrayList<String> value_parkingCnt = new ArrayList<>();
    public static ArrayList<String> value_stationID = new ArrayList<>();
    public static ArrayList<String> value_shared = new ArrayList<>();
    public static ArrayList<String> value_stationName = new ArrayList<>();

    public static void mergingValue(HashMap<Integer, ArrayList<String>> v1, HashMap<Integer, ArrayList<String>> v2){
        parsingValue1.putAll(v1);
        parsingValue2.putAll(v2);

        extractValue();
    }

    // 0 : 토탈카운트, 코드, 메시지
    // 1-0 : rackTotCount       1-1 : stationName       1-2 : parkingBikeTotCnt
    // 1-3 : shared             1-4 : stationLatitude   1-5 : stationLongitude      1-6 : stationId
    private static void extractValue() {
        for(int i=0; i<parsingValue1.size(); i++){
            if(i<parsingValue1.size()-1){
                value_totalCount.add(parsingValue1.get(i+1).get(0));
                value_stationName.add(parsingValue1.get(i+1).get(1));
                value_parkingCnt.add(parsingValue1.get(i+1).get(2));
                value_shared.add(parsingValue1.get(i+1).get(3));
                value_location.add(new String[]{parsingValue1.get(i+1).get(4), parsingValue1.get(i+1).get(5)});
                value_stationID.add(parsingValue1.get(i+1).get(6));

            }
        }

        for(int i=0; i<parsingValue2.size(); i++){
            if(i<parsingValue2.size()-1){
                value_totalCount.add(parsingValue2.get(i+1).get(0));
                value_stationName.add(parsingValue2.get(i+1).get(1));
                value_parkingCnt.add(parsingValue2.get(i+1).get(2));
                value_shared.add(parsingValue2.get(i+1).get(3));
                value_location.add(new String[]{parsingValue2.get(i+1).get(4), parsingValue2.get(i+1).get(5)});
                value_stationID.add(parsingValue2.get(i+1).get(6));
            }
        }

        MapFragment.setMarker();
    }

    protected static ArrayList<String[]> getLocation(){
        return value_location;
    }

}
