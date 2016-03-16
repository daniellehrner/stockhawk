package com.sam_chordas.android.stockhawk;

import com.sam_chordas.android.stockhawk.dependencyInjection.BasicComponent;
import com.sam_chordas.android.stockhawk.dependencyInjection.DaggerTestingComponent;
import com.sam_chordas.android.stockhawk.dependencyInjection.MockModule;

/**
 * Created by Daniel Lehrner
 */
public class MockApplication extends StockHawkApplication {
    @Override
    public BasicComponent createComponent() {
        return DaggerTestingComponent.builder()
                .mockModule(new MockModule())
                .build();
    }
}
