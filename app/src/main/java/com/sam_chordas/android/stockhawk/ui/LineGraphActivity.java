package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.CubicEase;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StockHawkApplication;
import com.sam_chordas.android.stockhawk.rest.HistoricalDataClient;
import com.sam_chordas.android.stockhawk.rest.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class LineGraphActivity extends AppCompatActivity {
    public static String KEY_SYM = "LineGraphActivity.KEY_SYM";
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Bind(R.id.loader) ProgressBar mProgressBar;
    @Bind(R.id.linechart) LineChartView mLineChartView;
    @Bind(R.id.durationButtons) RelativeLayout mButtons;
    @Bind(R.id.button_week) Button mButtonWeek;
    @Bind(R.id.button_month) Button mButtonMonth;
    @Bind(R.id.button_two_weeks) Button mButtonTwoWeeks;

    private PublishSubject<Integer> mHistoricalDataSubject;
    private Subscription mHistoricalDataSubscription;
    private String mSymbol;
    private int mLastButtonPressedId;
    @Inject HistoricalDataClient mRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        ButterKnife.bind(this);

        ((StockHawkApplication)getApplication()).getComponent().inject(this);

        Intent intent = getIntent();
        mSymbol = intent.getStringExtra(KEY_SYM);

        mButtonTwoWeeks.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        mButtonMonth.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        createObservable();

        // set variable to a valid value
        mLastButtonPressedId = R.id.button_month;
        // always start with weekly overview
        buttonClick(mButtonWeek);
    }

    private void createObservable() {
        Log.d(LOG_TAG, "createObservable");
        mHistoricalDataSubject = PublishSubject.create();

        mHistoricalDataSubscription = mHistoricalDataSubject
                .observeOn(Schedulers.io())
                .map(new Func1<Integer, LineSet>() {
                    @Override
                    public LineSet call(Integer duration) {
                        Log.d(LOG_TAG, "map");
                        return mRestClient.getData(mSymbol, duration);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LineSet>() {

                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "Couldn't get update:" + e.toString());
                    }

                    @Override
                    public void onNext(LineSet data) {
                        Log.d(LOG_TAG, "onNext");
                        updateGraph(data);
                    }
        });
    }

    @OnClick({R.id.button_week,
              R.id.button_two_weeks,
              R.id.button_month})
    public void buttonClick(View button) {
        int buttonId = button.getId();
        Button lastButton;

        if (buttonId == mLastButtonPressedId) {
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mLineChartView.setVisibility(View.GONE);

        switch(mLastButtonPressedId) {
            case R.id.button_week:
                lastButton = mButtonWeek;
                break;
            case R.id.button_two_weeks:
                lastButton = mButtonTwoWeeks;
                break;
            case R.id.button_month:
                lastButton = mButtonMonth;
                break;
            default:
                Log.e(LOG_TAG, "Invalid mLastButtonPressedId: " + mLastButtonPressedId);
                return;
        }

        lastButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        lastButton.setTextColor(Color.WHITE);

        button.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        ((Button) button).setTextColor(Color.BLACK);
        mLastButtonPressedId = buttonId;

        switch (buttonId) {
            case R.id.button_week:
                mHistoricalDataSubject.onNext(7);
                break;
            case R.id.button_two_weeks:
                mHistoricalDataSubject.onNext(14);
                break;
            case R.id.button_month:
                mHistoricalDataSubject.onNext(30);
                break;
            default:
                Log.e(LOG_TAG, "Invalid buttonId: " + mLastButtonPressedId);
        }
    }

    private void updateGraph(LineSet data) {
        mLineChartView.reset();

        mLineChartView.setAxisColor(Color.WHITE)
                .setLabelsColor(Color.WHITE);

        data.setColor(Color.YELLOW)
                .setDotsRadius(Tools.fromDpToPx(4))
                .setDotsStrokeThickness(Tools.fromDpToPx(3))
                .setDotsStrokeColor(Color.YELLOW)
                .setThickness(10);

        try {
            mLineChartView.addData(data);
        }
        catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, "Can't add data: " + e.toString());
            return;
        }

        Animation anim = new Animation()
                .setEasing(new CubicEase());

        StringBuilder descriptionBuilder = new StringBuilder();

        Log.d(LOG_TAG, "data size: " + data.size());

        float minValue = Float.MAX_VALUE, maxValue = Float.MIN_VALUE, value;

        for (int i = 0; i < data.size(); ++i) {
            value = data.getValue(i);

            minValue = min(minValue, value);
            maxValue = max(maxValue, value);

            descriptionBuilder.append(data.getLabel(i))
                    .append(": ")
                    .append(value);

            // add coma for all, but the last entry
            if (i < data.size() - 1) {
                descriptionBuilder.append(", ");
            }
        }

        mLineChartView.setContentDescription(descriptionBuilder.toString());


        int maxRoundUp = Utils.roundToNextTen((int) maxValue, true);
        int minRoundDown = Utils.roundToNextTen((int) minValue, false);

        mLineChartView.setAxisBorderValues(minRoundDown,
                maxRoundUp,
                (maxRoundUp - minRoundDown) / 5);

        mProgressBar.setVisibility(View.GONE);
        mLineChartView.setVisibility(View.VISIBLE);

        mLineChartView.show(anim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHistoricalDataSubscription != null && !mHistoricalDataSubscription.isUnsubscribed()) {
            mHistoricalDataSubscription.unsubscribe();
        }
    }
}
