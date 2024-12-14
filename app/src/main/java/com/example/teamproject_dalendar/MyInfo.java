package com.example.teamproject_dalendar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MyInfo extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProfile;
    private EditText editTextNickname, editTextIntro;
    private Button buttonUpload, buttonSave;

    private Uri imageUri;
    private FirebaseStorage storage;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        // Firebase 초기화
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI 초기화
        imageViewProfile = findViewById(R.id.ivProfilePic);
        editTextNickname = findViewById(R.id.NickName);
        editTextIntro = findViewById(R.id.writeAboutMe);
        buttonSave = findViewById(R.id.buttonSave);

        // 이미지 선택
        imageViewProfile.setOnClickListener(view -> openGallery());

        // 데이터 저장
        buttonSave.setOnClickListener(view -> saveUserData());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewProfile.setImageURI(imageUri);
        }
    }

    private void saveUserData() {
        String nickname = editTextNickname.getText().toString();
        String intro = editTextIntro.getText().toString();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(intro) || imageUri == null) {
            Toast.makeText(this, "모든 필드를 채워주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Storage에 이미지 업로드
        StorageReference storageRef = storage.getReference().child("profile_images/" + System.currentTimeMillis() + ".jpg");
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            // Firestore에 사용자 데이터 저장
                            Map<String, Object> user = new HashMap<>();
                            user.put("nickname", nickname);
                            user.put("intro", intro);
                            user.put("profileImage", uri.toString());

                            db.collection("users").add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(MyInfo.this, "저장 성공!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MyInfo.this, "저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(MyInfo.this, "이미지 업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}