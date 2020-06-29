package com.masyura.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.masyura.demo.BuildConfig;
import com.masyura.demo.R;
import com.masyura.demo.databinding.ActivityMainBinding;
import com.masyura.demo.fragment.AboutDialog;
import com.masyura.demo.viewmodel.MainViewModel;
import com.masyura.demo.widget.LoadableRecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String title = getString(R.string.main_app_bar_title,
                BuildConfig.GITHUB_REPOSITORY_OWNER,
                BuildConfig.GITHUB_REPOSITORY_NAME);

        getSupportActionBar().setTitle(title);

        setupBindings(savedInstanceState);
    }

    private void setupBindings(Bundle savedInstanceState) {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        if (savedInstanceState == null) {
            viewModel.init();
        }

        binding.setModel(viewModel);

        LoadableRecyclerView recyclerView = findViewById(R.id.main_recycler_view);

        recyclerView.setOnScrolledToEndListener(() -> {
            viewModel.isLoadingMoreData.set(true);
            viewModel.loadMoreData();
        });

        SwipeRefreshLayout swipeContainer = findViewById(R.id.main_swipe_layout);
        swipeContainer.setColorSchemeResources(R.color.colorAccent);
        swipeContainer.setOnRefreshListener(() -> {
            viewModel.isRefreshingData.set(true);
            viewModel.loadData();
        });

        viewModel.getSelectedCommitUri().observe(this, uri -> {
            if (uri != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(browserIntent);
            }
        });

        if (savedInstanceState == null) {
            viewModel.loadData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about: {
                AboutDialog dialog = new AboutDialog();
                dialog.show(getSupportFragmentManager(), AboutDialog.TAG);
                break;
            }
        }

        return true;
    }
}
