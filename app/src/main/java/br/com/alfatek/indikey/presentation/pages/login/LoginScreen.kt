package br.com.alfatek.indikey.presentation.pages.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alfatek.indikey.R
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), onLoginClick:()->Unit) {
    Scaffold(modifier = Modifier
        .fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),

        ) {
            Box (modifier = Modifier
                .fillMaxWidth()
                //.border(width = 2.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                .background(Color(0xFF020D4D)) // Purple 500
                .height(100.dp),
                contentAlignment = Alignment.Center,

                ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Box (modifier = Modifier
                .fillMaxWidth()
                //.border(width = 2.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                .background(Color(0xFF243493)) // Purple 500
                .height(50.dp),
                contentAlignment = Alignment.BottomStart,

                ) {
                Text(
                    text = stringResource(R.string.saudação),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(64.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("E-mail") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { onLoginClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE), // Purple 500
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Entrar")
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    "Esqueceu a senha?", modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                        //.fillMaxWidth()
                )
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ShowLogin (modifier: Modifier = Modifier) {
//    LoginScreen({})
//
//}