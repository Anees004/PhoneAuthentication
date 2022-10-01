package com.example.phoneauthetication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.phoneauthetication.R;
import com.example.phoneauthetication.databinding.ActivityLoginBinding;
import com.example.phoneauthetication.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    Boolean valid;
    AuthViewModel authviewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authviewModel = ViewModelProviders.of(LoginActivity.this).get(AuthViewModel.class);

        ActivityLoginBinding bind = DataBindingUtil.setContentView(this, R.layout.activity_login);
        bind.countryCodePicker.registerCarrierNumberEditText(bind.editTextPhone);

        bind.countryCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    valid = true;
                } else {
                    valid = false;
                }

            }
        });



        authviewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
        bind.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (valid) {
                    String phone_number = bind.countryCodePicker.getFullNumberWithPlus();
                    Intent intent = new Intent(getApplicationContext(), AuthenticateActivity.class);
                    intent.putExtra("phonenumber", phone_number);
                    startActivity(intent);
                } else {
                    bind.editTextPhone.setError("phone number is not valid");
                    bind.editTextPhone.requestFocus();
                    return;
                }
            }
        });
    }

}