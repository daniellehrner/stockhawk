package com.sam_chordas.android.stockhawk.rest;

import com.db.chart.model.LineSet;

public interface HistoricalDataClient {
    LineSet getData(String symbol, int mDurationInDays);
}
