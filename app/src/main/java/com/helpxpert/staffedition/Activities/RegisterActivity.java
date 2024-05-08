package com.helpxpert.staffedition.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.*;
import android.view.View;
import android.widget.Toast;
import java.io.*;
import java.util.HashMap;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpxpert.staffedition.databinding.ActivityRegisterBinding;
import com.helpxpert.staffedition.utilities.Constants;
import com.helpxpert.staffedition.utilities.PreferenceManager;


public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners(){
        binding.signInCreate.setOnClickListener(v -> onBackPressed());
        binding.registerButton.setOnClickListener(v -> {
            if(isValidRegisterDetails()){
                register();
            }
        });
        binding.imageLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void register(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.nameInput.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.emailInput.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.passwordInput.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        user.put(Constants.KEY_SECRETPHRASE, binding.textSecretPhrase.getText().toString());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true );
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.nameInput.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    private String encodedImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() *  previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_OK){
            if(result.getData() != null){
                Uri imageUri = result.getData().getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.profileImage.setImageBitmap(bitmap);
                    binding.addImageText.setVisibility(View.GONE);
                    encodedImage = encodedImage(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } {

                }
            }
        }
    });

    private Boolean isValidRegisterDetails(){
        if(encodedImage == null){
            showToast("Select a Profile Image");
            return false;
        }else if(binding.nameInput.getText().toString().trim().isEmpty()){
            showToast("Enter Name");
            return false;
        }else if(binding.emailInput.getText().toString().trim().isEmpty()){
            showToast("Enter Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailInput.getText().toString()).matches()){
            showToast("Enter a valid email");
            return false;
        } else if (binding.passwordInput.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (binding.passwordInputConfirm.getText().toString().trim().isEmpty()) {
            showToast("Confirm Password");
            return false;
        } else if (!binding.passwordInput.getText().toString().equals(binding.passwordInputConfirm.getText().toString())) {
            showToast("Passwords do not match");
            return false;
        }else if(binding.textSecretPhrase.getText().toString().trim().isEmpty()) {
            showToast("Enter Secret Phrase");
            return false;
        }
        else return true;
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.registerButton.setVisibility(View.INVISIBLE);
            binding.registerProgressBar.setVisibility(View.VISIBLE);
        } else{
            binding.registerProgressBar.setVisibility(View.INVISIBLE);
            binding.registerButton.setVisibility(View.VISIBLE);
        }
    }
}