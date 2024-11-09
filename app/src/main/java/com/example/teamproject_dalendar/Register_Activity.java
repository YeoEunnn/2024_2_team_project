package com.example.teamproject_dalendar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Register_Activity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText editNickname, editId, editPwd;
    private Button reg_btn;
    private Spinner ageSpinner;
    private ImageButton backPressBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        editNickname = findViewById(R.id.inRegAct_user_Nickname);
        editId = findViewById(R.id.inRegAct_user_id);
        editPwd = findViewById(R.id.inLogAct_email);
        reg_btn = findViewById(R.id.inRegAct_regBtn);
        backPressBtn = findViewById(R.id.inRegBackPress);
        ageSpinner = findViewById(R.id.age_spinner);

        List<String> ageList = new ArrayList<>();
        ageList.add("선택");
        for(int age = 12 ; age< 60; age++ ){
            ageList.add(String.valueOf(age));
        }
        //연동
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ageList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 기본적인 View를 가져옴
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                // 폰트 설정: sans-serif-black
                Typeface typeface = Typeface.create("sans-serif-black", Typeface.NORMAL);
                textView.setTypeface(typeface);

                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // 드롭다운 항목의 View를 가져옴
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                // 드롭다운 항목 폰트 설정: sans-serif-black
                Typeface typeface = Typeface.create("sans-serif-black", Typeface.NORMAL);
                textView.setTypeface(typeface);

                return view;
            }
        };

        // Spinner에 Adapter 설정
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);

        //back버튼 코드
        backPressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //버튼 코드
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 공백 허용은 안 할 거지만, 일단 trim 쓰긴 했음
                String userEmail = editId.getText().toString().trim();
                String userPwd = editPwd.getText().toString().trim();

                //Firebase 인증 ㄱㄱ
                mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPwd).addOnCompleteListener(Register_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Register_Activity.this, com.example.teamproject_dalendar.MainLoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Register_Activity.this, "문제가 발생하였습니다", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }
        });



    }
}