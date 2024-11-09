package com.example.teamproject_dalendar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
// import com.google.firebase. ㅗㅗ

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MyInfo extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText edit_nickname, edit_introduction; // 닉네임, 한줄소개 수정
    private Button btnLogin;
    private ImageButton imgBtnBackPress; // 뒤로가기
    private ImageView image_profile; // 프로필 사진
    private Uri myImageUri;
    //private UploadTask uploadTask; // 어떻게 해야댐;;;;
    //private StorageReference storageReference; // ;;;;;;;;;;;;;;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
    }

    private void initImageViewProfile() {

    }


}