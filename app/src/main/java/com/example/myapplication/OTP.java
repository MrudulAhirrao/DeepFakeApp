package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class OTP extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button resendOtpButton, verifyButton;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        resendOtpButton = findViewById(R.id.resendOtpButton);
        verifyButton = findViewById(R.id.verifyButton);

        startResendTimer();

        resendOtpButton.setOnClickListener(v -> resendOtp());
        verifyButton.setOnClickListener(v -> checkIfEmailVerified());
    }

    private void startResendTimer() {
        resendOtpButton.setEnabled(false);
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendOtpButton.setText("Resend in " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                resendOtpButton.setText("Resend OTP");
                resendOtpButton.setEnabled(true);
            }
        }.start();
    }

    private void resendOtp() {
        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(OTP.this, "OTP resent to email!", Toast.LENGTH_SHORT).show();
                        startResendTimer();
                    } else {
                        Toast.makeText(OTP.this, "Error resending OTP: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfEmailVerified() {
        mAuth.getCurrentUser().reload()
                .addOnCompleteListener(task -> {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(OTP.this, "OTP Verified! Welcome!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(OTP.this, "OTP not verified yet. Please check your email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
