package com.example.myapplication;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView input_text;
    private TextView result_text;
    private boolean equal=false;
    private String TAG="LIFECYCLE";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ImageView menu;

    //声音
    private SoundPool soundPool;
    private int music;

    boolean screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(MainActivity.this);
        setContentView(R.layout.activity_main);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            screen = false;// 横屏
        } else if (this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
            screen = true; // 竖屏
        }

        initView();


        input_text = findViewById(R.id.input_text);
        result_text = findViewById(R.id.result_text);

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        music = soundPool.load(this,R.raw.music,1);
    }

    public static void setStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    public void onNumberButtonClick(View view){
        Log.d(TAG, "onNumberButtonClick: ");
        onMusicClick(view);
        String input = input_text.getText().toString();
        String result = result_text.getText().toString();
        String btn = ((Button) findViewById(view.getId())).getText().toString();
        if (equal==false){
            if (input.charAt(0)=='0' && !(input.contains("/") || input.contains("*") || input.contains("-") || input.contains("+") || input.contains("%") || input.contains(".")) || result.equals("错误")){
                switch (btn){
                    case "e":
                        input_text.setText(Math.exp(1)+"");
                        break;
                    case "PI":
                        input_text.setText(Math.PI+"");
                        break;
                    default:
                        input_text.setText(btn);
                        break;
                }

            }else if(input_text.length()>=10 && screen){

            } else {
                switch (btn){
                    case "e":
                        input_text.setText(input+Math.exp(1));
                        break;
                    case "PI":
                        input_text.setText(input+Math.PI);
                        break;
                    default:
                        input_text.setText(input+btn);
                        break;
                }
            }
        }else {
            switch (btn){
                case "e":
                    input_text.setText(Math.exp(1)+"");
                    break;
                case "PI":
                    input_text.setText(Math.PI+"");
                    break;
                default:
                    input_text.setText(btn);
                    break;
            }
            equal=false;
        }
    }

    public void onSymbolButtonClick(View view) {
        Log.d(TAG, "onSymbolButtonClick: ");
        onMusicClick(view);
        String input = input_text.getText().toString();
        String result = result_text.getText().toString();
        String btn = ((Button) findViewById(view.getId())).getText().toString();
        char op = input.charAt(input.length()-1);
        if(equal==false){
            if((input_text.length()>=10 || op=='/' || op=='*' || op=='-' || op=='+' || op=='%') && screen){

            } else {
                if (btn.matches("sin|cos|tan|%|x!|x\\^2|x\\^3|1\\/x|e\\^x|10\\^x|√|log|ln")){
                    onSpecialButtonClick(btn);
                }
                else {
                    input_text.setText(input + btn);
                }
            }
        }else {
            if(input_text.length()<8 && screen){
                if (btn.equals("%")){
                    input_text.setText(result);
                    onSpecialButtonClick(btn);
                }else {
                    input_text.setText(result + btn);
//                    input_text.setSelection(input_text.length());
                }
                equal=false;
            }else if (!screen){
                if (btn.matches("sin|cos|tan|%|x!|x2|x3|1x|ex|10x|√|log|ln")){
                    input_text.setText(result);
                    onSpecialButtonClick(btn);
                }else {
                    input_text.setText(result + btn);
//                    input_text.setSelection(input_text.length());
                }
                equal=false;
            }
        }

    }

    public void onBracketLeftButtonClick(View view){
        onMusicClick(view);
        String input = input_text.getText().toString();
        String result = result_text.getText().toString();
        char op = input.charAt(input.length()-1);
        if(input.equals("0")){
            input_text.setText(((Button) findViewById(view.getId())).getText());
        }else if (input_text.length()<8 && (op=='/' || op=='*' || op=='-' || op=='+') && !screen){
            input_text.setText(input + ((Button) findViewById(view.getId())).getText());
        }else {
        }

    }

    public void onBracketRightButtonClick(View view){
        onMusicClick(view);
        String input = input_text.getText().toString();
        String result = result_text.getText().toString();
        char op = input.charAt(input.length()-1);
        if(input_text.length()<=10 && op!='/' && op!='*' && op!='-' && op!='+' && !screen){
            input_text.setText(input + ((Button) findViewById(view.getId())).getText());
        }
//        input_text.setSelection(input_text.length());
    }

    public void onResultClick(View view){
        onMusicClick(view);
        String input = input_text.getText().toString();
        try {
            Calculator calculator = new Calculator(input);
            result_text.setText(calculator.getResult());
            equal=true;
        }catch (Exception e){
            result_text.setText("错误");
        }
//        input_text.setSelection(input_text.length());

    }

    public void onClearClick(View view){
        onMusicClick(view);
        input_text.setText("0");
        result_text.setText("0");
        equal=false;
//        input_text.setSelection(input_text.length());
    }

    public void onDeleteClick(View view){
        onMusicClick(view);
        String input = input_text.getText().toString();
        if (input.length()>1){
            input_text.setText(input.substring(0,input.length()-1));
        }else {
            input_text.setText("0");
        }
//        input_text.setSelection(input_text.length());

    }

    public void onSpecialButtonClick(String btn){
        String input = input_text.getText().toString();
        int index=0;
        for(int i=input.length()-1; i>=0; i--){
            if (input.charAt(i)=='/' || input.charAt(i)=='*' || input.charAt(i)=='+' || input.charAt(i)=='-' || input.charAt(i)=='('){
                index=i;
                break;
            }
        }
        Log.d(TAG, "onSpecialButtonClick: "+index);
        Log.d(TAG, "onSpecialButtonClick: "+input);
        String str;
        double afterNum;
        if(index+1==input.length() || index==0){
            str = "";
            double beforeNum = Double.valueOf(input.substring(index,input.length()));
            Log.d(TAG, "onSpecialButtonClick: "+beforeNum);
            afterNum = specialNumber(beforeNum,btn);
        }else {
            str = input.substring(0,index+1);
            double beforeNum = Double.valueOf(input.substring(index+1,input.length()));
            Log.d(TAG, "onSpecialButtonClick: "+beforeNum);
            afterNum = specialNumber(beforeNum,btn);
        }
        input_text.setText(str+afterNum);
//        input_text.setSelection(input_text.length());
    }

    public double specialNumber(double beforeNum, String btn){
        double afterNum;
        switch (btn){
            case "sin":
                afterNum = Math.sin(beforeNum/180*Math.PI);
                break;
            case "cos":
                afterNum = Math.cos(beforeNum/180*Math.PI);
                break;
            case "tan":
                afterNum = Math.tan(beforeNum/180*Math.PI);
                break;
            case "x!":
                double sum = 1;
                for (int i=1;i<=beforeNum;i++){
                    sum *= i;
                }
                afterNum = sum;
                break;
            case "x^2":
                afterNum = Math.pow(beforeNum,2);
                break;
            case "x^3":
                afterNum = Math.pow(beforeNum,3);
                break;
            case "1/x":
                afterNum = 1/beforeNum;
                break;
            case "e^x":
                afterNum = Math.exp(beforeNum);
                break;
            case "10^x":
                afterNum = Math.pow(10,beforeNum);
                break;
            case "√":
                afterNum = Math.sqrt(beforeNum);
                break;
            case "log":
                afterNum = Math.log(beforeNum)/Math.log(10);
                break;
            case "ln":
                afterNum = Math.log(beforeNum)/Math.log(Math.exp(1));
                break;
            case "%":
                afterNum = beforeNum/100;
                break;
            default:
                afterNum = 0;
                break;
        }
        return afterNum;
    }


    public void onMusicClick (View view){
        soundPool.play(music,1,1,0,0,1);
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
                        drawerLayout.closeDrawer(navigationView);
                        return true;
                    case R.id.binary:
                        Intent intent_bin = new Intent(MainActivity.this,BinaryActivity.class);
                        startActivity(intent_bin);
                        finish();
//                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.unit:
//                        showUnit();
                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.rate:
//                        showRate();
                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.cashbook:
//                        showCashbook();
                        Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return MainActivity.super.onOptionsItemSelected(item);
                }
            }
        });
    }


}