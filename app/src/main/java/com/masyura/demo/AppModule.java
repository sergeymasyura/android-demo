package com.masyura.demo;

import com.masyura.demo.provider.GitHubGraphQlProvider;
import com.masyura.demo.provider.GitHubProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    @Singleton
    GitHubProvider getGitHubProvider() {
        return new GitHubGraphQlProvider(BuildConfig.GITHUB_API_URL,
                BuildConfig.GITHUB_API_TOKEN,
                BuildConfig.GITHUB_REPOSITORY_OWNER,
                BuildConfig.GITHUB_REPOSITORY_NAME);
    }
}
