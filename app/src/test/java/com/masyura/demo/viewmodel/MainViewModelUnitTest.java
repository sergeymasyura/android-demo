package com.masyura.demo.viewmodel;

import com.masyura.demo.model.Commit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import static org.mockito.Mockito.verify;


public class MainViewModelUnitTest {

    private static final String REVISION_0 = "c0b701b2415791cbd89c857e09b391332c7fce01";
    private static final String REVISION_1 = "22f4c322d63d19d392be689422be9e3e9c473410";

    private static final String COMMIT_URI_0 = "https://github.com/aosp-mirror/platform_build/commit/c0b701b2415791cbd89c857e09b391332c7fce01";
    private static final String COMMIT_URI_1 = "https://github.com/aosp-mirror/platform_build/commit/22f4c322d63d19d392be689422be9e3e9c473410";

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    private Observer<String> uriToOpenObserver;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_onCommitRevisionClick() {

        MainViewModel viewModel = new MainViewModel();
        viewModel.init();

        viewModel.getSelectedCommitUri().observeForever(uriToOpenObserver);

        List<Commit> commits = makeCommits();
        viewModel.getAdapter().setItems(commits);

        viewModel.onCommitRevisionClick(1);

        verify(uriToOpenObserver).onChanged(COMMIT_URI_1);
    }

    private static List<Commit> makeCommits() {
        List<Commit> commits = new ArrayList<>();
        commits.add(makeCommit(REVISION_0, COMMIT_URI_0));
        commits.add(makeCommit(REVISION_1, COMMIT_URI_1));

        return commits;
    }

    private static Commit makeCommit(String revision, String commitUri) {
        Commit commit = new Commit();
        commit.setRevision(revision);
        commit.setCommitUri(commitUri);
        return commit;
    }
}