package com.example.bicycleinseoul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;

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
    private Button btnTracing, btnObtion, btnStatus;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAppKeyHash();
        initReferences();

        initControls();
    }

    private void initControls() {
        btnTracing.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TracingActivity.class);
            startActivity(intent);
        });

        btnStatus.setOnClickListener(v -> {
            startParsing();
        });

        btnObtion.setOnClickListener(v -> {

        });
    }

    private void startParsing() {
        Thread parsingThread = new Thread(parsingStart);
        parsingThread.start();
    }

    // 파싱 서브스레드 시작.
    Runnable parsingStart = () -> {
        URL url = null;
        String str, receiveMsg;
        StringBuffer buffer = new StringBuffer();

        String[] arraysum = new String[7];

        try{
            url = new URL("http://openapi.seoul.go.kr:8088/4e6d464a42726577383861756e7759/json/bikeList/1/1000");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);

            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }

            receiveMsg = buffer.toString();

            // json parsing
//            bicycleVO VO = gson.fromJson(receiveMsg, bicycleVO.class);
//            Log.e("%% : ", VO.getParkingBikeTotCnt());
//            Log.e("%%% : ", VO.getStationId());
//            Model model = gson.fromJson(receiveMsg, Model.class);
//            StringBuffer result = new StringBuffer();
//            result.append(model.status.list_total_count);
//
//            Log.e("@@@@@@@@", result.toString());

            Log.e("receiveMsg : ", receiveMsg);

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

            // row
            JSONObject jsonObjectValue = new JSONObject(jStatus);
            String jValue = jsonObjectValue.getString("row");
            JSONArray jsonArrayValue = new JSONArray(jValue);
            for(int i=0; i<jsonArrayValue.length(); i++){
                JSONObject subJsonObject = jsonArrayValue.getJSONObject(i);
                String rackTotCnt = subJsonObject.getString("rackTotCnt");
                String stationName = subJsonObject.getString("stationName");
                String parkingBikeTotCnt = subJsonObject.getString("parkingBikeTotCnt");
                String shared = subJsonObject.getString("shared");
                String stationLatitude = subJsonObject.getString("stationLatitude");
                String stationLongitude = subJsonObject.getString("stationLongitude");
                String stationId = subJsonObject.getString("stationId");

                Log.e("good ?", rackTotCnt + stationName + parkingBikeTotCnt + shared + stationLatitude
                + stationLongitude + stationId + "\n\n\n");
            }


//            String j_CODE = jsonObjectResult.getString("CODE");
//            String j_MESSAGE = jsonObjectResult.getString("MESSAGE");

            Log.e("###", jTotalCount + jResult + jCode + jMessage);
//            for(int i=0; i<jsonArray.length(); i++){
//                JSONObject subJsonObject = jsonArray.getJSONObject(i);
//                String list_total_count = subJsonObject.getString("list_total_count");
//                String RESULT = subJsonObject.getString("RESULT");
//                String row = subJsonObject.getString("row");
//            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    };

    private void initReferences() {
        btnTracing = findViewById(R.id.btnTracing);
        btnStatus = findViewById(R.id.btnStatus);
        btnObtion = findViewById(R.id.btnObtion);
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