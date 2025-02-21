package br.com.alfatek.indikey.presentation.pages.auth

data class RegisterUserState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null,
)
