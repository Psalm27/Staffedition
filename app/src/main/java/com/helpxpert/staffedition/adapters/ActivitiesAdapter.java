package com.helpxpert.staffedition.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helpxpert.staffedition.databinding.ItemContainerActivitiesConversionBinding;
import com.helpxpert.staffedition.listeners.ConversionListener;
import com.helpxpert.staffedition.listeners.ReportListener;
import com.helpxpert.staffedition.models.History;
import com.helpxpert.staffedition.models.User;
import com.helpxpert.staffedition.utilities.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.UserViewHolder>{

    private final List<History> reportHistory;
    private final ReportListener reportListener;

    public ActivitiesAdapter(List<History> reportHistory, ReportListener reportListener) {
        this.reportHistory = reportHistory;
         this.reportListener = reportListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                ItemContainerActivitiesConversionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setData(reportHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return reportHistory.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        ItemContainerActivitiesConversionBinding binding;

        UserViewHolder(ItemContainerActivitiesConversionBinding itemContainerActivitiesConversionBinding){
            super(itemContainerActivitiesConversionBinding.getRoot());
            binding = itemContainerActivitiesConversionBinding;
        }

        void setData(History history){
            binding.imageProfile.setImageBitmap(getConversionImage(history.image));
            binding.textName.setText(history.name);
            binding.status.setText(history.status);
            binding.Myid.setText(history.id);
            binding.Reportid.setText(history.reportid);
            binding.timestamp.setText(history.dateTime);
            binding.getRoot().setOnClickListener(v ->{reportListener.onClickReport(history);
            });
        }
    }


    private Bitmap getConversionImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
