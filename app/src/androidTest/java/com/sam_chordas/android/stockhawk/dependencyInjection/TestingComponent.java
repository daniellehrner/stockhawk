package com.sam_chordas.android.stockhawk.dependencyInjection;

import com.sam_chordas.android.stockhawk.ui.LineGraphActivityTest;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivityTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { MockModule.class })
public interface TestingComponent extends BasicComponent {
    void inject(LineGraphActivityTest target);
    void inject(MyStocksActivityTest target);
}
