package br.com.alfatek.indikey.presentation.pages.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.alfatek.indikey.R
import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.util.Resource
import br.com.alfatek.indikey.util.validateCnpj
import kotlin.system.exitProcess

@Composable
fun DashboardScreen(onClientClick: () -> Unit = {},
                    onClientListClick: () -> Unit,
                    onEditClientClick: () -> Unit,
                    viewModel: DashboardViewModel? = hiltViewModel<DashboardViewModel>()) {

    val larguradevise = LocalConfiguration.current.screenWidthDp.dp
    var cnpj by rememberSaveable { mutableStateOf("") }
    var isBoxVisible by remember { mutableStateOf(false) }
    var cliente = viewModel?.resultCliente?.value
    var clientes by remember { mutableStateOf(emptyList<Cliente>()) }
    val activeClientes = viewModel?.isClientActive(clientes)
    val pendingClientes = viewModel?.isClientPending(clientes)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel?.clientes?.collect {
            if (it != null) {
                clientes = it
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel?.cliente?.collect {

            if (it is Resource.Success) {
                isBoxVisible = true
            }
        }

    }



    Scaffold(modifier = Modifier.fillMaxSize())
    { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState(0))
                //.background(Color(0xFF1A1A1A))
                .padding(innerPadding))
        {
            //topo->actionbar
            Box (modifier = Modifier
                .fillMaxWidth()
                //.border(width = 2.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                .background(Color(0xFF020D4D)) // Purple 500
                .height(100.dp),
                contentAlignment = Alignment.Center,)
            {
                Row(horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,) {
                    Column (
                        modifier = Modifier.width(larguradevise/2 + 50.dp),
                        horizontalAlignment = Alignment.End){
                        Text(
                            text = stringResource(R.string.painel),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd) {
                        Icon(
                            painterResource(R.drawable.logout_white), contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable { exitProcess(0) }


                        )
                    }

                }
            }
            //Pesquisar
            Box (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium)
                //.background(Color(0xFF243493)) // Purple 500
                .height(100.dp),
                contentAlignment = Alignment.Center,)
            {
                Column {
                    OutlinedTextField(
                        value = cnpj,
                        onValueChange = {cnpj = it},
                        singleLine = true,
                        maxLines = 1,
                        supportingText = { Text("Ex.: 00.000.000/0000-00") },
                        label = { Text(text = stringResource(R.string.search),
                           ) },
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .fillMaxWidth(),
                        trailingIcon = {
                            Icon(Icons.Filled.Search,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    if (validateCnpj(context,cnpj)) {
                                        viewModel?.getClient(cnpj)
                                    }else{
                                        isBoxVisible = false
                                    }
                                }
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                }

            }

            //Clientes
            Box (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.medium) )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.client_list),
                        color = Color(0xFF243493),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(
                        onClick = { /* Add client action */ },
                        //colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE)),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(text = stringResource(R.string.add_client),
                            modifier = Modifier.clickable { onClientClick() },
                            color = Color.White)
                    }
                }
            }

if (isBoxVisible) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = MaterialTheme.shapes.medium
            )
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable(onClick = { onEditClientClick() }),
        )
        {

            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                        .padding(8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = cliente?.contactPerson ?: "Contato",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = cliente?.companyName ?: "Razão social",
                            color = Color.Gray
                        )
                        Text(text = cliente?.email ?: "Email", color = Color.Gray)
                        Text(text = cliente?.phoneNumber ?: "Fone", color = Color.Gray)
                        Text(
                            text = cliente?.referrer ?: "Indicado por:",
                            color = Color.Gray
                        )
                    }

                }

            }

        }
    }
}

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth())
            {
                Box (modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(16.dp)
                    .fillMaxSize()){
                    Card(modifier = Modifier
                        .clickable { onClientListClick() }
                        .fillMaxSize()){
                        Column(modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        )
                         {
                             Icon(
                                 painterResource(R.drawable.people_alt_24),
                                 contentDescription = null, tint = Color.Blue
                             )
                             Spacer(modifier = Modifier.height(8.dp))
                             Text(
                                 text = stringResource(R.string.total_clients),
                                 color = Color.Gray,

                             )
                             Spacer(modifier = Modifier.height(8.dp))
                             Text(text = clientes.size.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }

                    }
                }
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp)
                        .padding(16.dp)
                        .fillMaxSize()
                       // .align(Alignment.CenterHorizontally)
                ) {
                    Card(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Icon(
                                painterResource(R.drawable.check_circle_24),
                                contentDescription = null, tint = Color.Green
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = stringResource(R.string.active_clients), color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = activeClientes?.toString() ?: "0", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box (modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(16.dp)
                    .fillMaxSize()){
                    Card(modifier = Modifier.fillMaxSize()){
                        Column(modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Icon(
                                painterResource(R.drawable.data_exploration_24),
                                contentDescription = null, tint = Color.Cyan
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = stringResource(R.string.new_this_month), color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = clientes.size.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }

                    }
                }
                Box (modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(16.dp)
                    .fillMaxSize()){
                    Card(modifier = Modifier.fillMaxSize()){
                        Column(modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Icon(
                                painterResource(R.drawable.pending_actions_24),
                                contentDescription = null, tint = Color.Yellow
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = stringResource(R.string.pending_approval), color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = pendingClientes?.toString()?:"0", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun StatisticCard(label: String, value: String) {
    Card(
        modifier = Modifier
           // .weight(1f)
            .padding(8.dp),
        //elevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = label, color = Color.Gray)
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowDashboardScreen(modifier: Modifier = Modifier) {
    DashboardScreen(
        onClientClick = {},
        onClientListClick = {},
        onEditClientClick = {},
        null
    )
}
