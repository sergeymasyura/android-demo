package com.masyura.demo.provider;

import com.masyura.demo.model.Commit;

import java.util.List;

import io.reactivex.Single;

public interface GitHubProvider {

    Single<List<Commit>> getFirstCommits(int amount);

    Single<List<Commit>> getCommitsAfter(Commit commit, int limit);
}
