package com.example.bicycleinseoul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    private HashMap<Integer, ArrayList<String>> parsingValue1 = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> parsingValue2 = new HashMap<>();

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAppKeyHash();

        initReferences();
        startParsing();
    }

    private void initReferences() {
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("지도"));
        tabLayout.addTab(tabLayout.newTab().setText("알리미"));
        tabLayout.addTab(tabLayout.newTab().setText("즐겨찾기"));
        tabLayout.addTab(tabLayout.newTab().setText("더보기"));

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void startParsing() {
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

            reader.close();
            sendData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    };

    //fragment로 보낸다.
    private void sendData() {
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("bundle1", parsingValue1);
        MapFragment a = MapFragment.getMapFragment();
        a.setArguments(bundle1);

        a.getAPIdata();
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

}