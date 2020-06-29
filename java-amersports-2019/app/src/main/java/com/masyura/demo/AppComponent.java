package com.masyura.demo;

import com.masyura.demo.viewmodel.MainViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainViewModel viewModel);
}
