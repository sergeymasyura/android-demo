package com.masyura.demo.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView which detects when scrolled to the end.
 * Once data are loaded @setLoaded needs to be called.
 */
public class LoadableRecyclerView extends RecyclerView {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = LoadableRecyclerView.class.getSimpleName();

    private OnScrolledToEndListener onScrolledToEndListener;
    private boolean isLoading = false;

    public interface OnScrolledToEndListener {
        /**
         * Called when a view has been scrolled to the end.
         */
        void onScrolledToEnd();
    }

    public LoadableRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LoadableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(@NonNull Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    final int visibleItemCount = layoutManager.getChildCount();
                    final int totalItemCount = layoutManager.getItemCount();
                    final int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoading = true;

                            if(onScrolledToEndListener != null) {
                                onScrolledToEndListener.onScrolledToEnd();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setOnScrolledToEndListener(@Nullable OnScrolledToEndListener listener) {
        this.onScrolledToEndListener = listener;
    }

    /**
     * Indicates new data has been loaded, so view can continue to detect events.
     */
    public void setLoaded() {
        isLoading = false;
    }
}
