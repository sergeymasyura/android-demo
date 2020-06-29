package com.masyura.demo.util;

import com.masyura.demo.GithubRepositoryCommitsAfterQuery;
import com.masyura.demo.GithubRepositoryCommitsFirstQuery;
import com.masyura.demo.model.Author;
import com.masyura.demo.model.Commit;

import java.util.ArrayList;
import java.util.List;

public class CommitConverter {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = CommitConverter.class.getSimpleName();

    public static List<Commit> fromResponseData(GithubRepositoryCommitsFirstQuery.Data data) {
        List<Commit> commits = new ArrayList<>();

        GithubRepositoryCommitsFirstQuery.DefaultBranchRef defaultBranchRef = data.repository().defaultBranchRef();
        GithubRepositoryCommitsFirstQuery.AsCommit asCommit = (GithubRepositoryCommitsFirstQuery.AsCommit) defaultBranchRef.target();

        for (GithubRepositoryCommitsFirstQuery.Edge edge : asCommit.history().edges()) {
            GithubRepositoryCommitsFirstQuery.Node node = edge.node();

            Author author = new Author();
            author.setName(node.author().name());
            author.setEmail(node.author().email());
            author.setAvatarUri(node.author().avatarUrl());

            Commit commit = new Commit();
            commit.setRevision(node.oid());
            commit.setCommittedDate(node.committedDate());
            commit.setMessageHeadline(node.messageHeadline());
            commit.setMessage(node.message());
            commit.setCommitUri(node.commitUrl());
            commit.setAuthor(author);

            commits.add(commit);
        }

        return commits;
    }


    public static List<Commit> fromResponseData(GithubRepositoryCommitsAfterQuery.Data data) {
        List<Commit> commits = new ArrayList<>();

        GithubRepositoryCommitsAfterQuery.DefaultBranchRef defaultBranchRef = data.repository().defaultBranchRef();
        GithubRepositoryCommitsAfterQuery.AsCommit asCommit = (GithubRepositoryCommitsAfterQuery.AsCommit) defaultBranchRef.target();

        for (GithubRepositoryCommitsAfterQuery.Edge edge : asCommit.history().edges()) {
            GithubRepositoryCommitsAfterQuery.Node node = edge.node();

            Author author = new Author();
            author.setName(node.author().name());
            author.setEmail(node.author().email());
            author.setAvatarUri(node.author().avatarUrl());

            Commit commit = new Commit();
            commit.setRevision(node.oid());
            commit.setCommittedDate(node.committedDate());
            commit.setMessageHeadline(node.messageHeadline());
            commit.setMessage(node.message());
            commit.setCommitUri(node.commitUrl());
            commit.setAuthor(author);

            commits.add(commit);
        }

        return commits;
    }
}
