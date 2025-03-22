package br.com.alfatek.indikey.presentation.pages.auth

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.alfatek.indikey.R
import br.com.alfatek.indikey.model.User
import br.com.alfatek.indikey.util.Resource
import br.com.alfatek.indikey.util.string2List
import br.com.alfatek.indikey.util.validateCnpj
import br.com.alfatek.indikey.util.validateEmail
import br.com.alfatek.indikey.util.validateName
import br.com.alfatek.indikey.util.validatePassword
import br.com.alfatek.indikey.util.validatePhone
import java.util.Date
import kotlin.system.exitProcess

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterUserScreen(
    viewModel: RegisterUserViewModel? = hiltViewModel(),
    onNavigateToDashboard:()->Unit) {

    val user: User = remember{User()}
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var cnpj by rememberSaveable { mutableStateOf("") }
    var confirmPass by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val registerFlow = viewModel?.registerFlow?.collectAsState()
    val isEnable = remember { mutableStateOf(true) }



    // Focus management
    val focusManager = LocalFocusManager.current

    // Password visibility state
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    registerFlow?.value.let {
        val collection = "users"
        when (it) {
            is Resource.Success -> {
                LaunchedEffect(Unit) {
                    user.name = name
                    user.cnpj = cnpj
                    user.registerDate = Date().toString()
                    user.email = email
                    user.phone = phone
                    user.password = password
                    user.services = string2List(description)
                    user.userId = viewModel?.currentUser?.uid
                    viewModel?.saveCollection(collection, user)
                    onNavigateToDashboard()
                }
            }
            is Resource.Error -> {
                Toast.makeText(context, it.execption.message, Toast.LENGTH_LONG).show()
            }
            Resource.Loading -> CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF6200EE)
            )
            null -> {
            }
        }
    }

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
                    .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.cadastro),
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    //.border(width = 2.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                    .background(Color(0xFF243493)) // Purple 500
                    .height(50.dp),
                contentAlignment = Alignment.BottomStart,

                ) {
                Text(
                    text = stringResource(R.string.authentication),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    label = { Text(text = stringResource(R.string.username)) },
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(Icons.Filled.Person, contentDescription = null,
                            tint = Color(0xFF3F51B5)
                        )
                    }
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},

                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    label = { Text(text = stringResource(R.string.email)) },
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(Icons.Filled.Email, contentDescription = null,
                            tint = Color(0xFFFF9800)
                        )
                    }
                )
                OutlinedTextField(
                    value = cnpj,
                    onValueChange = {cnpj = it},
                    maxLines = 1,
                    singleLine = true,
                    supportingText = {Text("Ex.: 00.000.000/0000-00")},
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    label = { Text(text = stringResource(R.string.cnpj)) },
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(painterResource(R.drawable.business_24),
                            contentDescription = null,
                            tint = Color(0xFFAC07F3)
                        )
                    }
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = {phone = it},
                    maxLines = 1,
                    singleLine = true,
                    supportingText = {Text("Ex.: (00) 00000-0000")},
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    label = { Text(text = stringResource(R.string.phone)) },
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(Icons.Filled.Phone, contentDescription = null,
                        tint = Color(0xFF000000)
                        )
                    }
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    visualTransformation = if(!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,

                    label = { Text(text = stringResource(R.string.password)) },
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(painterResource(if(!passwordVisible) R.drawable.key_off_24 else R.drawable.key_24), contentDescription = null,
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                            tint = Color(0xFFE91E63))
                    }
                )
                OutlinedTextField(
                    value = confirmPass,
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    visualTransformation = if(!confirmPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                    onValueChange = {confirmPass = it},
                    label = { Text(text = stringResource(R.string.confirm_password)) },
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(painterResource(if(!confirmPasswordVisible) R.drawable.key_off_24 else R.drawable.key_24), contentDescription = null,
                            modifier = Modifier.clickable { confirmPasswordVisible = !confirmPasswordVisible },
                            tint = Color(0xFFE91E63)
                        )
                    }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = {description = it},
                    maxLines = 6,

                    label = { Text(text = stringResource(R.string.description)) },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(painterResource(R.drawable.comment_24), contentDescription = null,
                            tint = Color(0xFF00BCD4)
                        )
                    }
                )
                Button(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    enabled = isEnable.value,

                    onClick = {
                        if (validateName(context, name) &&
                            validateEmail(context, email) &&
                            validateCnpj(context, cnpj) &&
                            validatePhone(context, phone) &&
                            validatePassword(context, password, confirmPass) &&
                            (password == confirmPass)
                        ) {
                            viewModel?.registerUser(name, email, password)
                            isEnable.value=false
                            Toast.makeText(
                                context,
                                "Salvando dados...",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            Toast.makeText(
                                context,
                                "Preencha todos os campos corretamente",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    },

                ) {
                    Text(text = stringResource(R.string.register))
                }
            }
        }

    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ShowRegisterUserScreen(modifier: Modifier = Modifier) {
    RegisterUserScreen(
        null,
       onNavigateToDashboard = {  },

    )
}

