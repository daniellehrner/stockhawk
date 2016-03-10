package com.sam_chordas.android.stockhawk.ui;

import android.support.test.rule.ActivityTestRule;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.customMatcher.ToastMatcher;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.core.StringEndsWith.endsWith;


public class MyStocksActivityTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(MyStocksActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldOpenSymbolSearch() {
        onView(withId(R.id.fab)).perform(click());

        onView(withText(R.string.symbol_search)).check(matches(isDisplayed()));
        onView(withText(R.string.content_test)).check(matches(isDisplayed()));
        onView(allOf(withClassName(endsWith("EditText")), withHint(R.string.input_hint)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowSymbolNotFoundToast() {
        onView(withId(R.id.fab)).perform(click());

        onView(allOf(withClassName(endsWith("EditText")), withHint(R.string.input_hint)))
                .perform(typeText("Test"));

        onView(withText("OK")).perform(click());

        onView(withText(R.string.symbol_not_found)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowLineGraphActivity() {
        onView(withText("YHOO")).perform(click());

        onView(withClassName(endsWith("LineChartView"))).check(matches(isDisplayed()));
    }
}