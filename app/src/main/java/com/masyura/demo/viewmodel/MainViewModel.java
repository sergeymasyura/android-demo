package com.masyura.demo.viewmodel;

import android.util.Log;

import com.masyura.demo.App;
import com.masyura.demo.AppComponent;
import com.masyura.demo.BuildConfig;
import com.masyura.demo.R;
import com.masyura.demo.adapter.MainAdapter;
import com.masyura.demo.model.Commit;
import com.masyura.demo.provider.GitHubProvider;

import java.util.List;

import javax.inject.Inject;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    public ObservableBoolean isError;
    public ObservableBoolean isLoadingInitialData;
    public ObservableBoolean isLoadingMoreData;
    public ObservableBoolean isRefreshingData;
    public MutableLiveData<String> selectedCommitUri;


    @SuppressWarnings("WeakerAccess")
    @Inject
    GitHubProvider gitHubProvider;

    private MainAdapter adapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void init() {
        isError = new ObservableBoolean(false);
        isLoadingInitialData = new ObservableBoolean(true);
        isLoadingMoreData = new ObservableBoolean(false);
        isRefreshingData = new ObservableBoolean(false);
        selectedCommitUri = new MutableLiveData<>();

        AppComponent appComponent = App.getAppComponent();

        if (appComponent != null) {
            appComponent.inject(this);
        }

        adapter = new MainAdapter(R.layout.item_main, this);
    }

    public void loadData() {
        gitHubProvider.getFirstCommits(BuildConfig.GITHUB_PAGINATION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Commit>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onSuccess(List<Commit> commits) {
                        adapter.setItems(commits);
                        adapter.notifyDataSetChanged();
                        isLoadingInitialData.set(false);
                        isRefreshingData.set(false);
                        isError.set(false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(LOG_TAG, "Failed to get commits data", throwable);

                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        isLoadingInitialData.set(false);
                        isRefreshingData.set(false);
                        isError.set(true);
                    }
                });
    }

    public void loadMoreData() {

        int itemCount = adapter.getItemCount();
        Commit lastCommit = adapter.getItem(itemCount - 1);

        gitHubProvider.getCommitsAfter(lastCommit, BuildConfig.GITHUB_PAGINATION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Commit>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onSuccess(List<Commit> commits) {
                        adapter.addAll(commits);
                        adapter.notifyDataSetChanged();
                        isLoadingMoreData.set(false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e(LOG_TAG, "Failed to load more data", throwable);

                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        isLoadingMoreData.set(false);
                        isError.set(true);
                    }
                });
    }

    @SuppressWarnings("unused")
    public MainAdapter getAdapter() {
        return adapter;
    }

    public void onCommitRevisionClick(Integer index) {
        Commit commit = adapter.getItem(index);
        String commitUri = commit.getCommitUri();
        selectedCommitUri.setValue(commitUri);
        selectedCommitUri.setValue(null);
    }

    public Commit getCommitAt(int position) {
        return adapter.getItem(position);
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        super.onCleared();
    }
}
