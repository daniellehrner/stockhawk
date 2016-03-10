package com.sam_chordas.android.stockhawk;


import android.app.Application;

import com.sam_chordas.android.stockhawk.dependencyInjection.AndroidModule;
import com.sam_chordas.android.stockhawk.dependencyInjection.ApplicationComponent;
import com.sam_chordas.android.stockhawk.dependencyInjection.ApplicationModule;
import com.sam_chordas.android.stockhawk.dependencyInjection.DaggerApplicationComponent;

public class StockHawkApplication extends Application {
    private static ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = createComponent();
    }

    @SuppressWarnings("WeakerAccess")
    public ApplicationComponent createComponent() {
        return DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .applicationModule(new ApplicationModule())
                .build();
    }

    public static ApplicationComponent getComponent() {
        return mComponent;
    }
}