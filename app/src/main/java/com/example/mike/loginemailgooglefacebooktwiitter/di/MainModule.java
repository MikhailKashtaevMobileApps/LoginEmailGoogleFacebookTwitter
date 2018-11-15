package com.example.mike.loginemailgooglefacebooktwiitter.di;

import com.example.mike.loginemailgooglefacebooktwiitter.ui.MainPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    MainPresenter providesMainPresenter(){
        return new MainPresenter();
    }

}
