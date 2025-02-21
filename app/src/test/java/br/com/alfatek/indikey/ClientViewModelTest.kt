package br.com.alfatek.indikey

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.alfatek.indikey.data.ClientRepository
import br.com.alfatek.indikey.model.Client
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ClientViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var clientRepository: ClientRepository

    private lateinit var clientViewModel: ClientViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        clientViewModel = ClientViewModel(clientRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertClient_callsRepositoryInsertClient() = runTest {
        val client = Client(companyName = "Test Company")
        clientViewModel.insertClient(client)
        verify(clientRepository).insertClient(client)
    }

    @Test
    fun updateClient_callsRepositoryUpdateClient() = runTest {
        val client = Client(companyName = "Test Company")
        clientViewModel.updateClient(client)
        verify(clientRepository).updateClient(client)
    }

    @Test
    fun deleteClient_callsRepositoryDeleteClient() = runTest {
        val client = Client(companyName = "Test Company")
        clientViewModel.deleteClient(client)
        verify(clientRepository).deleteClient(client)
    }

    @Test
    fun getAllClients_returnsFlowFromRepository() = runTest {
        val client1 = Client(companyName = "Test Company 1")
        val client2 = Client(companyName = "Test Company 2")
        val clientList = listOf(client1, client2)
        val expectedFlow = flowOf(clientList)

        `when`(clientRepository.getAllClients()).thenReturn(expectedFlow)

        val actualFlow = clientViewModel.getAllClients()

        assertThat(actualFlow).isEqualTo(expectedFlow)
    }

    @Test
    fun getClientById_callsRepositoryGetClientById() = runTest {
        val clientId = 1
        clientViewModel.getClientById(clientId)
        verify(clientRepository).getClientById(clientId)
    }

    @Test
    fun getClientByCnpj_callsRepositoryGetClientByCnpj() = runTest {
        val cnpj = "123456789"
        clientViewModel.getClientByCnpj(cnpj)
        verify(clientRepository).getClientByCnpj(cnpj)
    }
}