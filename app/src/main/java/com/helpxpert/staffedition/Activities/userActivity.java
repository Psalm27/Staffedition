package com.helpxpert.staffedition.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.helpxpert.staffedition.R;
import com.helpxpert.staffedition.adapters.userAdapter;
import com.helpxpert.staffedition.databinding.ActivityMainBinding;
import com.helpxpert.staffedition.databinding.ActivityUserBinding;
import com.helpxpert.staffedition.listeners.UserListener;
import com.helpxpert.staffedition.models.User;
import com.helpxpert.staffedition.utilities.Constants;
import com.helpxpert.staffedition.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class userActivity extends BaseActivity implements UserListener {

    private ActivityUserBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();

    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }

    private void getUsers(){
        isLoading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_FACILITY_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    isLoading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null){
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            userAdapter userAdapter = new userAdapter(users, this);
                            binding.usersRecylceView.setAdapter(userAdapter);
                            binding.usersRecylceView.setVisibility(View.VISIBLE);
                        } else{
                            showErrorMessage();
                        }
                    }else{
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "No Report Handler Available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void isLoading(Boolean loading){
        if(loading){
            binding.progressing.setVisibility(View.VISIBLE);
        } else {
            binding.progressing.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}