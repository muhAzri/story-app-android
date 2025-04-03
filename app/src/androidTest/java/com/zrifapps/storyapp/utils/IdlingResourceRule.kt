package com.zrifapps.storyapp.utils

import androidx.test.espresso.IdlingRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class IdlingResourceRule : TestWatcher() {

    override fun starting(description: Description) {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    override fun finished(description: Description) {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}
