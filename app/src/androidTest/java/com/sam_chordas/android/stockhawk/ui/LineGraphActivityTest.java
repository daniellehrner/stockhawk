package com.sam_chordas.android.stockhawk.ui;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.db.chart.model.LineSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StockHawkApplication;
import com.sam_chordas.android.stockhawk.dependencyInjection.TestingComponent;
import com.sam_chordas.android.stockhawk.rest.HistoricalDataClient;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.sam_chordas.android.stockhawk.customMatcher.ButtonBackgroundMatcher.withButtonBackgroundColor;
import static com.sam_chordas.android.stockhawk.customMatcher.ButtonTextColorMatcher.withButtonTextColor;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Daniel Lehrner
 */
@RunWith(AndroidJUnit4.class)
public class LineGraphActivityTest {
    @Rule
    public ActivityTestRule<LineGraphActivity> mActivityRule = new ActivityTestRule<>(
            LineGraphActivity.class,
            true,
            false); // do not launch the activity immediately

    @Inject HistoricalDataClient mHistoricalDataClient;

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        StockHawkApplication app
                = (StockHawkApplication) instrumentation.getTargetContext().getApplicationContext();
        TestingComponent component = (TestingComponent) app.getComponent();
        component.inject(this);
    }

    @Test
    public void shouldShowWeekGraph() {
        String stringStub[] = {"Fr", "Sa", "So"};
        float floatStub[] = {(float)75.0, (float)75.0, (float)100.0};
        LineSet dataStub = new LineSet(stringStub, floatStub);

        Mockito.when(mHistoricalDataClient.getData("test", 7)).thenReturn(dataStub);

        Intent intent = new Intent();
        intent.putExtra(LineGraphActivity.KEY_SYM, "test");

        mActivityRule.launchActivity(intent);

        onView(withId(R.id.linechart)).check(
                matches(withContentDescription(
                        createGraphHint(stringStub, floatStub)
                )));

    }

    @Test
    public void shouldShowTwoWeekGraph() {
        String stringStub[] = {"Fr", "Sa", "So"};
        float floatStub[] = {(float)75.0, (float)100.0, (float)150.0};
        LineSet dataStub = new LineSet(stringStub, floatStub);

        Mockito.when(mHistoricalDataClient.getData("test2", 7)).thenReturn(dataStub);
        Mockito.when(mHistoricalDataClient.getData("test2", 14)).thenReturn(dataStub);

        Intent intent = new Intent();
        intent.putExtra(LineGraphActivity.KEY_SYM, "test2");

        mActivityRule.launchActivity(intent);

        onView(withId(R.id.button_two_weeks)).perform(click());

        onView(withId(R.id.linechart)).check(
                matches(withContentDescription(
                        createGraphHint(stringStub, floatStub)
                )));


    }

    @Test
    public void shouldShowMonthGraph() {
        String stringStub[] = {"So", "Mo", "Tu", "Mi"};
        float floatStub[] = {(float)1.0, (float)7.0, (float)15.0, (float)20.0};
        LineSet dataStub = new LineSet(stringStub, floatStub);

        Mockito.when(mHistoricalDataClient.getData("test3", 7)).thenReturn(dataStub);
        Mockito.when(mHistoricalDataClient.getData("test3", 30)).thenReturn(dataStub);

        Intent intent = new Intent();
        intent.putExtra(LineGraphActivity.KEY_SYM, "test3");

        mActivityRule.launchActivity(intent);

        onView(withId(R.id.button_month)).perform(click());

        onView(withId(R.id.linechart)).check(
                matches(withContentDescription(
                        createGraphHint(stringStub, floatStub)
                )));
    }

    private String createGraphHint(String[] labels, float[] values) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < labels.length; ++i) {
            stringBuilder.append(labels[i])
                    .append(": ")
                    .append(values[i]);

            if (i < labels.length - 1) {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }
}