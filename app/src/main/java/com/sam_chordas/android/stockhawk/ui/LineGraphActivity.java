package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.CubicEase;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StockHawkApplication;
import com.sam_chordas.android.stockhawk.rest.HistoricalDataClient;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LineGraphActivity extends AppCompatActivity {
    public static String KEY_SYM = "LineGraphActivity.KEY_SYM";
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Bind(R.id.loader) ProgressBar mProgressBar;
    @Bind(R.id.linechart) LineChartView mLineChartView;

    private Subscription mLineChartSubscription;
    @Inject
    HistoricalDataClient mRestClient;
    private int mDurationInDays = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        ButterKnife.bind(this);

        ((StockHawkApplication)getApplication()).getComponent().inject(this);

        mLineChartView.setAxisColor(Color.WHITE)
                .setLabelsColor(Color.WHITE);

        Intent intent = getIntent();
        String symbol = intent.getStringExtra(KEY_SYM);
        createObservable(symbol);
    }

    private void createObservable(final String symbol) {
        Observable<LineSet> historicalDataObservable = Observable.fromCallable(new Callable<LineSet>() {
            @Override
            public LineSet call() {
                Log.d("LineGraphActivity", "call");
                return mRestClient.getData(symbol, mDurationInDays);
            }
        });

        mLineChartSubscription = historicalDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<LineSet>() {
                            @Override
                            public void onCompleted() {
                                Log.d("Subscription", "Completed");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("Subscription", "Error:" + e.toString());
                            }

                            @Override
                            public void onNext(LineSet data) {
                                Log.d("Subscription", "Next");
                                updateGraph(data);
                            }
                        }
                );
    }

    private void updateGraph(LineSet data) {


        data.setColor(Color.YELLOW)
                .setDotsRadius(Tools.fromDpToPx(4))
                .setDotsStrokeThickness(Tools.fromDpToPx(3))
                .setDotsStrokeColor(Color.YELLOW)
                .setThickness(10);

        mLineChartView.setStep(20);

        mLineChartView.addData(data);

        Animation anim = new Animation()
                .setEasing(new CubicEase());

        StringBuilder descriptionBuilder = new StringBuilder();

        Log.d(LOG_TAG, "data size: " + data.size());

        for (int i = 0; i < data.size(); ++i) {
            descriptionBuilder.append(data.getLabel(i))
                    .append(": ")
                    .append(data.getValue(i));

            // add coma for all, but the last entry
            if (i < data.size() - 1) {
                descriptionBuilder.append(", ");
            }
        }

        Log.d(LOG_TAG, "description: " + descriptionBuilder.toString());
        mLineChartView.setContentDescription(descriptionBuilder.toString());

        mProgressBar.setVisibility(View.GONE);
        mLineChartView.setVisibility(View.VISIBLE);

        mLineChartView.show(anim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLineChartSubscription != null && !mLineChartSubscription.isUnsubscribed()) {
            mLineChartSubscription.unsubscribe();
        }
    }
}
