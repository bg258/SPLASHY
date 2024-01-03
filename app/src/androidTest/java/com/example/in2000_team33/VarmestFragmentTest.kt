package com.example.in2000_team33

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.in2000_team33.ui.BadeAdapter
import com.example.in2000_team33.ui.badestedListe.VarmestFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VarmestFragmentTest {

    //Test universell utforming.
    companion object {
        init {
            AccessibilityChecks.enable().setRunChecksFromRootView(true)
        }
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun scrollTest() {
        launchFragmentInContainer<VarmestFragment>()
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.scrollTo<BadeAdapter.ViewHolder>(hasDescendant(withText("Huk"))),
                RecyclerViewActions.scrollTo<BadeAdapter.ViewHolder>(hasDescendant(withText("Telegrafbukta"))),
            )
    }

    @Test
    fun klikkTest() {
        launchFragmentInContainer<VarmestFragment>()
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<BadeAdapter.ViewHolder>(0, click())
            )
    }

    @Test(expected = PerformException::class)
    fun eksistererIkke() {
        launchFragmentInContainer<VarmestFragment>()
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.scrollTo<BadeAdapter.ViewHolder>(hasDescendant(withText("Sognsvann"))))
    }
}