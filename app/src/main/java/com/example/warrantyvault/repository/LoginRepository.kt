package com.example.warrantyvault.repository

import com.example.warrantyvault.common.model.ApiState
import com.example.warrantyvault.common.model.UserLogin
import com.example.warrantyvault.common.remote.LoginService
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginService: LoginService
) {


    suspend fun login(userLogin: UserLogin): ApiState<String>
    {

        try {

            val response=loginService.login(userLogin)

            return if(response.isSuccessful && response.body()!=null)
            {
                ApiState.Success(response.body()!!.message)
            }
            else
            {
                ApiState.Error(response.message())
            }


        }catch(ex:Exception)
        {
            return ApiState.Error(ex.localizedMessage?:"Unknown Error")
        }


    }


}