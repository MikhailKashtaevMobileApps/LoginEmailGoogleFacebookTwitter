package com.example.mike.loginemailgooglefacebooktwiitter.di;

import com.example.mike.loginemailgooglefacebooktwiitter.ui.MainActivity;

import javax.inject.Inject;

import dagger.Component;

@Component(modules=MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
