package br.com.alfatek.indikey.presentation.pages.cliente

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.alfatek.indikey.R
import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.presentation.pages.dashboard.DashboardViewModel
import br.com.alfatek.indikey.util.Resource
import kotlin.system.exitProcess

@Composable
fun UpdateClientScreen(
    viewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>(),
    onBackClick: () -> Unit

) {
     var client: Cliente = Cliente()
    LaunchedEffect(Unit) {

            viewModel.cliente.collect {

            if (it is Resource.Success) {
                client = it.result
                Log.d("UpdateClientScreen", "Client: $client")
            }
        }

    }



    var companyName by remember { mutableStateOf(client.companyName ?:"") }
    var email by remember { mutableStateOf(client.email) }
    var phoneNumber by remember { mutableStateOf(client.phoneNumber) }
    var contactPerson by remember { mutableStateOf(client.contactPerson) }
    var cnpj by remember { mutableStateOf("") }
    var project by remember { mutableStateOf(client.project) }
    var date by remember { mutableStateOf(client.date) }
    var isActive by remember { mutableStateOf(client.isActive) }
    var isPending by remember { mutableStateOf(client.isPending) }
    var referrer by remember { mutableStateOf(client.referrer) }

    companyName = client.companyName
    email = client.email
    phoneNumber = client.phoneNumber
    contactPerson = client.contactPerson
    cnpj = client.cnpj
    project = client.project
    date = client.date
    isActive = client.isActive
    isPending = client.isPending
    referrer = client.referrer


    Scaffold(
        Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
               // .padding(16.dp)
        ) {
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
                        text = stringResource(R.string.editar),
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
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text(stringResource(R.string.razao_social)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                Text(text = "EMAIL: $email")

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text(stringResource(R.string.phone)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = contactPerson,
                    onValueChange = { contactPerson = it },
                    label = { Text(stringResource(R.string.contato)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = cnpj,
                    onValueChange = { cnpj = it },
                    label = { Text(stringResource(R.string.cnpj)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = project,
                    onValueChange = { project = it },
                    label = { Text(stringResource(R.string.projeto)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text(stringResource(R.string.date)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextField(
                    value = referrer,
                    onValueChange = { referrer = it },
                    label = { Text(stringResource(R.string.indicacao)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        val updatedClient = Cliente(
                            companyName = companyName,
                            email = email,
                            phoneNumber = phoneNumber,
                            contactPerson = contactPerson,
                            cnpj = cnpj,
                            project = project,
                            date = date,
                            isActive = isActive,
                            isPending = isPending,
                            referrer = referrer
                        )
                        // onUpdateClient(updatedClient)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Client")
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    onClick = {
                        onBackClick()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateClientScreenPreview() {
    val sampleClient = Cliente(
        companyName = "Client A",
        email = "clientA@example.com",
        phoneNumber = "123-456-7890",
        contactPerson = "John Doe",
        cnpj = "12.345.678/0001-90",
        project = "Project X",
        date = "2023-10-27",
        isActive = true,
        isPending = false,
        referrer = "Referrer 1"
    )
//    UpdateClientScreen(
//        client = sampleClient,
////        onUpdateClient = { updatedClient ->
////            println("Updated client: $updatedClient")
////        },
//        onBackClick = {
//            println("Back")
//        }
//    )
}