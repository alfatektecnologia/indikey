package br.com.alfatek.indikey.presentation.pages.cliente

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.alfatek.indikey.R
import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.presentation.pages.dashboard.DashboardViewModel
import br.com.alfatek.indikey.util.getIsAdminFromSharedPreferences
import br.com.alfatek.indikey.util.saveJsonToSharedPreferences
import kotlin.system.exitProcess

@Composable
fun ClientListScreen(
    onBackClick: () -> Unit, onClick: () -> Unit, onItemClick: (Cliente) -> Unit,
    viewModel: DashboardViewModel? = hiltViewModel()) {
    val context = LocalContext.current
    var clientes by remember { mutableStateOf(viewModel?.getAllClients(
        getIsAdminFromSharedPreferences(context)
    )) }
    var activeClientes by remember { mutableStateOf (viewModel?.activeClientes?.value) }
    var pendingClientes by remember { mutableStateOf (viewModel?.pendingClientes?.value) }

    val todosState = remember { mutableStateOf(true) }
    val activesState = remember { mutableStateOf(false) }
    val pendingState = remember { mutableStateOf(false) }
    val searchType = remember { mutableStateOf("Todos") }
    val tabela = remember { mutableStateOf(clientes) }
    val texto = remember { mutableStateOf("N/A") }


    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    LaunchedEffect(Unit) {
        viewModel?.clientes?.collect {
            if (it != null) {
                clientes = it
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel?.activeClientes?.collect {
            if (it != null) {
                activeClientes = it
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel?.pendingClientes?.collect {
            if (it != null) {
                pendingClientes = it
            }
        }
    }

    Log.d("ClientListScreen", "clientes: $clientes")


    Scaffold (Modifier.fillMaxSize()){innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            //.verticalScroll(rememberScrollState(0))
            .fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    //.border(width = 2.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                    .background(Color(0xFF020D4D)) // Purple 500
                    .height(100.dp),
                contentAlignment = Alignment.Center,

                ) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), horizontalArrangement =
                Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(R.drawable.arrow_back_24), contentDescription = null,
                        modifier = Modifier.clickable { onBackClick() },
                        tint = Color.White
                    )
                    Text(
                        text = stringResource(R.string.clientes),
                        //Modifier.fillMaxWidth(),
                        //textAlign = TextAlign.End ,
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(
                        painterResource(R.drawable.logout_white), contentDescription = null,
                        modifier = Modifier.clickable { exitProcess(0) },
                        tint = Color.White
                    )
                }
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = todosState.value,
                            onClick = {
                                todosState.value = !todosState.value
                                activesState.value = false
                                pendingState.value = false
                                searchType.value = "Todos"
                               // texto.value = "N/A"
                            }
                        )
                        Text(
                            text = "Todos",
                            style = MaterialTheme.typography.bodyLarge,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = activesState.value,
                            onClick = {
                                activesState.value = !activesState.value
                                todosState.value = false
                                pendingState.value = false
                                searchType.value = "Actives"
                               // texto.value = "N/A"
                            }
                        )
                        Text(
                            text = "Ativos",
                            style = MaterialTheme.typography.bodyLarge,

                            )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = pendingState.value,
                            onClick = {
                                pendingState.value = !pendingState.value
                                todosState.value = false
                                activesState.value = false
                                searchType.value = "Pending"
                               // texto.value = "N/A"
                            }
                        )
                        Text(
                            text = "Pendentes",
                            style = MaterialTheme.typography.bodyLarge,

                            )
                    }


                }

                if (searchType.value == "Todos") {
                   tabela.value = clientes.orEmpty()
                } else if (searchType.value == "Actives") {
                    tabela.value = activeClientes.orEmpty()
                } else {
                    tabela.value = pendingClientes.orEmpty()
                }


                    ClientList(tabela.value!!, onItemClick = {
                        saveJsonToSharedPreferences(context, "cliente_key", it, Cliente.serializer())
                        Log.d("ClientListScreen", "ClientListScreen: $it")
                        viewModel?.getClient(it.cnpj)

                        onItemClick(it)
                    })




            }
        }
    }
}

@Composable
fun ClientListItem(client: Cliente, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = client.companyName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Row {
                Text(text = "CNPJ: ", fontWeight = FontWeight.Bold)
                Text(text = client.cnpj)
            }
            Row {
                Text(text = "Contato: ", fontWeight = FontWeight.Bold)
                Text(text = client.contactPerson)
            }
            Row {
                Text(text = "Fone: ", fontWeight = FontWeight.Bold)
                Text(text = client.phoneNumber)
            }
            Row {
                Text(text = "Email: ", fontWeight = FontWeight.Bold)
                Text(text = client.email)
            }
            Row {
                Text(text = "Projeto: ", fontWeight = FontWeight.Bold)
                Text(text = client.project)
            }
            Row {
                Text(text = "Data: ", fontWeight = FontWeight.Bold)
                Text(text = client.date)
            }
            Row {
                Text(text = "Ativo?: ", fontWeight = FontWeight.Bold)
                Text(text = client.isActive.toString())
            }
            Row {
                Text(text = "Pendente?: ", fontWeight = FontWeight.Bold)
                Text(text = client.isPending.toString())
            }
            Row {
                Text(text = "Indicado por: ", fontWeight = FontWeight.Bold)
                Text(text = client.referrer)
            }
        }
    }
}

@Composable
fun ClientList(clients: List<Cliente>, onItemClick: (Cliente) -> Unit) {
    LazyColumn {
        items(clients) { client ->
            ClientListItem(client = client) {
                onItemClick(client)
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun ClientListPreview() {
//    val sampleClients = listOf(
//        Cliente(
//            companyName = "Client A",
//            email = "clientA@example.com",
//            phoneNumber = "123-456-7890",
//            contactPerson = "John Doe",
//            cnpj = "12.345.678/0001-90",
//            project = "Project X",
//            date = "2023-10-27",
//            isActive = true,
//            isPending = false,
//            referrer = "Referrer 1"
//        ),
//        Cliente(
//            companyName = "Client B",
//            email = "clientB@example.com",
//            phoneNumber = "987-654-3210",
//            contactPerson = "Jane Smith",
//            cnpj = "98.765.432/0001-09",
//            project = "Project Y",
//            date = "2023-10-26",
//            isActive = false,
//            isPending = true,
//            referrer = "Referrer 2"
//        ),
//        Cliente(
//            companyName = "Client C",
//            email = "clientC@example.com",
//            phoneNumber = "555-123-4567",
//            contactPerson = "Peter Jones",
//            cnpj = "55.555.555/0001-55",
//            project = "Project Z",
//            date = "2023-10-25",
//            isActive = true,
//            isPending = true,
//            referrer = "Referrer 3"
//        )
//    )
//    ClientList(clients = sampleClients) { client ->
//        println("Clicked on: ${client.companyName}")
//    }
//}