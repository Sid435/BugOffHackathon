package com.example.e_commerce.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.activity.HomeScreenActivity;
import com.example.e_commerce.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class LogIn extends Fragment {

    final int RC_SIGN_IN = 1000;
    private TextView don_t_have_acc;
    private Button btnLogInFragment;
    private FirebaseAuth mAuth;

    private TextInputEditText emailTIETLI;
    private TextInputEditText passwordTIETLI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_log_in, container, false);
        initialize(view);

        don_t_have_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction(new SignUp());
            }
        });
        btnLogInFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                existingUserLogIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) SignInButton signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Toast.makeText(getContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getContext(),HomeScreenActivity.class);
//                startActivity(intent);

            } catch (ApiException e) {
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(getContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),HomeScreenActivity.class);
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initialize(View view){
        mAuth = FirebaseAuth.getInstance();
        don_t_have_acc=view.findViewById(R.id.don_t_have_acc);
        btnLogInFragment=view.findViewById(R.id.btnLogInFragment);
        emailTIETLI=view.findViewById(R.id.emailTIETLI);
        passwordTIETLI=view.findViewById(R.id.passwordTIETLI);
    }



    private void fragmentTransaction(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.pos_100_to_pos_0,R.anim.pos_0_to_neg_100);
        fragmentTransaction.replace(R.id.frameLayoutLogSignActivity,fragment);
        fragmentTransaction.commit();
    }

    private void existingUserLogIn(){
        final ProgressDialog dialog = new ProgressDialog(requireContext());
        dialog.setTitle("Verification");
        dialog.setMessage("Checking credentials in the database");
        dialog.show();
        String email= Objects.requireNonNull(emailTIETLI.getText()).toString().trim();
        String password= Objects.requireNonNull(passwordTIETLI.getText()).toString().trim();

        if(!email.equals("")&&!password.equals(""))
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Intent intent = new Intent(requireContext(), HomeScreenActivity.class);


                        FirebaseDatabase db= FirebaseDatabase.getInstance();
                        DatabaseReference node=db.getReference("Users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                        node.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().exists()){
                                        DataSnapshot dataSnapshot = task.getResult();
                                        String name= (String) dataSnapshot.child("name").getValue();
                                        String email= (String) dataSnapshot.child("email").getValue();
                                        String phone= (String) dataSnapshot.child("phone").getValue();
                                        String address= (String) dataSnapshot.child("address").getValue();
                                        dialog.dismiss();

                                        SharedPreferences sp = requireActivity().getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("NAME",name);
                                        editor.putString("EMAIL",email);
                                        editor.putString("PHONE",phone);
                                        editor.putString("ADDRESS",address);
                                        editor.apply();


                                        startActivity(intent);
                                        //Log.d("AddressOf",address);
                                        requireActivity().finish();
                                    }else {
                                        dialog.dismiss();
                                        Log.d("USERLOG","User does not exist");
                                        requireActivity().finish();
                                    }
                                }else{
                                    dialog.dismiss();
                                    Toast.makeText(requireContext(), "Problem", Toast.LENGTH_SHORT).show();
                                    requireActivity().finish();
                                }
                            }
                        });



                    } else {
                        // If sign in fails, display a message to the user.
                        dialog.dismiss();
                        Toast.makeText(requireContext(), "Failed to login", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        else {
            dialog.dismiss();
            Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }
}