package br.com.alfatek.indikey.presentation.pages.cliente

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alfatek.indikey.data.AuthRepository
import br.com.alfatek.indikey.presentation.pages.dashboard.DashboardViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddClientViewModel @Inject constructor(
    private val repository: AuthRepository,

):ViewModel() {
    val user = repository.currentUser

    fun <T: Any> saveCollection(collection:String, data: T) = viewModelScope.launch {
        try {
            repository.saveToFirestore(collection,data)

        }catch (e:Exception){
            e.printStackTrace()

        }

    }

}