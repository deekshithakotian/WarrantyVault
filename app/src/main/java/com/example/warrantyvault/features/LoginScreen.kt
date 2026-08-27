package com.example.warrantyvault.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel,
                onSuccessLogin:()->Unit)
{

    val state by viewModel.loginState.collectAsState()
    val error by viewModel.errorState.collectAsState()

    Scaffold(
        topBar = {

            TopAppBar(title = { Text("Login") })
        },

        ) { contentPadding ->


        Column(modifier = Modifier.padding(contentPadding)
            .fillMaxSize()
            , horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChanged,
                label = { Text("Email") }
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") }
            )

            Button(
                onClick = viewModel::onLoginClicked
            ) {
                Text("Login")
            }

            Text(error)


        }
    }

}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview()
{
    val viewModel: LoginViewModel=hiltViewModel()
    LoginScreen(viewModel, onSuccessLogin = {})

}