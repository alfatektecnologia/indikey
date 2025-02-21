package br.com.alfatek.indikey

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.preference.contains
import androidx.privacysandbox.tools.core.generator.build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import br.com.alfatek.indikey.model.Client
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ClientRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var clientDao: ClientDao
    private lateinit var clientRepository: ClientRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        clientDao = database.clientDao()
        clientRepository = ClientRepository(clientDao)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertClient_success() = runTest {
        val client = Client(companyName = "Test Company")
        clientRepository.insertClient(client)

        val allClients = clientRepository.getAllClients().first()
        assertThat(allClients).contains(client)
    }

    @Test
    fun updateClient_success() = runTest {
        val client = Client(companyName = "Test Company")
        clientRepository.insertClient(client)

        val updatedClient = client.copy(companyName = "Updated Company")
        clientRepository.updateClient(updatedClient)

        val allClients = clientRepository.getAllClients().first()
        assertThat(allClients).contains(updatedClient)
    }

    @Test
    fun deleteClient_success() = runTest {
        val client = Client(companyName = "Test Company")
        clientRepository.insertClient(client)

        clientRepository.deleteClient(client)

        val allClients = clientRepository.getAllClients().first()
        assertThat(allClients).doesNotContain(client)
    }

    @Test
    fun getClientById_success() = runTest {
        val client = Client(companyName = "Test Company")
        clientRepository.insertClient(client)

        val retrievedClient = clientRepository.getClientById(client.id)
        assertThat(retrievedClient).isEqualTo(client)
    }

    @Test
    fun getClientByCnpj_success() = runTest {
        val client = Client(companyName = "Test Company", cnpj = "123456789")
        clientRepository.insertClient(client)

        val retrievedClient = clientRepository.getClientByCnpj("123456789")
        assertThat(retrievedClient).isEqualTo(client)
    }
}