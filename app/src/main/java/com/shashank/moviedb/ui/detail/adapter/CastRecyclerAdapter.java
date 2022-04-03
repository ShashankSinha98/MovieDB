package com.shashank.moviedb.ui.detail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.model.Cast;
import com.shashank.moviedb.ui.detail.viewholder.CastViewHolder;
import com.shashank.moviedb.ui.trending.adapter.SearchExhaustedViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CastRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RequestManager requestManager;
    private List<Cast> castList = new ArrayList<>();

    public void setCast(List<Cast> casts) {
        this.castList = casts;
        notifyDataSetChanged();
    }

    public CastRecyclerAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_view_layout, parent, false);
        return new CastViewHolder(view, requestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CastViewHolder)holder).onBind(castList.get(position));
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }
}
