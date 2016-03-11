package com.sam_chordas.android.stockhawk.dependencyInjection;

import com.sam_chordas.android.stockhawk.rest.HistoricalDataClient;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@SuppressWarnings("unused")
@Module
public class ApplicationModule {
    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Provides
    @Singleton
    public HistoricalDataClient provideHistoricalDataClient() {
        return new HistoricalDataClient();
    }
}
