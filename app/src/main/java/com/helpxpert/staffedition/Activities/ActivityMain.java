package com.helpxpert.staffedition.Activities;

import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.helpxpert.staffedition.R;

import com.helpxpert.staffedition.adapters.ActivitiesAdapter;
import com.helpxpert.staffedition.databinding.ActivityMainBinding;
import com.helpxpert.staffedition.models.History;
import com.helpxpert.staffedition.utilities.Constants;
import com.helpxpert.staffedition.utilities.PreferenceManager;

public class ActivityMain extends BaseActivity implements  ReportListener {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private ListenerRegistration listenerRegistration;
    private ActivitiesAdapter activitiesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadReports();
        loadUserDetails();
        getToken();
        setListeners();

    }

    private void setListeners(){
        binding.makingReport.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), userActivity.class));
        });

        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.viewChat) {
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            }
            return true;
        });

        binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        });

        binding.homeUserName.setOnClickListener(v -> loadReports());

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to Update Token"));
    }

    public void loadUserDetails(){

        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imgProfile.setImageBitmap(bitmap);
    }


    private void loading(boolean isLoading){
        if(isLoading == true){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void loadReports(){
        loading(true);
        database = FirebaseFirestore.getInstance();
        listenerRegistration = database.collection(Constants.KEY_COLLECTION_HISTORY)
               .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener((value, error) -> {
                    loading(false);
                    if (error != null) {
                        return;
                    }
                    List<History> report = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : value) {

                        History history = new History();
                        history.name = queryDocumentSnapshot.getString(Constants.KEY_REPORTER);
                        history.image = queryDocumentSnapshot.getString(Constants.KEY_REPORTERIMAGE);
                        history.id = queryDocumentSnapshot.getString(Constants.KEY_RECEIVER_ID);
                        history.status = queryDocumentSnapshot.getString(Constants.KEY_STATUS);
                        history.reportid = queryDocumentSnapshot.getString(Constants.KEY_REPORT_ID);
                        history.dateTime = getReadableDateTime(queryDocumentSnapshot.getDate(Constants.KEY_TIMESTAMP));
                        report.add(history);
                    }
                    if (report.size() > 0) {
                        activitiesAdapter = new ActivitiesAdapter(report,this::onClickReport);
                        binding.activitiesRecyclerView.setAdapter(activitiesAdapter);
                        binding.activitiesRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                       showErrorMessage();
                    }
                });
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMM dd, yyy - hh:mm a", Locale.getDefault()).format(date);
    }

    public void showErrorMessage(){
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }


  @Override
   public void onClickReport(History history) {
        preferenceManager.putString(Constants.REPO_ID, history.reportid);
        Intent intent = new Intent(getApplicationContext(), ReportInformationActivity.class);
        startActivity(intent);
   }


}