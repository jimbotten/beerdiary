package com.beerdiary.activities;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.beerdiary.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SelectDrinkRecyclerTest {
    @Rule
    public ActivityTestRule<SelectDrinkRecycler> mActivityRule = new ActivityTestRule<>(
            SelectDrinkRecycler.class);


    @Test
    public void selectDrink() throws Exception {
        onView(withId(R.id.drink_recycler_cardview))
                .perform(click());
        onView(withId(R.id.drink_recycler_cardview))
                .check(matches(withText("nope")));
    }

}