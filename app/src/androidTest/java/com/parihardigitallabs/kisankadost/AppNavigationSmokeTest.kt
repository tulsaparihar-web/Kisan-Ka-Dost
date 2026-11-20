package com.parihardigitallabs.kisankadost

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationSmokeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeCanNavigateToOfflineKnowledgeSearch() {
        composeRule.onNodeWithText("नमस्ते किसान भाई!").assertIsDisplayed()
        composeRule.onNodeWithText("जानकारी").performClick()
        composeRule.onNodeWithText("किसान ज्ञान सहायक").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).performTextInput("मिट्टी")
        composeRule.waitForIdle()
        assertTrue(
            "Search query or matching result should be present",
            composeRule.onAllNodesWithText("मिट्टी").fetchSemanticsNodes().isNotEmpty()
        )
        composeRule.onNodeWithContentDescription("सेटिंग्स").performClick()
        composeRule.onNodeWithText("English").performClick()
        composeRule.onNodeWithText("Settings").assertIsDisplayed()
        composeRule.onNodeWithText("Kisan Ka Dost").assertExists()
    }
}
