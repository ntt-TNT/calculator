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

import com.google.android.material.navigation.NavigationView;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemScrollListener;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;

public class BinaryActivity extends AppCompatActivity {
    private String TAG="LIFECYCLE";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ImageView menu;
    private Toast toast;

    private EditText editNum1;
    private EditText editNum2;
    private EditText editGirth;
    private EditText editArea;
    private int type1;
    private int type2;

    private TextView textGirth;
    private TextView textArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary);

        initView();

        initBinary();

        initGirth();


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
                        Intent intent_cal = new Intent(BinaryActivity.this,MainActivity.class);
                        startActivity(intent_cal);
                        finish();
                        return true;
                    case R.id.binary:
                        drawerLayout.closeDrawer(navigationView);
//                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.unit:
//                        showUnit();
                        Toast.makeText(BinaryActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.rate:
//                        showRate();
                        Toast.makeText(BinaryActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.cashbook:
//                        showCashbook();
                        Toast.makeText(BinaryActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return BinaryActivity.super.onOptionsItemSelected(item);
                }
            }
        });
    }

    private void initBinary(){
        final LoopView loopView1 = (LoopView) findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) findViewById(R.id.loopView2);


        editNum1 = findViewById(R.id.edit_num1);
        editNum2 = findViewById(R.id.edit_num2);
//        type1 = initLoopView(loopView1);
//        type2 = initLoopView(loopView2);
        ArrayList<String> list = new ArrayList<>();
        list.add("二进制");
        list.add("八进制");
        list.add("十进制");
        list.add("十六进制");
        //滚动监听
        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(BinaryActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();
                type1 = index;
            }

        });
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(BinaryActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();
                type2 = index;
                String num1 = editNum1.getText().toString();
                String num2 = editNum2.getText().toString();
                operation(num1,type1,num2,type2);
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
    }

    private void initGirth(){
        final LoopView loopView_girth = (LoopView) findViewById(R.id.loopView_girth);
        editGirth = findViewById(R.id.editText_girth);
        textGirth = findViewById(R.id.girth_result);
        editArea = findViewById(R.id.editText_girth);
        textArea = findViewById(R.id.area_result);

        ArrayList<String> list = new ArrayList<>();
        list.add("长方形");
        list.add("正方形");
        list.add("三角形");
        list.add("圆形");
        //滚动监听
        loopView_girth.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(BinaryActivity.this, list.get(index), Toast.LENGTH_SHORT);
                }
                toast.setText(list.get(index));
                toast.show();
            }

        });
        //设置原始数据
        loopView_girth.setItems(list);

        Log.d(TAG, "initGirth: "+editGirth.getText().toString());

        findViewById(R.id.button_girth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editGirth.getText().toString()==null || editGirth.getText().toString().equals("")) {
                    Log.d(TAG, "onClick: ");
                    Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] str_side = editGirth.getText().toString().split(",");
                double[] double_side = new double[str_side.length];
                double girth=0;
                for(int i=0; i<str_side.length; i++){
                    double_side[i] = Double.valueOf(str_side[i]);
                }
                switch (loopView_girth.getSelectedItem()){
                    case 0:
                        if(double_side.length>=2){
                            girth = (double_side[0]+double_side[1])*2;
                        }else {
                            Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT);
                        }
                        break;
                    case 1:
                        if(double_side.length>=1){
                            girth = double_side[0]*4;
                        }else {
                            Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if(double_side.length>=3){
                            double a = double_side[0];
                            double b = double_side[1];
                            double c = double_side[2];
                            if (a+b>c&&a+c>b&&b+c>a){
                                girth = double_side[0]+double_side[1]+double_side[2];
                            }else {
                                Toast.makeText(BinaryActivity.this, "这不是一个三角形", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        if(double_side.length>=1){
                            girth = double_side[0]*2*Math.PI;
                        }else {
                            Toast.makeText(BinaryActivity.this, "请输入半径", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT).show();
                }
                textGirth.setText(girth+"");

            }

        });

        findViewById(R.id.button_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editArea.getText().toString()==null || editArea.getText().toString().equals("")) {
                    Log.d(TAG, "onClick: ");
                    Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] str_side = editArea.getText().toString().split(",");
                double[] double_side = new double[str_side.length];
                double area=0;
                for(int i=0; i<str_side.length; i++){
                    double_side[i] = Double.valueOf(str_side[i]);
                }
                switch (loopView_girth.getSelectedItem()){
                    case 0:
                        if(double_side.length>=2){
                            area = double_side[0]*double_side[1];
                        }else {
                            Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT);
                        }
                        break;
                    case 1:
                        if(double_side.length>=1){
                            area = double_side[0]*double_side[0];
                        }else {
                            Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if(double_side.length>=3){
                            double a = double_side[0];
                            double b = double_side[1];
                            double c = double_side[2];
                            if (a+b>c&&a+c>b&&b+c>a){
                                double s=(a+b+c)/2;
                                area=(Math.sqrt(s*(s-a)*(s-b)*(s-c)));
                            }else {
                                Toast.makeText(BinaryActivity.this, "这不是一个三角形", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        if(double_side.length>=1){
                            area = double_side[0]*double_side[0]*Math.PI;
                        }else {
                            Toast.makeText(BinaryActivity.this, "请输入半径", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(BinaryActivity.this, "请输入正确格式", Toast.LENGTH_SHORT).show();
                }
                textArea.setText(area+"");
            }
        });

    }

    public void getArea(int type){



    }




    public void operation(String num1, int type1, String num2, int type2){
        Log.d(TAG, "operation: "+num1 + num2+type1+type2);
        int num;
        switch (type1){
            case 0:
                num = Integer.parseInt(num1, 2);
                break;
            case 1:
                num = Integer.parseInt(num1, 8);
                break;
            case 2:
                num = Integer.parseInt(num1, 10);
                break;
            case 3:
                num = Integer.parseInt(num1, 16);
                break;
            default:
                num = Integer.parseInt(num1, 2);
                break;
        }
        Log.d(TAG, "num: "+num);
        switch (type2){
            case 0:
                editNum2.setText(Integer.toBinaryString(num)+"");
                break;
            case 1:
                editNum2.setText(Integer.toOctalString(num)+"");
                break;
            case 2:
                editNum2.setText(num+"");
                break;
            case 3:
                editNum2.setText(Integer.toHexString(num)+"");
                break;
            default:
                editNum2.setText(Integer.toBinaryString(num)+"");
                break;
        }

    }
}