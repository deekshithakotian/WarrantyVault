package com.example.warrantyvault

import com.example.warrantyvault.features.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    lateinit var  viewModel: LoginViewModel

    @Before
    fun setUp()
    {
        viewModel= LoginViewModel()
    }

    @Test
    fun login_withBlankEmail_showsBlankEmailError()
    {
        viewModel.onEmailChanged("")
        viewModel.onPasswordChange("1234")

        viewModel.onLoginClicked()
        assertEquals("Email and password cannot be blank",viewModel.errorState.value)
    }


    @Test
    fun login_withBlankPassword_returnsBlankPasswordError()
    {
        viewModel.onEmailChanged("test@gmail.com")
        viewModel.onPasswordChange("")
        viewModel.onLoginClicked()
        assertEquals("Email and password cannot be blank",viewModel.errorState.value)
    }


    @Test
    fun login_withInValidEmail_showsInvalidEmailError()
    {
        viewModel.onEmailChanged("testgmail.com")
        viewModel.onPasswordChange("1234567")
        viewModel.onLoginClicked()
        assertEquals("Invalid email",viewModel.errorState.value)
    }


    @Test
    fun login_withInvalidPasswordLen_showsPasswordLengthError()
    {
        viewModel.onEmailChanged("test@gmail.com")
        viewModel.onPasswordChange("123")
        viewModel.onLoginClicked()
        assertEquals("Password must be at least 6 characters",viewModel.errorState.value)
    }


}