package com.helpxpert.staffedition.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.helpxpert.staffedition.databinding.ActivityReportInformationBinding;
import com.helpxpert.staffedition.databinding.ItemContainerActivitiesConversionBinding;
import com.helpxpert.staffedition.models.Form;
import com.helpxpert.staffedition.models.History;
import com.helpxpert.staffedition.utilities.Constants;
import com.helpxpert.staffedition.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ReportInformationActivity extends AppCompatActivity {

    private ActivityReportInformationBinding binding;
    private ItemContainerActivitiesConversionBinding bind;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;
    private History reportHistory;
    private Form form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportInformationBinding.inflate(getLayoutInflater());
        bind = ItemContainerActivitiesConversionBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        reportHistory = new History();
        form = new Form();

        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());



        displayReportInfo();
        setListeners();
    }

    public void setListeners(){

        binding.back.setOnClickListener(v -> onBackPressed());

    }


    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMM dd, yyy - hh:mm a", Locale.getDefault()).format(date);
    }

    public void displayReportInfo(){
        loading(true);
        database.collection(Constants.KEY_COLLECTION_HISTORY)
                .whereEqualTo(Constants.KEY_REPORT_ID, preferenceManager.getString(Constants.REPO_ID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null &&
                            task.getResult().getDocuments().size() > 0) {

                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        binding.reportId.setText(document.getString(Constants.KEY_REPORT_ID));
                        binding.description.setText(document.getString(Constants.KEY_DESCRIPTION));
                        binding.handlerName.setText(document.getString(Constants.KEY_HANDLERNAME));
                        binding.timeStamp.setText(getReadableDateTime(document.getDate(Constants.KEY_TIMESTAMP)));
                        binding.status.setText(document.getString(Constants.KEY_STATUS));
                        loading(false);





                    } else {
                        // Handle the error
                    }
                });
    }

    private void loading(boolean isLoading){
        if (isLoading == true){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

}