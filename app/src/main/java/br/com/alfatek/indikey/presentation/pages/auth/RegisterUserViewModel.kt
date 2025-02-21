package br.com.alfatek.indikey.presentation.pages.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alfatek.indikey.data.AuthRepository
import br.com.alfatek.indikey.model.User
import br.com.alfatek.indikey.util.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _registerFlow = MutableStateFlow <Resource<FirebaseUser>?>(null  )
    val registerFlow = _registerFlow

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList = _userList.asStateFlow()


    var db = FirebaseFirestore.getInstance()

    fun <T: Any> saveCollection(collection:String, data: T) = viewModelScope.launch {
        try {
            repository.saveToFirestore(collection,data)
           _registerFlow.value = Resource.Success(repository.currentUser!!)
        }catch (e:Exception){
            e.printStackTrace()
            _registerFlow.value = Resource.Error(e)
        }

    }

    fun getUsers() = viewModelScope.launch {
        repository.getUsers().collect{
            _userList.value = it.let {
                when(it){
                    is Resource.Success -> it.result
                    else -> emptyList()
                }
            }
        }
    }


    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun registerUser(name: String, email: String, password: String) = viewModelScope.launch {
        _registerFlow.value = Resource.Loading
        val result = repository.signUp(name,email, password)
        _registerFlow.value = result
    }

    init {
        if(repository.currentUser != null){
            _registerFlow.value = Resource.Success(repository.currentUser!!)

        }
    }

}