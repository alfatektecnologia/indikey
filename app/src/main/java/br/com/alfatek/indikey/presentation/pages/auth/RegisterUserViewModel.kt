package br.com.alfatek.indikey.presentation.pages.auth


import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alfatek.indikey.data.AuthRepository
import br.com.alfatek.indikey.util.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val repository: AuthRepository, @ApplicationContext private val context: Context
) : ViewModel() {

    private val _registerFlow = MutableStateFlow <Resource<FirebaseUser>?>(null  )
    val registerFlow = _registerFlow

    val userId = repository.currentUser?.uid
    val isUsuarioAdmin =  mutableStateOf( false)
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)



    fun <T: Any> saveCollection(collection:String, data: T) = viewModelScope.launch {
        try {
            repository.saveToFirestore(collection,data)
           _registerFlow.value = Resource.Success(repository.currentUser!!)
        }catch (e:Exception){
            e.printStackTrace()
            _registerFlow.value = Resource.Error(e)
        }

    }


    val currentUser: FirebaseUser?
        get() = repository.currentUser


    suspend fun getUserAdmin(): Boolean {
        isUsuarioAdmin.value = repository.getUserById(userId!!)?.isAdmin == true
        Log.d("RegisterUserViewModel", "getUserAdmin: ${isUsuarioAdmin.value}")
        sharedPreferences.edit().putBoolean("isAdmin", isUsuarioAdmin.value ).apply()
        return isUsuarioAdmin.value

    }

    fun registerUser(name: String, email: String, password: String) = viewModelScope.launch {
        _registerFlow.value = Resource.Loading
        val result = repository.signUp(name,email, password)
        _registerFlow.value = result
    }

    init {
        if(repository.currentUser != null){
            _registerFlow.value = Resource.Success(repository.currentUser!!)

        }
        viewModelScope.launch {
            getUserAdmin()
        }

    }

}