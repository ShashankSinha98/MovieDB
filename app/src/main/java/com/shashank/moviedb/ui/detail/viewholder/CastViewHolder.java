package com.shashank.moviedb.ui.detail.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.model.Cast;
import com.shashank.moviedb.util.Constants;

public class CastViewHolder extends RecyclerView.ViewHolder {

    private AppCompatImageView ivCastImage;
    private TextView tvCastName;
    private RequestManager requestManager;

    public CastViewHolder(@NonNull View itemView, RequestManager requestManager) {
        super(itemView);
        this.requestManager = requestManager;
        ivCastImage = itemView.findViewById(R.id.ivActor);
        tvCastName = itemView.findViewById(R.id.tvActorValue);
    }

    public void onBind(Cast cast) {

        if(cast.getProfilePath()!=null) {
            requestManager.load(Constants.BASE_IMAGE_URL_w500_API+cast.getProfilePath())
                    .into(ivCastImage);
        }

        tvCastName.setText(cast.getName());
    }
}
