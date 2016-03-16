package com.sam_chordas.android.stockhawk.dependencyInjection;

import com.sam_chordas.android.stockhawk.rest.HistoricalDataClientImpl;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.ui.LineGraphActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by Daniel Lehrner
 */
public interface BasicComponent {
    void inject(MyStocksActivity target);
    void inject(LineGraphActivity target);
    void inject(StockTaskService target);
    void inject(HistoricalDataClientImpl target);
}
