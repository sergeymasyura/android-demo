package com.masyura.demo.provider;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.masyura.demo.GithubRepositoryCommitsAfterQuery;
import com.masyura.demo.GithubRepositoryCommitsFirstQuery;
import com.masyura.demo.model.Commit;
import com.masyura.demo.util.ApolloClientCreator;
import com.masyura.demo.util.CommitConverter;

import java.util.List;

import io.reactivex.Single;

public class GitHubGraphQlProvider implements GitHubProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = GitHubGraphQlProvider.class.getSimpleName();

    private final ApolloClient apolloClient;

    private final String repositoryOwner;
    private final String repositoryName;

    public GitHubGraphQlProvider(String serverUrl, String apiToken, String repositoryOwner, String repositoryName) {
        this.repositoryOwner = repositoryOwner;
        this.repositoryName = repositoryName;

        apolloClient = ApolloClientCreator.create(serverUrl, apiToken);
    }

    @Override
    public Single<List<Commit>> getFirstCommits(int limit) {

        GithubRepositoryCommitsFirstQuery query = GithubRepositoryCommitsFirstQuery.builder().
                repositoryOwner(repositoryOwner)
                .repositoryName(repositoryName)
                .limit(limit)
                .build();

        ApolloCall<GithubRepositoryCommitsFirstQuery.Data> apolloCall = apolloClient.query(query);

        return Single.fromObservable(Rx2Apollo
                .from(apolloCall)
                .map(dataResponse -> CommitConverter.fromResponseData(dataResponse.data())));
    }

    @Override
    public Single<List<Commit>> getCommitsAfter(Commit commit, int limit) {

        GithubRepositoryCommitsAfterQuery query = GithubRepositoryCommitsAfterQuery.builder()
                .repositoryOwner(repositoryOwner)
                .repositoryName(repositoryName)
                .limit(limit)
                .cursor(String.format("%s %s", commit.getRevision(), 0))
                .build();

        ApolloCall<GithubRepositoryCommitsAfterQuery.Data> apolloCall = apolloClient.query(query);

        return Single.fromObservable(Rx2Apollo
                .from(apolloCall)
                .map(dataResponse -> CommitConverter.fromResponseData(dataResponse.data())));
    }
}
