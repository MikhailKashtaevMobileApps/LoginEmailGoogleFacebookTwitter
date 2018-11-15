package com.example.mike.loginemailgooglefacebooktwiitter.ui;

import com.example.mike.loginemailgooglefacebooktwiitter.base.BasePresenter;
import com.example.mike.loginemailgooglefacebooktwiitter.base.BaseView;

public class MainContract {

    interface Presenter extends BasePresenter{
        void onAttach(View view);
        void onDetach();
    }
    interface View extends BaseView{}

}
