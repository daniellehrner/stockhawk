package com.sam_chordas.android.stockhawk;

import android.content.Context;

import com.sam_chordas.android.stockhawk.rest.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Daniel Lehrner
 */
@RunWith(RobolectricGradleTestRunner.class)
public class UtilsTest {
    private Context mContext;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application;
    }

    @Test
    public void shouldConvertDateToWeekday() {


        assertEquals(mContext.getString(R.string.wed_short),
                Utils.convertDateToWeekday("2016-03-16", mContext));

        assertEquals(mContext.getString(R.string.mon_short),
                Utils.convertDateToWeekday("2016-02-29", mContext));

        assertEquals(mContext.getString(R.string.thu_short),
                Utils.convertDateToWeekday("1970-01-01", mContext));
    }

    @Test
    public void shouldThrowNullPointerExceptionConvertDateToWeekdayDate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("date is null");

        Utils.convertDateToWeekday(null, mContext);
    }

    @Test
    public void shouldThrowNullPointerExceptionConvertDateToWeekdayContext() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("context is null");

        Utils.convertDateToWeekday("1970-01-01", null);
    }

    @Test
    public void shouldRoundToNextTen() {
        assertEquals(10, Utils.roundToNextTen(8, true));
        assertEquals(0, Utils.roundToNextTen(8, false));

        assertEquals(10, Utils.roundToNextTen(10, false));
        assertEquals(10, Utils.roundToNextTen(10, true));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionRoundToNextTen() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("value has to be greater or equal to zero");

        Utils.roundToNextTen(-1, true);
    }

    @Test
    public void shouldThrowNullPointerExceptionGetCalculatedDate() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("dateFormat is null");

        Utils.getCalculatedDate(null, 0);
    }
}