package com.example.warrantyvault.features

import androidx.lifecycle.ViewModel
import com.example.warrantyvault.common.model.UserLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {


    private val _loginState= MutableStateFlow(UserLogin())
    val loginState = _loginState.asStateFlow()

    private val _errorState= MutableStateFlow<String>("")
    val errorState = _errorState.asStateFlow()

    fun onEmailChanged(value:String)
    {
        _loginState.value = _loginState.value.copy(email = value)
    }

    fun onPasswordChange(value:String)
    {
        _loginState.value= _loginState.value.copy(password = value)
    }

    fun onLoginClicked()
    {
        _errorState.value = ""

        val email = _loginState.value.email
        val password = _loginState.value.password

        if(email.isBlank() || password.isBlank())
        {
            _errorState.value = "Email and password cannot be blank"
            return
        }

        if(!email.contains("@"))
        {
            _errorState.value = "Invalid email"
            return
        }

        if(password.length<6)
        {
            _errorState.value = "Password must be at least 6 characters"
            return

        }


    }

}