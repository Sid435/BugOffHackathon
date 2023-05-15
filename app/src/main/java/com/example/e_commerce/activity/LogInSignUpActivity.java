package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.e_commerce.R;
import com.example.e_commerce.fragments.LogIn;
import com.example.e_commerce.fragments.SignUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInSignUpActivity extends AppCompatActivity {
    private FrameLayout frameLayoutLogSignActivity;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(this,HomeScreenActivity.class));
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_activity);
        initialize();
        fragmentTransaction(new SignUp());
    }
    private void initialize(){
        frameLayoutLogSignActivity=findViewById(R.id.frameLayoutLogSignActivity);
        mAuth=FirebaseAuth.getInstance();
    }
    private void fragmentTransaction(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutLogSignActivity,fragment);
        fragmentTransaction.commit();
    }
}