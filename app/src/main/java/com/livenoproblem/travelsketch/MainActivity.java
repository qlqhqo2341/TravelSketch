package com.livenoproblem.travelsketch;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.livenoproblem.travelsketch.Common.Common;
import com.livenoproblem.travelsketch.Helper.Helper;
import com.livenoproblem.travelsketch.Model.OpenWeatherMap;
import com.livenoproblem.travelsketch.Struct.Travel;
import com.squareup.picasso.Picasso;

import java.io.*;
import java.lang.reflect.Type;


public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView txtCity, txtLastUpdate, txtDescription, txtHumidity, txtTime, txtCelsius, txtDesa, txtEmergencyCall;


    LocationManager locationManager;
    String provider;
    static double lat, lng;
    OpenWeatherMap openWeatherMap = new OpenWeatherMap();
    private String mNum;
    int MY_PERMISSION = 0;

    File travFile;
    Travel trav;

    static final int MANAGE_TRAVEL=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //파일에서 일정 로드하기
        travFile = new File(getApplicationContext().getFilesDir(),"trav.ser");
        try{
            ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(travFile)));
            trav = (Travel)input.readObject();
            input.close();
        }
        catch(Exception e){
            Log.i("MainActivity","새로운 trav를 생성합니다.");
            e.printStackTrace();
            trav = new Travel();
        }

        // 플로팅 액션 버튼
        FloatingActionButton fab0 = findViewById(R.id.fab_action0);
        fab0.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ListDataActivity.class);
                startActivity(intent);
                showToast("여행노트");
            }
        });

        FloatingActionButton fab1 = findViewById(R.id.fab_action1);
        fab1.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), manageTravel.class);
                intent.putExtra("travel",trav);
                startActivityForResult(intent,MANAGE_TRAVEL);
                showToast("일정추가");
            }
        });
        FloatingActionButton fab2 = findViewById(R.id.fab_action2);
        fab2.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "공유 테스트";
                String ShareSub = "공유 테스트 2";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent, "일정공유"));

                showToast("일정공유");
            }
        });
        FloatingActionButton fab3 = findViewById(R.id.fab_action3);
        fab3.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                showToast("목적지찾기");
            }
        });

        //Control
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtLastUpdate = (TextView) findViewById(R.id.txtLastUpdate);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtCelsius = (TextView) findViewById(R.id.txtCelsius);
        txtEmergencyCall = (TextView) findViewById(R.id.txtEmergencyCall);
        txtDesa = (TextView) findViewById(R.id.txtDesa);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //provider = locationManager.getBestProvider(new Criteria(), false);
        provider = LocationManager.NETWORK_PROVIDER;    //네트워크로 위치정보 받아오기


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE


                }, MY_PERMISSION);



        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null)
            Log.e("TAG", "No Location");
            if (locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER) == true) {
                provider = LocationManager.NETWORK_PROVIDER;
            } else
                provider = LocationManager.GPS_PROVIDER;

    }

    }

    //토스트메시지

    public void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE


                }, MY_PERMISSION);

            }
            locationManager.removeUpdates(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE


                }, MY_PERMISSION);

            }
            locationManager.requestLocationUpdates(provider, 1000, 1, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MANAGE_TRAVEL && resultCode==RESULT_OK){
            this.trav = (Travel)data.getSerializableExtra("travel");
            travelSave();
            //TODO 수정된 Travel이 화면에 적용 시켜야함.
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat),String.valueOf(lng)));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetWeather extends AsyncTask<String,Void,String>{
        ProgressDialog pd = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("기다려주세요...");
            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("Error: 현재 위치를 찾을 수 없습니다.")){
                pd.dismiss();
                return;
            }
            Gson gson = new Gson();
            Type mType = new TypeToken<OpenWeatherMap>(){}.getType();
            openWeatherMap = gson.fromJson(s,mType);
            pd.dismiss();

            txtCity.setText(String.format("%s,%s",openWeatherMap.getName(),openWeatherMap.getSys().getCountry()));
            txtLastUpdate.setText(String.format("업데이트됨: %s", Common.getDateNow()));
            txtDescription.setText(String.format("%s",openWeatherMap.getWeather().get(0).getDescription()));
            txtHumidity.setText(String.format("%d%%",openWeatherMap.getMain().getHumidity()));
            txtTime.setText(String.format("%s/%s",Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunrise()),Common.unixTimeStampToDateTime(openWeatherMap.getSys().getSunset())));
            txtCelsius.setText(String.format("%.0f°C",openWeatherMap.getMain().getTemp()));
            txtDesa.setText("※"+String.format("%s",openWeatherMap.getSys().getCountry())+"대사관번호");
            if(openWeatherMap.getSys().getCountry().equals("KR"))
            {
                txtEmergencyCall.setText("+82-2-3210-0404");
            }
            else if(openWeatherMap.getSys().getCountry().equals("GR")) //그리스
            {
                txtEmergencyCall.setText("00800-82-12-17264");
            }
            else if(openWeatherMap.getSys().getCountry().equals("ZA")) //남아공
            {
                txtEmergencyCall.setText("0800-99-4724");
            }
            else if(openWeatherMap.getSys().getCountry().equals("NL")) //네덜란드
            {
                txtEmergencyCall.setText("0800-022-9657");
            }
            else if(openWeatherMap.getSys().getCountry().equals("NO")) //노르웨이
            {
                txtEmergencyCall.setText("800-1-8517");
            }
            else if(openWeatherMap.getSys().getCountry().equals("NZ"))  //뉴질랜드
            {
                txtEmergencyCall.setText("0800-449522");
            }
            else if(openWeatherMap.getSys().getCountry().equals("DK"))  //덴마크
            {
                txtEmergencyCall.setText("8088-5544");
            }
            else if(openWeatherMap.getSys().getCountry().equals("DE")) //독일
            {
                txtEmergencyCall.setText("0800-181-6982");
            }
            else if(openWeatherMap.getSys().getCountry().equals("RU")) //러시아
            {
                txtEmergencyCall.setText("810800-2022-2082");
            }
            else if(openWeatherMap.getSys().getCountry().equals("MO")) //마카오
            {
                txtEmergencyCall.setText("0800-652");
            }
            else if(openWeatherMap.getSys().getCountry().equals("MY")) //말레이시아
            {
                txtEmergencyCall.setText("1-800-81-2099");
            }
            else if(openWeatherMap.getSys().getCountry().equals("US")) //미국
            {
                if(openWeatherMap.getName().equals("Hawaii"))
                {
                    txtEmergencyCall.setText("1-800-631-9551");
                }
              else
                {
                    txtEmergencyCall.setText("1-866-236-5670");
                }
            }
            else if(openWeatherMap.getSys().getCountry().equals("BE")) //벨기에
            {
                txtEmergencyCall.setText("0800-7-1233");
            }
            else if(openWeatherMap.getSys().getCountry().equals("CY")) //사이프러스
            {
                txtEmergencyCall.setText("8009-5685");
            }
            else if(openWeatherMap.getSys().getCountry().equals("SE")) //스웨덴
            {
                txtEmergencyCall.setText("020-795642");
            }
            else if(openWeatherMap.getSys().getCountry().equals("CH")) //스위스
            {
                txtEmergencyCall.setText("0800-561-345");
            }
            else if(openWeatherMap.getSys().getCountry().equals("SG")) //싱가포르
            {
                txtEmergencyCall.setText("800-8211-338");
            }
            else if(openWeatherMap.getSys().getCountry().equals("GB")) //영국
            {
                txtEmergencyCall.setText("0800-028-8307");
            }
            else if(openWeatherMap.getSys().getCountry().equals("IL")) //이스라엘
            {
                txtEmergencyCall.setText("180-947-6887");
            }
            else if(openWeatherMap.getSys().getCountry().equals("IT")) //이탈리아
            {
                txtEmergencyCall.setText("8007-82603");
            }
            else if(openWeatherMap.getSys().getCountry().equals("ID")) //인도네시아
            {
                txtEmergencyCall.setText("001-803-821-16863");
            }
            else if(openWeatherMap.getSys().getCountry().equals("JP")) //일본
            {
                txtEmergencyCall.setText("00531-82-0440");
            }
            else if(openWeatherMap.getSys().getCountry().equals("CN")) //중국
            {
                txtEmergencyCall.setText("10800-282-0001");
            }
            else if(openWeatherMap.getSys().getCountry().equals("CA")) //캐나다
            {
                txtEmergencyCall.setText("1-866-708-2838");
            }
            else if(openWeatherMap.getSys().getCountry().equals("TH")) //타이
            {
                txtEmergencyCall.setText("001-800-821-16892");
            }
            else if(openWeatherMap.getSys().getCountry().equals("TW")) //타이완
            {
                txtEmergencyCall.setText("00801-82-7353");
            }
            else if(openWeatherMap.getSys().getCountry().equals("FR")) //프랑스
            {
                txtEmergencyCall.setText("0800-90-2590");
            }
            else if(openWeatherMap.getSys().getCountry().equals("PH")) //필리핀
            {
                txtEmergencyCall.setText("1-800-1-821-0248");
            }
            else if(openWeatherMap.getSys().getCountry().equals("AU")) //호주
            {
                txtEmergencyCall.setText("1-800-249-224");
            }
            else if(openWeatherMap.getSys().getCountry().equals("HK")) //홍콩
            {
                txtEmergencyCall.setText("800-90-0524");
            }
            else
            {
                txtEmergencyCall.setText("+82-2-3210-0404");
            }
            //대사관연락
            mNum = txtEmergencyCall.getText().toString();
            final String tel ="tel:" + mNum;
            txtEmergencyCall.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                    showToast("긴급전화");
                }
            });

        }

    }

    private boolean travelSave(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(travFile)));
            output.writeObject(trav);
            output.close();
        }
        catch (Exception e){
            e.printStackTrace();
            showToast("파일 저장 실패");
            return false;
        }
        return true;
    }
}
