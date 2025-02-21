package br.com.alfatek.indikey.presentation.pages.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alfatek.indikey.data.AuthRepository
import br.com.alfatek.indikey.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow
    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.signIn(email, password)
        _loginFlow.value = result
    }
    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)

        }
    }
}