package com.Arya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Phonefairbase extends AppCompatActivity {
    EditText phone,otp;
    Button  generateotp,varifyotp;
    FirebaseAuth mAuth;
    String verificatinID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonefairbase);
        phone=(EditText) findViewById(R.id.number);
        otp=(EditText) findViewById(R.id.otp);
        generateotp=(Button) findViewById(R.id.numbutton);
        varifyotp=(Button) findViewById(R.id.otpbutton);
        mAuth = FirebaseAuth.getInstance();

        generateotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(phone.getText().toString()))
                {
                    Toast.makeText(Phonefairbase.this, "Enter Valid No.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String number=phone.getText().toString();
                    sendverificationcode(number);
                }

            }
        });
        varifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(otp.getText().toString()))
                {
                    Toast.makeText(Phonefairbase.this, "Rong OTp Enterd", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifycode(otp.getText().toString());
                }
            }
        });
    }
    private void sendverificationcode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+(1"+number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
   private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted( @NonNull PhoneAuthCredential credential) {
          final  String code=credential.getSmsCode();
             if (code!=null){
                 verifycode(code);
             }
        }

        @Override
        public void onVerificationFailed( @NonNull FirebaseException e) {

            Toast.makeText(Phonefairbase.this, "Varification ", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onCodeSent(@NonNull String s,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
          super.onCodeSent(s,token);
          verificatinID=s;
            Toast.makeText(Phonefairbase.this, "code sent", Toast.LENGTH_SHORT).show();

        }
    };


    private void verifycode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificatinID,code);
         signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
      firebaseAuth.signInWithCredential(credential)
              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful())
                  {
                      Toast.makeText(Phonefairbase.this, "Login Sucessfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Phonefairbase.this,HomeActivity.class));
                  }
                  }
              });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser  currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            startActivity(new Intent(Phonefairbase.this,HomeActivity.class));
        finish();
        }
    }
}