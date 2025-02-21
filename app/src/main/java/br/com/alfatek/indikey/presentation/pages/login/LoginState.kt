package br.com.alfatek.indikey.presentation.pages.login

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)