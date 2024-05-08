package com.helpxpert.staffedition.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpxpert.staffedition.R;
import com.helpxpert.staffedition.databinding.ActivitySignInBinding;
import com.helpxpert.staffedition.utilities.Constants;
import com.helpxpert.staffedition.utilities.PreferenceManager;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
            startActivity(intent);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){
        binding.createAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        binding.signInButton.setOnClickListener(v -> {
            if(isValidSignInDetails()){
                signIn();
            }
        });
        binding.forgotPassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));
    }

     private void signIn(){
        loading(true);
         FirebaseFirestore database = FirebaseFirestore.getInstance();
         database.collection(Constants.KEY_COLLECTION_USERS)
                 .whereEqualTo(Constants.KEY_EMAIL, binding.emailInput.getText().toString())
                 .whereEqualTo(Constants.KEY_PASSWORD, binding.passwordInput.getText().toString())
                 .get()
                 .addOnCompleteListener(task -> {
                     if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                         DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                         preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                         preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                         preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                         preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                         Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                     } else {
                         loading(false);
                         showToast("Unable to Sign In, Check Sign In Details");
                     }
                 });

     }

     private void loading(Boolean loading){
        if(loading){
            binding.signInButton.setVisibility(View.INVISIBLE);
            binding.signInProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.signInButton.setVisibility(View.VISIBLE);
            binding.signInProgressBar.setVisibility(View.INVISIBLE);
        }
     }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if (binding.emailInput.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.getText().toString()).matches()) {
            showToast("Enter a valid email");
            return false;
        } else if (binding.passwordInput.getText().toString().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else {
            return true;
        }
    }
}