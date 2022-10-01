package com.example.phoneauthetication.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.phoneauthetication.MainActivity;
import com.example.phoneauthetication.R;
import com.example.phoneauthetication.databinding.ActivityAuthenticateBinding;
import com.example.phoneauthetication.viewmodel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class AuthenticateActivity extends AppCompatActivity {

    ActivityAuthenticateBinding binding;
    public static Activity activity;
    AuthViewModel authviewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authenticate);
        authviewModel= ViewModelProviders.of(AuthenticateActivity.this).get(AuthViewModel.class);
        String phoneNumber = getIntent().getStringExtra("phonenumber");
        authviewModel.register(phoneNumber);

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredCode = binding.editTextCode.getText().toString().trim();
                if (enteredCode.isEmpty() || enteredCode.length() < 6) {
                    binding.editTextCode.setError("Enter correctly");
                    binding.editTextCode.requestFocus();
                    return;
                }
                authviewModel.verifyCode(enteredCode);
            }
        });
    }
}