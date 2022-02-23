package com.example.mysecretdialyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NumberPicker numberPicker1;
    private NumberPicker numberPicker2;
    private NumberPicker numberPicker3;

    private Button openButton;
    private Button changePasswordButton;
    private Boolean changePasswordMode = false;

    private String tempValue = "123";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        addEventListener();
    }

    private void initData() {
        numberPicker1 = findViewById(R.id.numberPicker1);
        numberPicker2 = findViewById(R.id.numberPicker2);
        numberPicker3 = findViewById(R.id.numberPicker3);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(9);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(9);
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(9);

        openButton = findViewById(R.id.openButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);

    }

    private void addEventListener() {

        openButton.setOnClickListener(v -> {

            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경중 입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 메서드 화 처리
            SharedPreferences passwordPreferences =
                    getSharedPreferences("password", Context.MODE_PRIVATE);
            String savedPwd = passwordPreferences.getString("password", "000");


            String passwordFormUser = "" + numberPicker1.getValue()
                    + numberPicker2.getValue()
                    + numberPicker3.getValue();


            if(savedPwd.equals(passwordFormUser)) {
                Intent intent = new Intent(this, DiaryActivity.class);
                startActivity(intent);
            } else {
                showErrorAlertDialog();
            }

        });

        changePasswordButton.setOnClickListener(v -> {

            // 메서드 처리
            SharedPreferences passwordPreferences =
                    getSharedPreferences("password", Context.MODE_PRIVATE);
            String savedPwd = passwordPreferences.getString("password", "000");


            // 1. 비밀번호 변경 모드로 코드를 작성
            String passwordFormUser = "" + numberPicker1.getValue()
                    + numberPicker2.getValue()
                    + numberPicker3.getValue();

            if(changePasswordMode) {
                // 번호를 저장하는 기능 -- > SharedPreferences
                tempValue = passwordFormUser;
                // 값을 쓰거나 수정할 때 알아야 하는 개념 (수정모드)
                SharedPreferences.Editor editor = passwordPreferences.edit(); // 수정 모드 만들기
                editor.putString("password", passwordFormUser);
                // 완료 - 1. (apply : 비동기 방식) , (commit UI를 멈추고 기다리는 방식 )
                editor.apply();

                changePasswordMode = false;
                changePasswordButton.setBackgroundColor(Color.WHITE);
                Toast.makeText(this, "변경완료", Toast.LENGTH_SHORT).show();
            } else {
                // 비밀번호가 맞는 체크하는 기능
                if(savedPwd.equals(passwordFormUser)) {
                    changePasswordButton.setBackgroundColor(Color.RED);
                    changePasswordMode = true;
                } else {
                    showErrorAlertDialog();
                }
            }
        });
    }

    private void showErrorAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("실패")
                .setMessage("비밀번호가 잘못되었습니다.")
                .setPositiveButton("닫기", (dialog, which) -> {
                    // 동작 정의하지 않음
                });
        builder.show();
    }


}