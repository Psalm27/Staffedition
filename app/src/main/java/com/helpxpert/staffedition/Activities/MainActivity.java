//package com.helpxpert.staffedition.Activities;
//
//import java.lang.String;
//import androidx.appcompat.app.AppCompatActivity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Base64;
//import android.widget.Toast;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.helpxpert.staffedition.databinding.ActivityMainBinding;
//import com.helpxpert.staffedition.utilities.Constants;
//import com.helpxpert.staffedition.utilities.PreferenceManager;
//
//public class MainActivity extends AppCompatActivity {
//
//    private ActivityMainBinding binding;
//    private PreferenceManager preferenceManager;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        loadUserDetails();
//        getToken();
//    }
//
//
//    private void showToast(String message){
//        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
//    }
//
//    private void getToken(){
//        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
//    }
//
//    private void updateToken(String token){
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
//        documentReference.update(Constants.KEY_FCM_TOKEN, token)
//                .addOnSuccessListener(unused -> showToast("Token Updated Successfully")).
//                addOnFailureListener(e -> showToast("Unable to Update Token"));
//    }
//
//    public void loadUserDetails(){
//        binding.homeUserName.setText(preferenceManager.getString(Constants.KEY_NAME));
//        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        binding.imgProfile.setImageBitmap(bitmap);
//    }
//}