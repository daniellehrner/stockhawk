package com.sam_chordas.android.stockhawk.ui;

import android.app.Instrumentation;
import android.content.Intent;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
    public void shouldShowLineGraphActivity() {
        Intent intent = new Intent();
        intent.putExtra(LineGraphActivity.KEY_SYM, "test");
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.linechart)).check(matches(isDisplayed()));
        onView(withText(R.string.title_activity_line_graph)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldMockRestClient() {
        String stringStub[] = {"Fr", "Sa", "So"};
        float floatStub[] = {(float)75.0, (float)75.0, (float)100.0};
        LineSet dataStub = new LineSet(stringStub, floatStub);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < stringStub.length; ++i) {
            stringBuilder.append(stringStub[i])
                    .append(": ")
                    .append(floatStub[i]);

            if (i < stringStub.length - 1) {
                stringBuilder.append(", ");
            }
        }

        Mockito.when(mHistoricalDataClient.getData("test", 7)).thenReturn(dataStub);

        Intent intent = new Intent();
        intent.putExtra(LineGraphActivity.KEY_SYM, "test");

        mActivityRule.launchActivity(intent);

        onView(withId(R.id.linechart)).check(matches(withContentDescription(stringBuilder.toString())));
    }
}