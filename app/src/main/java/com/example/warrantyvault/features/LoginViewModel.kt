package com.example.warrantyvault.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warrantyvault.common.model.ApiState
import com.example.warrantyvault.common.model.UserLogin
import com.example.warrantyvault.common.remote.di.IoDispatcher
import com.example.warrantyvault.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

): ViewModel() {


    private val _loginState= MutableStateFlow(UserLogin())
    val loginState = _loginState.asStateFlow()

    private val _errorState= MutableStateFlow<String>("")
    val errorState = _errorState.asStateFlow()


    private val _loginResponse= MutableStateFlow("")
    val loginResponse = _loginResponse.asStateFlow()

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


        if(_errorState.value.isEmpty())
        {
            viewModelScope.launch {
                withContext(ioDispatcher)
                {
                    when(val response=loginRepository.login(_loginState.value))
                    {
                        is ApiState.Success->
                        {
                            _loginResponse.value=response.data
                        }
                        is ApiState.Error->
                        {
                            _errorState.value=response.message
                        }
                        else -> {

                        }
                    }
                }

            }
        }


    }

}