package com.example.bicycleinseoul;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {
    private static String TAG = "DataManager";

    public static HashMap<Integer, ArrayList<String>> parsingValue1 = new HashMap<>();
    public static HashMap<Integer, ArrayList<String>> parsingValue2 = new HashMap<>();

    public static ArrayList<String[]> value_location = new ArrayList<>();
    public static ArrayList<String> value_totalCount = new ArrayList<>();
    public static ArrayList<String> value_parkingCnt = new ArrayList<>();
    public static ArrayList<String> value_stationID = new ArrayList<>();
    public static ArrayList<String> value_shared = new ArrayList<>();
    public static ArrayList<String> value_stationName = new ArrayList<>();

    IDataMangerCallback mCallback;

    public void setCallback(IDataMangerCallback callback) {
        this.mCallback = callback;
    }

    private volatile static DataManager mSingleton = null;

    public static DataManager getInstance(){
        if(mSingleton == null){
            mSingleton = new DataManager();
        }

        return mSingleton;
    }

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
    }

    protected static ArrayList<String[]> getLocation(){
        return value_location;
    }

    public void requestParsingValue(){
        // 1 - 1000 page
        Thread parsingThread1 = new Thread(parsingStart1);
        parsingThread1.start();
        // 1001 - max page
        Thread parsingThread2 = new Thread(parsingStart2);
        parsingThread2.start();
    }

    // 파싱 서브스레드 1 시작.
    Runnable parsingStart1 = () -> {
        URL url = null;
        String str, receiveMsg;
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> arrStatus = new ArrayList<>();

        try {
            url = new URL("http://openapi.seoul.go.kr:8088/4e6d464a42726577383861756e7759/json/bikeList/1/1000");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);

            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }

            receiveMsg = buffer.toString();

            // rentBikeStatus(가장 바깥부)
            JSONObject jsonObjectStatus = new JSONObject(receiveMsg);
            String jStatus = jsonObjectStatus.getString("rentBikeStatus");

            // list_total_count
            JSONObject jsonObjectTotalCount = new JSONObject(jStatus);
            String jTotalCount = jsonObjectTotalCount.getString("list_total_count");

            // Result
            JSONObject jsonObjectResult = new JSONObject(jStatus);
            String jResult = jsonObjectResult.getString("RESULT");

            JSONObject jsonObjectResultValue = new JSONObject(jResult);
            String jCode = jsonObjectResultValue.getString("CODE");
            String jMessage = jsonObjectResultValue.getString("MESSAGE");
            arrStatus.add(jTotalCount);
            arrStatus.add(jCode);
            arrStatus.add(jMessage);

            parsingValue1.put(0, arrStatus);

            // row
            JSONObject jsonObjectValue = new JSONObject(jStatus);
            String jValue = jsonObjectValue.getString("row");
            JSONArray jsonArrayValue = new JSONArray(jValue);
            for (int i = 0; i < jsonArrayValue.length(); i++) {
                JSONObject subJsonObject = jsonArrayValue.getJSONObject(i);
                String rackTotCnt = subJsonObject.getString("rackTotCnt");
                String stationName = subJsonObject.getString("stationName");
                String parkingBikeTotCnt = subJsonObject.getString("parkingBikeTotCnt");
                String shared = subJsonObject.getString("shared");
                String stationLatitude = subJsonObject.getString("stationLatitude");
                String stationLongitude = subJsonObject.getString("stationLongitude");
                String stationId = subJsonObject.getString("stationId");

                ArrayList<String> arrValue = new ArrayList<>();

                arrValue.add(rackTotCnt);
                arrValue.add(stationName);
                arrValue.add(parkingBikeTotCnt);
                arrValue.add(shared);
                arrValue.add(stationLatitude);
                arrValue.add(stationLongitude);
                arrValue.add(stationId);

                parsingValue1.put(i + 1, arrValue);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    };

    // 파싱 서브스레드 2 시작.
    Runnable parsingStart2 = () -> {
        URL url = null;
        String str, receiveMsg;
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> arrStatus = new ArrayList<>();

        try {
            url = new URL("http://openapi.seoul.go.kr:8088/4e6d464a42726577383861756e7759/json/bikeList/1001/2000");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);

            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }

            receiveMsg = buffer.toString();

            // rentBikeStatus(가장 바깥부)
            JSONObject jsonObjectStatus = new JSONObject(receiveMsg);
            String jStatus = jsonObjectStatus.getString("rentBikeStatus");

            // list_total_count
            JSONObject jsonObjectTotalCount = new JSONObject(jStatus);
            String jTotalCount = jsonObjectTotalCount.getString("list_total_count");

            // Result
            JSONObject jsonObjectResult = new JSONObject(jStatus);
            String jResult = jsonObjectResult.getString("RESULT");

            JSONObject jsonObjectResultValue = new JSONObject(jResult);
            String jCode = jsonObjectResultValue.getString("CODE");
            String jMessage = jsonObjectResultValue.getString("MESSAGE");
            arrStatus.add(jTotalCount);
            arrStatus.add(jCode);
            arrStatus.add(jMessage);

            parsingValue2.put(0, arrStatus);

            // row
            JSONObject jsonObjectValue = new JSONObject(jStatus);
            String jValue = jsonObjectValue.getString("row");
            JSONArray jsonArrayValue = new JSONArray(jValue);
            for (int i = 0; i < jsonArrayValue.length(); i++) {
                JSONObject subJsonObject = jsonArrayValue.getJSONObject(i);
                String rackTotCnt = subJsonObject.getString("rackTotCnt");
                String stationName = subJsonObject.getString("stationName");
                String parkingBikeTotCnt = subJsonObject.getString("parkingBikeTotCnt");
                String shared = subJsonObject.getString("shared");
                String stationLatitude = subJsonObject.getString("stationLatitude");
                String stationLongitude = subJsonObject.getString("stationLongitude");
                String stationId = subJsonObject.getString("stationId");

                ArrayList<String> arrValue = new ArrayList<>();

                arrValue.add(rackTotCnt);
                arrValue.add(stationName);
                arrValue.add(parkingBikeTotCnt);
                arrValue.add(shared);
                arrValue.add(stationLatitude);
                arrValue.add(stationLongitude);
                arrValue.add(stationId);

                parsingValue2.put(i + 1, arrValue);
            }

            mCallback.onComplete(1);
            mergingValue(parsingValue1, parsingValue2);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    };

}
