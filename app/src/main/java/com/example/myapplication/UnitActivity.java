package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.google.android.material.navigation.NavigationView;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class UnitActivity extends AppCompatActivity {
    private static String TAG="LIFECYCLE";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ImageView menu;

    Toast toast;
    private EditText editLength;
    private EditText editVolume;
    private TextView textLength;
    private TextView textVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        initView();

        initLength();

        initVolume();
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
                        Intent intent_rate = new Intent(UnitActivity.this,MainActivity.class);
                        startActivity(intent_rate);
                        finish();
                        return true;
                    case R.id.binary:
                        Intent intent_bin = new Intent(UnitActivity.this,BinaryActivity.class);
                        startActivity(intent_bin);
                        finish();
//                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.unit:
                        drawerLayout.closeDrawer(navigationView);
//                        Toast.makeText(RateActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.rate:
                        Intent intent_unit = new Intent(UnitActivity.this,RateActivity.class);
                        startActivity(intent_unit);
                        finish();
//                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.cashbook:
//                        showCashbook();
                        Toast.makeText(UnitActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return UnitActivity.super.onOptionsItemSelected(item);
                }
            }
        });
    }

    private void initLength(){
        editLength = findViewById(R.id.edit_length);
        textLength = findViewById(R.id.text_length);

        final LoopView loopView1 = (LoopView) findViewById(R.id.loopView_length_from);
        final LoopView loopView2 = (LoopView) findViewById(R.id.loopView_length_to);

        ArrayList<String> list = new ArrayList<>();
        list.add("毫米(mm)");
        list.add("厘米(cm)");
        list.add("分米(dm)");
        list.add("米(m)");
        list.add("公里(km)");

        //滚动监听
        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(UnitActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();

                String num1 = editLength.getText().toString();
                if(num1!=null && !num1.equals("")){
                    double result;
                    if (loopView1.getSelectedItem()>=loopView2.getSelectedItem()){
                        result = Double.valueOf(num1)*Math.pow(10,loopView1.getSelectedItem()-loopView2.getSelectedItem());
                    }else {
                        result = Double.valueOf(num1)/Math.pow(10,loopView2.getSelectedItem()-loopView1.getSelectedItem());
                    }

                    textLength.setText(result+"");
                }
            }

        });
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(UnitActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();

                String num1 = editLength.getText().toString();
                if(num1!=null && !num1.equals("")){
                    double result;
                    if (loopView1.getSelectedItem()>=loopView2.getSelectedItem()){
                        result = Double.valueOf(num1)*Math.pow(10,loopView1.getSelectedItem()-loopView2.getSelectedItem());
                    }else {
                        result = Double.valueOf(num1)/Math.pow(10,loopView2.getSelectedItem()-loopView1.getSelectedItem());
                    }

                    textLength.setText(result+"");
                }
            }

        });

        //设置原始数据
        loopView1.setItems(list);
        loopView2.setItems(list);

    }


    private void initVolume(){
        editVolume = findViewById(R.id.edit_volume);
        textVolume = findViewById(R.id.text_volume);

        final LoopView loopView1 = (LoopView) findViewById(R.id.loopView_volume_from);
        final LoopView loopView2 = (LoopView) findViewById(R.id.loopView_volume_to);

        ArrayList<String> list = new ArrayList<>();
        list.add("立方毫米");
        list.add("立方厘米");
        list.add("立方米");

        //滚动监听
        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(UnitActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();

                String num1 = editVolume.getText().toString();
                if(num1!=null && !num1.equals("")){
                    double result;
                    if (loopView1.getSelectedItem()>=loopView2.getSelectedItem()){
                        result = Double.valueOf(num1)*Math.pow(1000,loopView1.getSelectedItem()-loopView2.getSelectedItem());
                    }else {
                        result = Double.valueOf(num1)/Math.pow(1000,loopView2.getSelectedItem()-loopView1.getSelectedItem());
                    }

                    textVolume.setText(result+"");
                }
            }

        });
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(UnitActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();

                String num1 = editVolume.getText().toString();
                if(num1!=null && !num1.equals("")){
                    double result;
                    if (loopView1.getSelectedItem()>=loopView2.getSelectedItem()){
                        result = Double.valueOf(num1)*Math.pow(1000,loopView1.getSelectedItem()-loopView2.getSelectedItem());
                    }else {
                        result = Double.valueOf(num1)/Math.pow(1000,loopView2.getSelectedItem()-loopView1.getSelectedItem());
                    }

                    textVolume.setText(result+"");
                }
            }

        });

        //设置原始数据
        loopView1.setItems(list);
        loopView2.setItems(list);

    }
}