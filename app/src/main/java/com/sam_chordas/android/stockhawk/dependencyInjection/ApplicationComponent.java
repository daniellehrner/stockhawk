package com.sam_chordas.android.stockhawk.dependencyInjection;

import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

import javax.inject.Singleton;

import dagger.Component;

@SuppressWarnings("unused")
@Singleton
@Component(modules = {
        AndroidModule.class,
        ApplicationModule.class
})
public interface ApplicationComponent {
    void inject(MyStocksActivity target);
    void inject(StockTaskService target);
}
