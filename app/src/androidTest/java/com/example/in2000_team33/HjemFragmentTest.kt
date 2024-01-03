package com.example.in2000_team33

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HjemFragmentTest {

    //Slår på testing av universell utforming.
    companion object {
        init {
            AccessibilityChecks.enable()
        }
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    //Sjekker at elementene i hjemfragmentet vises.
    fun elementerSynlig() {
        onView(withId(R.id.kart)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomSheet)).check(matches(isDisplayed()))
        onView(withId(R.id.posisjonsKnapp)).perform(click()).check(matches(isDisplayed()))
    }
}