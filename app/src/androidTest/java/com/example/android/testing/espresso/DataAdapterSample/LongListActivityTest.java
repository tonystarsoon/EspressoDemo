package com.example.android.testing.espresso.DataAdapterSample;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

/**
 * Tests to verify that the behavior of {@link LongListActivity} is correct.
 * <p>
 * Note that in order to scroll the list you shouldn't use {@link ViewActions#scrollTo()} as
 * {@link Espresso#onData(org.hamcrest.Matcher)} handles scrolling.</p>
 *
 * @see #onRow(String)
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LongListActivityTest {
    private static final String TEXT_ITEM_30 = "item: 30";
    private static final String TEXT_ITEM_30_SELECTED = "30";
    private static final String TEXT_ITEM_60 = "item: 60";
    // Match the last item by matching its text.
    private static final String LAST_ITEM_ID = "item: 99";

    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test. This is a replacement
     * for {@link ActivityInstrumentationTestCase2}.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<LongListActivity> mActivityRule = new ActivityTestRule<>(
            LongListActivity.class);

    //检查文本是LAST_ITEM_ID的条目是否存在,也就是检查list的长度
    @Test
    public void lastItem_NotDisplayed() {
        // Last item should not exist if the list wasn't scrolled down.
        //使用onView查找视图,withText通过匹配文本来查找,
        onView(withText(LAST_ITEM_ID)).check(doesNotExist());
    }

    //Check that the item is created. onData() takes care of scrolling.
    //检查list的条目是否都被创建
    @Test
    public void list_Scrolls() {
        onRow(LAST_ITEM_ID).check(matches(isCompletelyDisplayed()));
    }

    //Clicks on a row and checks that the activity detected the click.
    //点击一个条目,并检查这个条目是否被点击了.
    @Test
    public void row_Click() {
        //点击这个条目
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowContentTextView)).perform(click());

        // Check that the activity detected the click on the first column.
        onView(ViewMatchers.withId(R.id.selection_row_value))
                .check(matches(withText(TEXT_ITEM_30_SELECTED)));
    }

    //点击条目中的按钮
    @Test
    public void toggle_Click() {
        //点击条目中的按钮
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowToggleButton)).perform(click());
        //检查条目中的按钮是否被点击了
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowToggleButton)).check(matches(isChecked()));
    }

    /**
     * Make sure that clicking on the toggle button doesn't trigger a click on the row.
     */
    @Test
    public void toggle_ClickDoesntPropagate() {
        // Click on one of the rows.
        onRow(TEXT_ITEM_30).onChildView(withId(R.id.rowContentTextView)).perform(click());

        // Click on the toggle button, in a different row.
        onRow(TEXT_ITEM_60).onChildView(withId(R.id.rowToggleButton)).perform(click());

        // Check that the activity didn't detect the click on the first column.
        onView(ViewMatchers.withId(R.id.selection_row_value))
                .check(matches(withText(TEXT_ITEM_30_SELECTED)));
    }

    /**
     * Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific row.
     * <p>
     * Note: A custom matcher can be used to match the content and have more readable code.
     * See the Custom Matcher Sample.
     * </p>
     *
     * @param str the content of the field
     * @return a {@link DataInteraction} referencing the row
     */
    private static DataInteraction onRow(String str) {
        return onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is(str)));
//        return onData(withText(str));
    }
}


