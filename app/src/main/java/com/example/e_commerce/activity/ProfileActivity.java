package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.e_commerce.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private ImageView back_btnIV_AP;
    private TextInputEditText nameTIETPro;
    private TextInputEditText emailTIETPro;
    private TextInputEditText phoneTIETPro;
    private TextInputEditText addressTIETPro;
    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        nameTIETPro.setText(getSharedPreferences("CREDENTIALS",MODE_PRIVATE).getString("NAME","Arijit Singh"));
        emailTIETPro.setText(getSharedPreferences("CREDENTIALS",MODE_PRIVATE).getString("EMAIL","arijitsingh2003@gmail.com"));
        phoneTIETPro.setText(getSharedPreferences("CREDENTIALS",MODE_PRIVATE).getString("PHONE","6589745636"));
        addressTIETPro.setText(getSharedPreferences("CREDENTIALS",MODE_PRIVATE).getString("ADDRESS","Lak Pin: 514789"));


        back_btnIV_AP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("CREDENTIALS",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,LogInSignUpActivity.class));
                HomeScreenActivity.activity.finish();
                finish();

            }
        });
    }
    private void init(){
        back_btnIV_AP=findViewById(R.id.back_btnIV_AP);
        back_btnIV_AP=findViewById(R.id.back_btnIV_AP);
        nameTIETPro=findViewById(R.id.nameTIETPro);
        emailTIETPro=findViewById(R.id.emailTIETPro);
        phoneTIETPro=findViewById(R.id.phoneTIETPro);
        addressTIETPro=findViewById(R.id.addressTIETPro);
        btnLogOut=findViewById(R.id.btnLogOut);
    }

}