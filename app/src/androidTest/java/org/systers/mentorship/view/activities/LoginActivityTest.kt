package org.systers.mentorship.view.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.systers.mentorship.R
import org.systers.mentorship.utils.AssertFunctions.Companion.findEditTextInTextInputLayout
import org.systers.mentorship.utils.AssertFunctions.Companion.getString
import org.systers.mentorship.utils.AssertFunctions.Companion.hasTextInputLayoutErrorText
import org.systers.mentorship.utils.AssertFunctions.Companion.hasTextInputLayoutHintText

/**
 * This class specifies the UI test for LoginActivity
 */
class LoginActivityTest {

    private val EMPTY_USERNAME_ERROR: String = getString(R.string.error_empty_username)
    private val EMPTY_PASSWORD_ERROR: String = getString(R.string.error_empty_password)

    private val TEST_USERNAME: String = "test user"
    private val TEST_PASSWORD: String = "test password"

    private val USER_OR_PASSWORD_IS_WRONG = "Username or password is wrong."

    @get:Rule
    var mActivityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)

    /**
     * This test simply checks if all views are enabled and have the corresponding text/hint
     */
    @Test
    fun testAllViewsEnabledAndHaveCorrectTextOrHint() {
        onView(withId(R.id.tiUsername)).check(matches(isEnabled()))
                .check(matches(hasTextInputLayoutHintText(
                        mActivityRule.activity.getString(R.string.username_or_email))))
        onView(withId(R.id.tiPassword)).check(matches(isEnabled()))
                .check(matches(hasTextInputLayoutHintText(
                        mActivityRule.activity.getString(R.string.password))))
        onView(withId(R.id.btnLogin)).check(matches(isEnabled())).check(matches(withText(R.string.login)))
    }

    /**
     * This test checks that error messages are shown after the Login button is clicked
     *  with empty username and password fields.
     */
    @Test
    fun testLoginButtonClickedWhenUsernameAndPasswordAreEmpty() {
        // Perform Click operation on Login Button
        onView(withId(R.id.btnLogin)).perform(click())

        // Checks that the error message in both the editTexts appears after button click
        onView(withId(R.id.tiUsername)).check(matches(hasTextInputLayoutErrorText(EMPTY_USERNAME_ERROR)))
        onView(withId(R.id.tiPassword)).check(matches(hasTextInputLayoutErrorText(EMPTY_PASSWORD_ERROR)))
    }

    /**
     * This test checks that error messages are shown after the Login button is clicked
     *  with empty username and a password.
     */
    @Test
    fun testLoginButtonClickedWhenUsernameIsEmptyAndPasswordIsFilled() {
        // Type in a password
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText(TEST_PASSWORD), closeSoftKeyboard())

        // Perform Click operation on Login Button
        onView(withId(R.id.btnLogin)).perform(click())

        // Check for error message on username and not on password
        onView(withId(R.id.tiUsername)).check(matches(hasTextInputLayoutErrorText(EMPTY_USERNAME_ERROR)))
        onView(withId(R.id.tiPassword)).check(matches(not(hasTextInputLayoutErrorText(EMPTY_PASSWORD_ERROR))))
    }

    /**
     *  This test checks that error messages are shown after the Login button is clicked
     *  with a username and an empty password.
     */
    @Test
    fun testLoginButtonClickedWhenUsernameIsFilledAndPasswordIsEmpty() {
        // Type in a username
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText(TEST_USERNAME), closeSoftKeyboard())

        // Perform Click operation on Login Button
        onView(withId(R.id.btnLogin)).perform(click())

        // Check for no error message on username with an error message on password
        onView(withId(R.id.tiUsername)).check(matches(not(hasTextInputLayoutErrorText(EMPTY_USERNAME_ERROR))))
        onView(withId(R.id.tiPassword)).check(matches(hasTextInputLayoutErrorText(EMPTY_PASSWORD_ERROR)))
    }

    /**
     *  This test checks that the error message that the data is wrong is being showed
     *  in the SnackBar as the user just can't exist because of the whitespaces in password.
     */
    @Test
    fun testLoginButtonClickedWhenAllFieldsAreFilled() {
        // Type in a username and a password
        findEditTextInTextInputLayout(R.id.tiUsername).perform(typeText(TEST_USERNAME), closeSoftKeyboard())
        findEditTextInTextInputLayout(R.id.tiPassword).perform(typeText(TEST_PASSWORD), closeSoftKeyboard())

        // Perform Click operation on Login Button
        onView(withId(R.id.btnLogin)).perform(click())

        // Check for the error message in SnackBar, the user does not exist
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(USER_OR_PASSWORD_IS_WRONG)))
    }

}
