package com.sam_chordas.android.stockhawk.dependencyInjection;

import com.sam_chordas.android.stockhawk.rest.HistoricalDataClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daniel Lehrner
 */
@Module
public class MockModule {
    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Provides
    @Singleton
    public HistoricalDataClient provideHistoricalDataClient() {
        return Mockito.mock(HistoricalDataClient.class);
    }

    @Provides
    public OkHttpClient providesClient() {
//        return Mockito.mock(OkHttpClient.class);
        return new OkHttpClient();
    }
}
