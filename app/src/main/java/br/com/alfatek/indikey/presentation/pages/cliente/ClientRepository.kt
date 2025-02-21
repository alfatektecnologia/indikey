package br.com.alfatek.indikey.presentation.pages.cliente

import br.com.alfatek.indikey.data.ClientDao
import br.com.alfatek.indikey.model.Client
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientRepository @Inject constructor(private val clientDao: ClientDao) {

    suspend fun insertClient(client: Client) = clientDao.insertClient(client)

    suspend fun updateClient(client: Client) = clientDao.updateClient(client)

    suspend fun deleteClient(client: Client) = clientDao.deleteClient(client)

    fun getAllClients(): Flow<List<Client>> = clientDao.getAllClients()

    suspend fun getClientById(clientId: Int): Client? = clientDao.getClientById(clientId)

    suspend fun getClientByCnpj(cnpj: String): Client? = clientDao.getClientByCnpj(cnpj)
}