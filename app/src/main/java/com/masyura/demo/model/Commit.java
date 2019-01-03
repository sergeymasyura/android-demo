package com.masyura.demo.model;

import java.util.Date;

public class Commit {
    private String revision;
    private Date committedDate;
    private String messageHeadline;
    private String message;
    private String commitUri;
    private Author author;

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Date getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(Date committedDate) {
        this.committedDate = committedDate;
    }

    public String getMessageHeadline() {
        return messageHeadline;
    }

    public void setMessageHeadline(String messageHeadline) {
        this.messageHeadline = messageHeadline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommitUri() {
        return commitUri;
    }

    public void setCommitUri(String commitUri) {
        this.commitUri = commitUri;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
