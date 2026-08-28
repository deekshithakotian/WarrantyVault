package com.example.warrantyvault

import com.example.warrantyvault.common.model.ApiState
import com.example.warrantyvault.common.model.UserLogin
import com.example.warrantyvault.features.LoginViewModel
import com.example.warrantyvault.repository.LoginRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.whenever

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginRepository: LoginRepository

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Before
    fun setUp()
    {
        loginRepository = mock()
        viewModel= LoginViewModel(loginRepository,
            ioDispatcher = testDispatcher
        )
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


    @Test
    fun login_withUserCredentials_callsRepository()= runTest {

        // Arrange
        val userLogin = UserLogin(
            email = "test@gmail.com",
            password = "123456"
        )

        whenever(
            loginRepository.login(any())
        ).thenReturn(
            ApiState.Success("Login successful")
        )

        viewModel.onEmailChanged("test@gmail.com")
        viewModel.onPasswordChange("123456")

        // Act
        viewModel.onLoginClicked()

        // Wait for ViewModel coroutine
        advanceUntilIdle()

        // Assert

        verify(loginRepository).login(userLogin)

        assertEquals("Login successful",viewModel.loginResponse.value)
    }


    @Test
    fun login_withInvalidCredentials_apiReturnsError()= runTest{

        //Arrange
        val userLogin = UserLogin(
            email = "test@gmail.com",
            password = "123459"
        )

        whenever(loginRepository.login(any()))
            .thenReturn(ApiState.Error("Invalid email or password"))

        viewModel.onEmailChanged("test@gmail.com")
        viewModel.onPasswordChange("123459")

        //Act
        viewModel.onLoginClicked()

        //Wait for coroutine
        advanceUntilIdle()

        //Assert
        verify(loginRepository).login(userLogin)
        assertEquals("Invalid email or password",viewModel.errorState.value)
    }


    @Test
    fun login_withInvalidEmail_doesNotCallRepository() = runTest {

        // Arrange
        viewModel.onEmailChanged("testgmail.com")
        viewModel.onPasswordChange("123456")

        // Act
        viewModel.onLoginClicked()

        advanceUntilIdle()

        // Assert
        assertEquals(
            "Invalid email",
            viewModel.errorState.value
        )

        verify(loginRepository, never()).login(any())
    }


}