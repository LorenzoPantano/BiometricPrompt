package it.lorenzopantano.biometricprompt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private BiometricManager biometricManager;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    private static final String TAG = "BIOMETRICS TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(TAG, "onCreate: Biometric SUCCESS");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.d(TAG, "onCreate: Biometrics ERROR_HW_UNAVAILABLE");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.d(TAG, "onCreate: Biometric ERROR_NONE_ENROLLED");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.d(TAG, "onCreate: Biometric ERROR_NO_HARDWARE");
                break;
        }


        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            //Creates an executor on a different thread
            Executor executor = new Executor() {
                @Override
                public void execute(Runnable command) {
                    command.run();
                }
            };

            //Create and implement the methods of the abstract class AuthenticationCallback
            BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.e(TAG, "onAuthenticationError: " + errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Log.d(TAG, "onAuthenticationSucceeded: Authentication Succeeded");
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Log.d(TAG, "onAuthenticationFailed: Authentication Failed");
                }
            };

            //Instaciate the biometric prompt
            biometricPrompt = new BiometricPrompt(this, executor, authenticationCallback);

            //Create the dialog to be shown on the screen
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Authentication Required")
                    .setSubtitle("Log in")
                    .setDescription("Log in using your biometric credential")
                    .setDeviceCredentialAllowed(true)
                    .build();

            //Set up the button LOGIN and his onClickListener
            Button btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            biometricPrompt.authenticate(promptInfo);
        }
    }
}
