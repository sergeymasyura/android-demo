package com.masyura.demo.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.masyura.demo.BR;
import com.masyura.demo.model.Commit;
import com.masyura.demo.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = MainAdapter.class.getSimpleName();

    private int layoutId;
    private MainViewModel viewModel;
    private List<Commit> items = new ArrayList<>();

    public MainAdapter(@LayoutRes int layoutId, MainViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Commit getItem(int position) {
        return items.get(position);
    }

    public void setItems(@NotNull List<Commit> items) {
        this.items = new ArrayList<>(items);
    }

    public void clear() {
        items.clear();
    }

    public void addAll(List<Commit> newItems) {
        items.addAll(newItems);
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        final ViewDataBinding binding;

        MainViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MainViewModel viewModel, Integer position) {
            binding.setVariable(BR.viewModel, viewModel);
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
        }
    }
}
