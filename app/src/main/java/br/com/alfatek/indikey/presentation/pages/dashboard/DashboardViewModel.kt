package br.com.alfatek.indikey.presentation.pages.dashboard

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alfatek.indikey.data.AuthRepository
import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _cliente = MutableStateFlow<Resource<Cliente>?>(null)
    val cliente = _cliente.asStateFlow<Resource<Cliente>?>()
    val resultCliente: MutableState<Cliente> = mutableStateOf<Cliente>(Cliente())
    private val _clientes = MutableStateFlow<List<Cliente>?>(emptyList())
    val clientes = _clientes.asStateFlow()
    val activeClientes = isClientActive(clientes.value!!)
    val pendingClientes = isClientPending(clientes.value!!)

    fun getClients() = viewModelScope.launch {

        _clientes.value = repository.getClients().let {
            when(it){
                is Resource.Success -> {
                  it.result
                }
                else -> emptyList()
            }
        }
    }

    fun getClient(cnpj: String) = viewModelScope.launch {
        _cliente.value = Resource.Loading
        val result = repository.getClientByCnpj(cnpj, Cliente::class.java)
       result.onSuccess {
           resultCliente.value = it as Cliente
           _cliente.value = Resource.Success(it as Cliente)
           Log.d("DashboardViewModel", "getClient: $it")

       }.onFailure {
          // _cliente.value = Resource.Error(it.message.toString())
       }

    }

    fun checkClient(cnpj: String): Boolean {
       var result: Boolean = false
       val cliente = clientes.value?.firstOrNull(
            predicate = {
                it.cnpj == cnpj
            }
        )
        if (cliente != null) {
            result = true
            return true
        }
        return result
    }

    fun isClientActive(clientes: List<Cliente>): Int {
        val filteredList0 = clientes.filter {
            it.isActive  == true
        }
        Log.d("DashboardViewModel", "isClientActive: ${filteredList0}")
        return filteredList0.size
    }

    fun isClientPending(clientes: List<Cliente>): Int {
        val filteredList = clientes.filter {
            it.isPending == true
        }
        Log.d("DashboardViewModel", "isClientPending: ${filteredList}")
        return filteredList.size
    }


    init {
        viewModelScope.launch {
            getClients()
        }

    }
}