package com.parihardigitallabs.kisankadost

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.onNodeWithPlaceholder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationSmokeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun resetLanguagePreference() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("kisan_preferences", 0)
            .edit()
            .clear()
            .commit()
    }

    @Test
    fun homeCanNavigateToOfflineKnowledgeSearch() {
        composeRule.onNodeWithText("नमस्ते किसान भाई!").assertIsDisplayed()
        composeRule.onNodeWithText("जानकारी").performClick()
        composeRule.onNodeWithText("किसान ज्ञान सहायक").assertIsDisplayed()
        composeRule.onNodeWithPlaceholder("अपना सवाल या शब्द लिखें...").performTextInput("मिट्टी")
        composeRule.onNodeWithText("मिट्टी").assertExists()
    }

    @Test
    fun settingsCanSwitchTheAppToEnglish() {
        composeRule.onNodeWithContentDescription("सेटिंग्स").performClick()
        composeRule.onNodeWithText("English").performClick()
        composeRule.onNodeWithText("Settings").assertIsDisplayed()
        composeRule.onNodeWithText("Kisan Ka Dost").assertExists()
    }
}
