package com.sam_chordas.android.stockhawk;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

/**
 * Created by Daniel Lehrner
 */
public class RobolectricGradleTestRunner extends RobolectricTestRunner {
    public RobolectricGradleTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String appAppPath = RobolectricGradleTestRunner.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();

        String manifestPath = appAppPath + "../../../../../src/main/AndroidManifest.xml";
        String resPath = appAppPath + "../../../../../src/main/res";
        String assetPath = appAppPath + "../../../../../src/main/assets";

        return createAppManifest(Fs.fileFromPath(manifestPath),
                Fs.fileFromPath(resPath),
                Fs.fileFromPath(assetPath),
                "com.sam_chordas.android.stockhawk");
    }
}