package com.helpxpert.staffedition.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;

import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpxpert.staffedition.databinding.ActivitySettingsBinding;
import com.helpxpert.staffedition.utilities.Constants;
import com.helpxpert.staffedition.utilities.PreferenceManager;


import java.util.HashMap;


public class SettingsActivity extends AppCompatActivity {


    private ActivitySettingsBinding binding;
    private PreferenceManager preferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());


        boolean isNotificationOn = preferenceManager.getBoolean(Constants.NOTIFICATION_VALUE);
        binding.notification.setChecked(isNotificationOn);


        setContentView(binding.getRoot());
        onSwitchClick();
        setListeners();

    }

    public void onSwitchClick(){
        binding.notification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.putBoolean(Constants.NOTIFICATION_VALUE, isChecked);

            if (isChecked){
                Toast.makeText(getApplicationContext(), "Notification is On", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getApplicationContext(), "Notification is Off", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void setListeners() {

        binding.logoutButton.setOnClickListener(v -> signOut());
        binding.UpdateButton.setOnClickListener(v -> updateCredentials());
        binding.textName.setOnClickListener(v -> onBackPressed());

    }


    private void updateCredentials(){
        FirebaseFirestore database  = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        if (!TextUtils.isEmpty(binding.UpdateEmail.getText().toString())){
            documentReference.update(Constants.KEY_EMAIL, binding.UpdateEmail.getText().toString())
                    .addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(),
                            "Email has been updated", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                            "Unable to update Email", Toast.LENGTH_SHORT).show());}


        if(!TextUtils.isEmpty(binding.UpdatePassword.getText().toString())){
            documentReference.update(Constants.KEY_PASSWORD, binding.UpdatePassword.getText().toString())
                    .addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(),
                            "Password has been updated", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                            "Unable to update Password", Toast.LENGTH_SHORT).show());}
    }


    private void signOut(){
        Toast.makeText(getApplicationContext(), "SIGNING OUT..", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })

                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                        "Unable to sign out", Toast.LENGTH_SHORT).show());
    }



}