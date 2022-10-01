package com.example.phoneauthetication.model;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.phoneauthetication.MainActivity;
import com.example.phoneauthetication.view.AuthenticateActivity;
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

public class AuthenticationRepository {
    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedstatusMutableLive() {
        return userLoggedstatusMutableLive;
    }

    private MutableLiveData<Boolean> userLoggedstatusMutableLive;
    private FirebaseAuth auth;

    private String recieveCode;
    private Activity activity;
    public AuthenticationRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userLoggedstatusMutableLive = new MutableLiveData<>();
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
        }
    }

    public void verifyCode(String enteredCode) {
        Toast.makeText(application,"Before error",Toast.LENGTH_SHORT).show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(recieveCode, enteredCode);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(application,"After error",Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
//                    Intent intent = new Intent(application, MainActivity.class);
//                   application.startActivity(intent);
                } else
                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendVerificationCode(String phNumber) {

//        application.binding.progressbar.setVisibility(View.VISIBLE);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(AuthenticateActivity.activity) // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
         }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    recieveCode = s;
                    Toast.makeText(application, "Code sent", Toast.LENGTH_SHORT).show();
//                    activity.binding.progressbar.setVisibility(View.GONE);

                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
//                        binding.setEditTextCode(code);
                        verifyCode(code);

                    }

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(application, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
    public void signOut()
    {
        auth.signOut();
        userLoggedstatusMutableLive.postValue(true);
    }

}
