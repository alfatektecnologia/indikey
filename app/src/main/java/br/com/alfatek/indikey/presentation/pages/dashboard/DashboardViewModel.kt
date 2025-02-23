package br.com.alfatek.indikey.presentation.pages.dashboard

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.alfatek.indikey.data.AuthRepository
import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _cliente = MutableStateFlow<Resource<Cliente>?>(null)
    val cliente = _cliente.asStateFlow<Resource<Cliente>?>()
    val resultCliente: MutableState<Cliente> = mutableStateOf<Cliente>(Cliente())
    private val _clientes = MutableStateFlow<List<Cliente>?>(emptyList())
    val clientes = _clientes.asStateFlow()
    val _activeClientes = MutableStateFlow<List<Cliente>?>(emptyList())
    val _pendingClientes = MutableStateFlow<List<Cliente>?>(emptyList())
    val activeClientes = _activeClientes.asStateFlow()
    val pendingClientes = _pendingClientes.asStateFlow()


    fun getAllClients(): List<Cliente>? {
        var list: List<Cliente>? = null
        viewModelScope.launch {
            repository.getClients().let { resource ->
                _clientes.value = resource
                list = resource
                _activeClientes.value = getActiveClients(list!!)
                _pendingClientes.value = getPendingClients(list!!)
                Log.d("DashboardViewModel", "getAllClients: $list")
                Log.d("DashboardViewModel", "getAllClients: ${resource?.size}")

            }
            return@launch
        }
        return list

    }
    fun getAllClientsActives(): List<Cliente>? {
        var list: List<Cliente>? = null
        viewModelScope.launch {
            repository.getActiveClients().let { resource ->
                _activeClientes.value = resource
                list = resource
                Log.d("DashboardViewModel", "getAllClientsActives: $list")
                Log.d("DashboardViewModel", "getSize: ${resource?.size}")

            }
            return@launch
        }
        return list

    }
    fun getAllClientsPending(): List<Cliente>? {
        var list: List<Cliente>? = null
        viewModelScope.launch {
            repository.getPendingClients().let { resource ->
                _pendingClientes.value = resource
                list = resource
                Log.d("DashboardViewModel", "getAllClientsPending: $list")
                Log.d("DashboardViewModel", "getSize: ${resource?.size}")
            }
            return@launch
        }
        return list
    }


    private fun getDocumentoID(): String {
        return repository.getDocumentId()
    }

    fun updateClient(cliente: Cliente) = viewModelScope.launch {
        repository.updateClient(cliente, getDocumentoID())
    }

    fun getClient(cnpj: String) = viewModelScope.launch {
        _cliente.value = Resource.Loading
        val result = repository.getClientByCnpj(cnpj, Cliente::class.java)
        result.onSuccess {
            resultCliente.value = it as Cliente
            _cliente.value = Resource.Success(it as Cliente)

            Log.d("DashboardViewModel", "getClient: $it")
            Log.d("DashboardViewModel", "getDocumentoID: ${getDocumentoID()}")

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
    fun getActiveClients(clientes: List<Cliente>): List<Cliente> {
        return clientes.filter { it.isActive }
    }
    fun getPendingClients(clientes: List<Cliente>): List<Cliente> {
        return clientes.filter { it.isPending }
    }

    fun onCnpjClick(cnpj: String, context: Context): Boolean {
        if (!checkClient(cnpj)) {
            Toast.makeText(
                context, "Cliente liberado para cadastro", Toast.LENGTH_SHORT
            ).show()
            return true

        } else {
            Toast.makeText(
                context, "CLIENTE JÁ CADASTRADO", Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }



    init {
        viewModelScope.launch {
            getAllClients()
            getAllClientsActives()
            getAllClientsPending()
        }

    }
}
