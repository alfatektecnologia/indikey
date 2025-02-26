package br.com.alfatek.indikey.presentation.pages.dashboard

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alfatek.indikey.data.AuthRepository
import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.model.User
import br.com.alfatek.indikey.util.Resource
import br.com.alfatek.indikey.util.getIsAdminFromSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: AuthRepository,
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
    val userId = repository.currentUser?.uid





    private val _usuarios = MutableStateFlow<List<User>?>(emptyList())
    val usuarios = _usuarios.asStateFlow()

    var users: User? = null

    fun getAllUsers(): List<User>? {
        var list: List<User>? = null
        // Log.d("DashboardViewModel", "Admin: $isAdmin")
        viewModelScope.launch {
            repository.getUsers().let { resource ->
                _usuarios.value = resource
                list = resource

                // Log.d("DashboardViewModel", "getAllClients: $list")
                Log.d("DashboardViewModel", "getAllUsers: ${resource?.size?:0}")

            }
            return@launch
        }
        return list

    }

    fun getAllClients(isAdmin: Boolean): List<Cliente>? {
        var list: List<Cliente>? = null
       // Log.d("DashboardViewModel", "Admin: $isAdmin")
        viewModelScope.launch {
            repository.getClients(isAdmin).let { resource ->
                _clientes.value = resource
                list = resource
                _activeClientes.value = getActiveClients(list!!)
                _pendingClientes.value = getPendingClients(list!!)
                getAllUsers()
                Log.d("DashboardViewModel", "isAdmim: ${isUsuarioAdmin()}")
                //Log.d("DashboardViewModel", "getAllClients: ${resource?.size}")

            }
            return@launch
        }
        return list

    }

    fun isUsuarioAdmin(): Boolean {
        return getUsuarioById(usuarios.value!!,userId!!)?.isAdmin ?: false
    }

    fun getDocumentoID(): String {
        return repository.getDocumentId()
    }

    fun updateClient(cliente: Cliente) = viewModelScope.launch {
        repository.updateClient(cliente, getDocumentoID())
    }

    fun getClient(cnpj: String) = viewModelScope.launch {
        _cliente.value = Resource.Loading
        val result = repository.getClientByCnpj(cnpj, Cliente::class.java)
        result?.onSuccess {
            resultCliente.value = it as Cliente
            _cliente.value = Resource.Success(it as Cliente)

            Log.d("DashboardViewModel", "getClient: $it")
            Log.d("DashboardViewModel", "getDocumentoID: ${getDocumentoID()}")

        }?.onFailure {
            // _cliente.value = Resource.Error(it.message.toString())
        }

    }
    fun deleteDocument(documentoId: String,collection: String) = viewModelScope.launch {
        repository.deleteDocument(documentoId,collection)
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
    fun getActiveClients(clientes: List<Cliente>): List<Cliente>? {
        return clientes.filter { it.isActive }
    }
    fun getPendingClients(clientes: List<Cliente>): List<Cliente>? {
        return clientes.filter { it.isPending }
    }

    fun getUsuarioById(usuarios: List<User>, userId: String): User? {
        return usuarios.firstOrNull { it.userId == userId }
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
            isUsuarioAdmin()

        }

    }
}
