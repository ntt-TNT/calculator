package com.example.myapplication;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView input_text;
    private TextView result_text;
    private boolean equal=false;

    //声音
    private SoundPool soundPool;
    private int music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_text = findViewById(R.id.input_text);
        result_text = findViewById(R.id.result_text);

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        music = soundPool.load(this,R.raw.music,1);
    }


    public void onNumberButtonClick(View view){
        onMusicClick(view);
        String input = input_text.getText().toString();
        String result = result_text.getText().toString();
        String btn = ((Button) findViewById(view.getId())).getText().toString();
        if (input.charAt(0)=='0' && !(input.contains(".") || input.contains("/") || input.contains("*")
                || input.contains("-") || input.contains("+") || input.contains("%")) || result.equals("错误")){
            input_text.setText(((Button) findViewById(view.getId())).getText());
        }else if(input_text.length()>=10){

        } else {
            input_text.setText(input + ((Button) findViewById(view.getId())).getText());
        }
    }

    public void onSymbolButtonClick(View view) {
        onMusicClick(view);
        String input = input_text.getText().toString();
        String result = result_text.getText().toString();
        char op = input.charAt(input.length()-1);
        if(equal==false){
            if(input_text.length()>=10 || op=='/' || op=='*' || op=='-' || op=='+' || op=='%' || op=='('){

            } else {
                if (((Button) findViewById(view.getId())).getText().equals("%")){
                    onPrecentClick();
                }
                else {
                    input_text.setText(input + ((Button) findViewById(view.getId())).getText());
                }
            }
        }else {
            if(input_text.length()<8){
                input_text.setText(result + ((Button) findViewById(view.getId())).getText());
            }
            equal=false;
        }

    }

    public void onBracketLeftButtonClick(View view){
        onMusicClick(view);
        String input = input_text.getText().toString();
        String result = result_text.getText().toString();
        char op = input.charAt(input.length()-1);
        if(input_text.length()<8 && (op=='/' || op=='*' || op=='-' || op=='+')){
            input_text.setText(input + ((Button) findViewById(view.getId())).getText());
        }else {
            input_text.setText(((Button) findViewById(view.getId())).getText());
        }
    }

    public void onBracketRightButtonClick(View view){
        onMusicClick(view);
        String input = input_text.getText().toString();
        String result = result_text.getText().toString();
        char op = input.charAt(input.length()-1);
        if(input_text.length()<=10 && op!='/' && op!='*' && op!='-' && op!='+'){
            input_text.setText(input + ((Button) findViewById(view.getId())).getText());
        }
    }

    public void onResultClick(View view){
        onMusicClick(view);
        try {
            String input = input_text.getText().toString();
            Calculator calculator = new Calculator(input);
            result_text.setText(calculator.getResult());
            equal=true;
        }catch (Exception e){
            result_text.setText("错误");
        }

    }

    public void onClearClick(View view){
        onMusicClick(view);
        input_text.setText("0");
        result_text.setText("0");
        equal=false;
    }

    public void onDeleteClick(View view){
        onMusicClick(view);
        String input = input_text.getText().toString();
        if (input.length()>1){
            input_text.setText(input.substring(0,input.length()-1));
        }else {
            input_text.setText("0");
        }

    }

    public void onPrecentClick(){
        String input = input_text.getText().toString();
        int index=0;
        for(int i=input.length()-1; i>=0; i--){
            if (input.charAt(i)=='/' || input.charAt(i)=='*' || input.charAt(i)=='+' || input.charAt(i)=='-'){
                index=i;
                break;
            }
        }
        double precent;
        String str;
        if(index==0){
            str = "";
            precent = Double.valueOf(input.substring(index,input.length()))/100;
        }else {
            str = input.substring(0,index+1);
            precent = Double.valueOf(input.substring(index+1,input.length()))/100;
        }
        input_text.setText(str+precent);
    }

    public void onMusicClick (View view){
        soundPool.play(music,1,1,0,0,1);
    }

}