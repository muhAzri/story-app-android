package com.zrifapps.storyapp.presentation.screens.story.create

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.zrifapps.storyapp.MainActivity
import com.zrifapps.storyapp.R
import com.zrifapps.storyapp.utils.IdlingResourceRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class CreateStoryTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val idlingResourceRule = IdlingResourceRule()

    private lateinit var context: Context
    private lateinit var mockImageUri: Uri

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()

        val testImageFile = File(context.cacheDir, "test_image.jpg")
        if (!testImageFile.exists()) {
            testImageFile.createNewFile()
        }
        mockImageUri = Uri.fromFile(testImageFile)

        Intents.init()

        val resultData = Intent()
        resultData.data = mockImageUri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

        val galleryResult = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(galleryResult)

        performLogin()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private val testEmail = "azri@domain.com"
    private val testPassword = "test1234"

    private fun performLogin() {
        val isAuthenticated = try {
            composeTestRule.onNodeWithContentDescription("Add Story").assertExists()
            true
        } catch (e: AssertionError) {
            false
        }

        if (!isAuthenticated) {
            composeTestRule.onNodeWithText(context.getString(R.string.skip)).performClick()
            composeTestRule.onNodeWithText(context.getString(R.string.get_started)).performClick()
            composeTestRule.onNodeWithText(context.getString(R.string.email_label))
                .performTextInput(testEmail)
            composeTestRule.onNodeWithText(context.getString(R.string.password_label))
                .performTextInput(testPassword)
            composeTestRule.onNodeWithText(context.getString(R.string.login)).performClick()

            composeTestRule.waitUntil(timeoutMillis = 10000) {
                try {
                    composeTestRule.onNodeWithContentDescription("Add Story").assertExists()
                    true
                } catch (e: AssertionError) {
                    false
                }
            }
        }
    }

    @Test
    fun testAddStoryWorkflow() {
        composeTestRule.onNodeWithContentDescription("Add Story")
            .assertExists()
            .performClick()

        composeTestRule.waitForIdle()

        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("pm grant ${context.packageName} android.permission.CAMERA")

        composeTestRule.onNodeWithText(context.getString(R.string.camera)).performClick()
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.selected_photo))
            .assertExists()

        val testDescription = "This is a test story description"
        composeTestRule.onNodeWithText(context.getString(R.string.description_placeholder))
            .performTextInput(testDescription)

        composeTestRule.onNodeWithText(
            context.getString(R.string.add_current_location),
            useUnmergedTree = true
        )
            .assertExists()
            .performClick()

        composeTestRule.onNodeWithText(context.getString(R.string.submit_story)).performClick()

        composeTestRule.waitForIdle()
    }

    @Test
    fun testAddStoryWithGallery() {
        composeTestRule.onNodeWithContentDescription("Add Story")
            .assertExists().performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(context.getString(R.string.gallery)).performClick()
        intended(hasAction(Intent.ACTION_GET_CONTENT))

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.selected_photo))
            .assertExists()

        val testDescription = "This is a test story from gallery"
        composeTestRule.onNodeWithText(context.getString(R.string.description_placeholder))
            .performTextInput(testDescription)

        composeTestRule.onNodeWithText(context.getString(R.string.submit_story)).performClick()

        composeTestRule.waitForIdle()
    }

    @Test
    fun testValidationErrors() {
        composeTestRule.onNodeWithContentDescription("Add Story")
            .assertExists().performClick()

        composeTestRule.waitForIdle()

        val testDescription = "Test description"
        composeTestRule.onNodeWithText(context.getString(R.string.description_placeholder))
            .performTextInput(testDescription)

        composeTestRule.onNodeWithText(context.getString(R.string.submit_story)).performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(context.getString(R.string.photo_required))
            .assertExists()

        composeTestRule.onNodeWithText(context.getString(R.string.camera)).performClick()
        composeTestRule.onNodeWithText(testDescription).performTextClearance()

        composeTestRule.onNodeWithText(context.getString(R.string.submit_story)).performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(context.getString(R.string.description_error))
            .assertExists()
    }
}
