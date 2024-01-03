package com.example.in2000_team33

import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.in2000_team33.ui.NyttBadestedDialogFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NyttBadestedTest {

    //Test universell utforming.
    companion object {
        init {
            AccessibilityChecks.enable().setRunChecksFromRootView(true)
        }
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun skrivNavn() {
        launchFragment<NyttBadestedDialogFragment>(null, R.style.Splashy)
        onView(withId(R.id.navn)).perform(typeText("Huk"))
        onView(withId(R.id.sted)).perform(typeText("Oslo"))
    }


    @Test
    fun avbryt() {
        launchFragment<NyttBadestedDialogFragment>(null, R.style.Splashy)
        onView(withText("Avbryt")).check(matches(isDisplayed())).perform(click())
        onView(withText("Avbryt")).check(doesNotExist())
    }
}