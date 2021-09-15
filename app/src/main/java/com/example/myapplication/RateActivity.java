package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.google.android.material.navigation.NavigationView;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RateActivity extends AppCompatActivity {
    private static String TAG="LIFECYCLE";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ImageView menu;

    Toast toast;
    private EditText editNum1;
    private EditText editNum2;
    private TextView date;

    private String url = "https://api.exchangerate-api.com/v4/latest/USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        initView();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            initRate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        drawerLayout = findViewById(R.id.activity_nav);
        navigationView = findViewById(R.id.nav);
        menu = findViewById(R.id.main_menu);
        View headerView = navigationView.getHeaderView(0);//获取头布局
        navigationView.setItemIconTintList(null);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击菜单，跳出侧滑菜单
                if (drawerLayout.isDrawerOpen(navigationView)){
                    drawerLayout.closeDrawer(navigationView);
                }else{
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.calculator:
                        Intent intent_rate = new Intent(RateActivity.this,MainActivity.class);
                        startActivity(intent_rate);
                        finish();
                        return true;
                    case R.id.binary:
                        Intent intent_bin = new Intent(RateActivity.this,BinaryActivity.class);
                        startActivity(intent_bin);
                        finish();
//                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.unit:
                        Intent intent_unit = new Intent(RateActivity.this,UnitActivity.class);
                        startActivity(intent_unit);
                        finish();
//                        Toast.makeText(RateActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.rate:
                        drawerLayout.closeDrawer(navigationView);
//                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.cashbook:
//                        showCashbook();
                        Toast.makeText(RateActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return RateActivity.super.onOptionsItemSelected(item);
                }
            }
        });
    }

    private void initRate() throws IOException, JSONException {
        JSONObject json = loadJson(url);
//        Log.d(TAG, "initRate: "+json.getString("rates"));



        Map<String,Double> map = (Map<String, Double>) JSON.parseObject(json.getString("rates"), LinkedHashMap.class, Feature.OrderedField);
//        Log.d(TAG, "initRate: "+"["+json.getString("rates")+"]");


        editNum1 = findViewById(R.id.edit_num1);
        editNum2 = findViewById(R.id.edit_num2);
        date = findViewById(R.id.date);

        date.setText(date.getText().toString()+json.getString("date"));

        final LoopView loopView1 = (LoopView) findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) findViewById(R.id.loopView2);

        ArrayList<String> list = new ArrayList<>();
        for(String key:map.keySet()){
            list.add(key);
        }

        //滚动监听
        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(RateActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();

                String num1 = editNum1.getText().toString();
                if(num1!=null && !num1.equals("")){
                    Log.d(TAG, "onItemSelected: ");
                    String num2 = convert(map,list.get(loopView1.getSelectedItem()),list.get(loopView2.getSelectedItem()),Double.valueOf(num1));
                    editNum2.setText(num2);
                }
            }

        });
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(RateActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();

                String num1 = editNum1.getText().toString();
                if(num1!=null && !num1.equals("")){
                    Log.d(TAG, "onItemSelected: ");
                    String num2 = convert(map,list.get(loopView1.getSelectedItem()),list.get(loopView2.getSelectedItem()),Double.valueOf(num1));
                    editNum2.setText(num2);
                }
            }

        });

        //设置原始数据
        loopView1.setItems(list);
        loopView2.setItems(list);


        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopView1.setCurrentPosition(0);
                editNum1.setText("");
                editNum2.setText("");
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopView2.setCurrentPosition(0);
                editNum2.setText("");
            }
        });

        findViewById(R.id.transform).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = loopView1.getSelectedItem();
                loopView1.setCurrentPosition(loopView2.getSelectedItem());
                loopView2.setCurrentPosition(index);

                String num1 = editNum1.getText().toString();
                if(num1!=null && !num1.equals("")){
                    Log.d(TAG, "onItemSelected: ");
                    String num2 = convert(map,list.get(loopView1.getSelectedItem()),list.get(loopView2.getSelectedItem()),Double.valueOf(num1));
                    editNum2.setText(num2);
                }
            }
        });
    }

    //网络连接获取数据

    public static JSONObject loadJson (String url) throws JSONException {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            Log.d(TAG, "loadJson: ");
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
//                Log.d(TAG, "loadJson: "+inputLine);
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(json.toString());

    }


    //换算
    public String convert(Map map,String from_currency,String to_currency,double amount){
        if(from_currency!="USD"){
            amount = amount / Double.valueOf(map.get(from_currency).toString());
        }
        amount = amount*Double.valueOf(map.get(to_currency).toString());
        DecimalFormat format = new DecimalFormat("0.0000");
        return format.format(amount);
    }
}