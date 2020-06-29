package com.masyura.demo.util;

import android.net.Uri;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.masyura.demo.widget.LoadableRecyclerView;

import java.util.Date;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BindingAdapterUtil {
    @BindingAdapter({"isVisible"})
    public static void setVisibility(View view, Boolean isVisible) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({"isRefreshing"})
    public static void setRefreshing(SwipeRefreshLayout layout, Boolean isRefreshing) {
        layout.setRefreshing(isRefreshing);
    }

    @BindingAdapter("setAdapter")
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("setLoaded")
    public static void setLoaded(LoadableRecyclerView recyclerView, Boolean isLoadingMoreData) {
        if (!isLoadingMoreData) {
            recyclerView.setLoaded();
        }
    }

    @BindingAdapter("formatRevision")
    public static void formatRevision(TextView textView, String revision) {
        textView.setText(revision.substring(0, 6));
    }

    @BindingAdapter("formatDate")
    public static void formatDate(TextView textView, Date date) {
        textView.setText(DateFormat.format("dd MMM yyyy", date));
    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
