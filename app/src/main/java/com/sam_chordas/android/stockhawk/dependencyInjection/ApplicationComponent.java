package com.sam_chordas.android.stockhawk.dependencyInjection;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Daniel Lehrner
 */
@SuppressWarnings("unused")
@Singleton
@Component(modules = {
        AndroidModule.class,
        ApplicationModule.class
})
public interface ApplicationComponent extends BasicComponent {

}
