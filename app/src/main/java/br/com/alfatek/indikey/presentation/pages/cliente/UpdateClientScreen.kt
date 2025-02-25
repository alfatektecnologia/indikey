package br.com.alfatek.indikey.presentation.pages.cliente

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import br.com.alfatek.indikey.util.getClientFromSharedPreferences
import br.com.alfatek.indikey.util.getIsAdminFromSharedPreferences
import java.util.Locale
import kotlin.system.exitProcess
import androidx.compose.material3.AlertDialog as AlertDialog1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateClientScreen(
    viewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>(),
    onBackClick: () -> Unit

) {

    var client= getClientFromSharedPreferences(context = LocalContext.current)

    var companyName by remember { mutableStateOf(client?.companyName ?:"") }
    var email by remember { mutableStateOf(client?.email?:"") }
    var phoneNumber by remember { mutableStateOf(client?.phoneNumber?:"") }
    var contactPerson by remember { mutableStateOf(client?.contactPerson?:"") }
    var cnpj by remember { mutableStateOf(client?.cnpj?:"") }
    var project by remember { mutableStateOf(client?.project?:"") }
    var date by remember { mutableStateOf(client?.date?:"") }
    var isActive by remember { mutableStateOf(client?.isActive?:false) }
    var isPending by remember { mutableStateOf(client?.isPending?:false) }
    var referrer by remember { mutableStateOf(client?.referrer?:"") }
    val context = LocalContext.current

    val documentoId = viewModel.getDocumentoID()
    var usuarios by remember { mutableStateOf(viewModel.getAllUsers()) }
    val isUsuarioAdmin by remember { mutableStateOf(getIsAdminFromSharedPreferences(context)) }

    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val openDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.usuarios.collect {
            if (it != null) {
                usuarios = it

            }
        }
    }

    Log.d("UpdateClientScreen", "UpdateClientScreen: $usuarios")

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
                if (openDialog.value) {
                    BasicAlertDialog(onDismissRequest = {

                        openDialog.value = false
                    }
                    )
                    {
                        Surface(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            shape = MaterialTheme.shapes.large,
                            color = Color.LightGray,
                            tonalElevation = AlertDialogDefaults.TonalElevation
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text =
                                    "Deseja realmente deletar esse cliente?",
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        colors = ButtonColors( containerColor = Color.Red, contentColor = Color.White, disabledContainerColor = Color.Red, disabledContentColor = Color.White),
                                        onClick = {
                                            openDialog.value = false
                                        },
                                        //modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Cancelar")
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.deleteDocument(
                                                documentoId,
                                                "clientes"
                                            )
                                            Toast.makeText(
                                                context,
                                                "Cliente deletado com sucesso",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            openDialog.value = false
                                        },
                                        //modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Confirmar")
                                    }
                                }
                            }
                        }
                    }
                }


                if (isUsuarioAdmin) {
                    OutlinedTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text(stringResource(R.string.razao_social)) },

                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier
                                    .clickable(enabled = true, onClick = {
                                        openDialog.value = true


                                    })
                            )

                        }

                    )
                } else{
                    OutlinedTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text(stringResource(R.string.razao_social)) },

                        modifier = Modifier.fillMaxWidth(),

                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))

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
                Row(modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    if(isUsuarioAdmin) {
                        OutlinedTextField(
                            value = referrer,
                            onValueChange = { referrer = it },
                            label = { Text(stringResource(R.string.indicacao)) },
                            //modifier = Modifier.fillMaxWidth()
                        )
                    } else{
                        OutlinedTextField(
                            value = referrer,
                            onValueChange = { referrer = it },
                            label = { Text(stringResource(R.string.indicacao)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (isUsuarioAdmin) {
                        Box(
                            modifier = Modifier
                                // .fillMaxSize()
                                .wrapContentSize(Alignment.TopEnd)
                        ) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "Localized description"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                scrollState = scrollState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                            {
                                usuarios?.forEach()
                                {
                                    DropdownMenuItem(
                                        text = { Text("${it.name}") },
                                        onClick = { referrer =
                                            it.name.toString().uppercase()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Outlined.Edit,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }

                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = isActive, onClick = { isActive = !isActive })
                        Text(stringResource(R.string.ativo))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = isPending, onClick = { isPending = !isPending })
                        Text(stringResource(R.string.pending_approval))
                    }

                }
                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        val updatedClient =
                            Cliente(
                                companyName = companyName,
                                email = email,
                                phoneNumber = phoneNumber,
                                contactPerson = contactPerson,
                                cnpj = cnpj,
                                project = project,
                                date = date,
                                isActive = isActive,
                                isPending = isPending,
                                referrer = referrer,
                                documentId = documentoId
                            )

                        try{
                            viewModel.updateClient(updatedClient)
                            Log.d("UpdateClientScreen", "UpdateClientScreen: $updatedClient")
                            Toast.makeText(context, "Client updated successfully", Toast.LENGTH_SHORT).show()
                            onBackClick()
                        }catch (e:Exception){
                            Log.d("UpdateClientScreen", "UpdateClientScreen: ${e.message}")
                            Toast.makeText(context, "Error updating client", Toast.LENGTH_SHORT).show()

                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.update_client))
                }
                Spacer(modifier = Modifier.padding(8.dp))

            }
        }
    }

}


