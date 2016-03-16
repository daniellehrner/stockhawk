package com.sam_chordas.android.stockhawk;

import android.app.Application;

import com.sam_chordas.android.stockhawk.dependencyInjection.AndroidModule;
import com.sam_chordas.android.stockhawk.dependencyInjection.ApplicationModule;
import com.sam_chordas.android.stockhawk.dependencyInjection.BasicComponent;
import com.sam_chordas.android.stockhawk.dependencyInjection.DaggerApplicationComponent;

/**
 * Created by Daniel Lehrner
 */
public class StockHawkApplication extends Application {
    private BasicComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = createComponent();
    }

    @SuppressWarnings("WeakerAccess")
    public BasicComponent createComponent() {
        return DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public BasicComponent getComponent() {
        return mComponent;
    }
}