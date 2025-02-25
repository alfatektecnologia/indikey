package br.com.alfatek.indikey.presentation.pages.cliente


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.alfatek.indikey.R
import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.presentation.pages.dashboard.DashboardViewModel
import br.com.alfatek.indikey.util.validateCnpj
import br.com.alfatek.indikey.util.validateEmail
import br.com.alfatek.indikey.util.validateName
import br.com.alfatek.indikey.util.validatePhone
import java.util.Date
import kotlin.system.exitProcess

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddClientScreen(onBackClick: () -> Unit,
                    viewModel: AddClientViewModel? = hiltViewModel()
    ){
    val viewModelDashboard = hiltViewModel<DashboardViewModel>()

    val userDisplayName = viewModel?.user?.displayName
    var email by rememberSaveable { mutableStateOf("") }
    var contatoName by rememberSaveable { mutableStateOf("") }
    var razao_social by rememberSaveable { mutableStateOf("") }
    var cnpj by rememberSaveable { mutableStateOf("") }
    var projeto by rememberSaveable { mutableStateOf("") }
    var contatoPhone by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var cnpjError= remember { viewModelDashboard.checkClient(cnpj) }


    // Focus management
    val focusManager = LocalFocusManager.current

    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState(0))
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
                        text = stringResource(R.string.cliente),
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp),
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                    .height(350.dp),
                    contentAlignment = Alignment.Center,) {

                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Dados do cliente:", modifier = Modifier.padding(top = 8.dp, start = 8.dp))
                        OutlinedTextField(
                            value = razao_social,
                            onValueChange = {
                                razao_social = it
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                            label = { Text(text = stringResource(R.string.razao_social)) },
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    painterResource(R.drawable.business_24), contentDescription = null,
                                    tint = Color(0xFF3F51B5)
                                )
                            }
                        )
                        OutlinedTextField(
                            value = cnpj,
                            onValueChange = { cnpj = it },
                            maxLines = 1,
                            singleLine = true,
                            textStyle = TextStyle( color = if(cnpjError) Color.Red else Color.Black),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                            supportingText = {Text("Ex: 00.000.000/0000-00")},
                            label = { Text(text = stringResource(R.string.cnpj)) },
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                     viewModelDashboard.onCnpjClick(cnpj, context)

                                    },
                                    tint = Color(0xFFAC07F3)
                                )
                            }
                        )
                        OutlinedTextField(
                            value = projeto,
                            onValueChange = { projeto = it },
                            minLines = 4,

                            label = { Text(text = stringResource(R.string.projeto)) },
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    painterResource(R.drawable.comment_24),
                                    contentDescription = null,
                                    tint = Color(0xFF00BCD4)
                                )
                            }
                        )

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                    .height(300.dp),
                    contentAlignment = Alignment.TopStart,) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Dados do contato:", modifier = Modifier.padding(top = 8.dp, start = 8.dp))
                        OutlinedTextField(
                            value = contatoName,
                            onValueChange = {contatoName = it },

                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                            keyboardActions = KeyboardActions(onSend = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                            label = { Text(text = stringResource(R.string.username)) },
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Person, contentDescription = null,
                                    tint = Color(0xFF3F51B5)
                                )
                            }
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },

                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                            keyboardActions = KeyboardActions(onSend = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                            label = { Text(text = stringResource(R.string.email)) },
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Email, contentDescription = null,
                                    tint = Color(0xFFFF9800)
                                )
                            }
                        )

                        OutlinedTextField(
                            value = contatoPhone,
                            onValueChange = { contatoPhone = it },
                            maxLines = 1,
                            singleLine = true,
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }),
                            label = { Text(text = stringResource(R.string.phone)) },
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Phone, contentDescription = null,
                                    tint = Color(0xFF000000)
                                )
                            }
                        )

                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .fillMaxWidth(),

                    onClick = {
                        if (
                            validateName(context, razao_social) &&
                            validateCnpj(context, cnpj) &&
                            viewModelDashboard.onCnpjClick(cnpj, context)  &&
                            validateName(context, contatoName) &&
                            validateEmail(context, email) &&
                            validatePhone(context, contatoPhone)
                        ) {
                            val cliente: Cliente = Cliente(
                                companyName = razao_social,
                                email = email,
                                contactPerson = contatoName,
                                phoneNumber = contatoPhone,
                                cnpj = cnpj,
                                project = projeto,
                                isActive = true,
                                isPending = true,
                                date = Date().toString(),
                                referrer = userDisplayName!!,

                                )
                            val collection = "clientes"
                            viewModel.saveCollection(collection, cliente)
                            onBackClick()
                        } else {
                            Toast.makeText(context, "Verifique os campos", Toast.LENGTH_SHORT)
                                .show()
                            if (cnpjError) onBackClick()
                        }

                    },

                    ) {
                    Text(text = stringResource(R.string.add_client))
                }
            }
        }

    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ShowAddClientScreen(modifier: Modifier = Modifier) {
    AddClientScreen(onBackClick = {}, null)
}

