package br.com.alfatek.indikey.model

import kotlinx.serialization.Serializable

@Serializable

data class Cliente(
    val companyName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    //val endereco: String = "",
    val contactPerson: String = "",
    val cnpj: String = "",
    val project: String = "",
    val date: String = "",
    val isActive: Boolean = true,
    val isPending: Boolean = true,
    //val cidade: String = "",
    //val estado: String = "",
    //val cep: String = "",
    val referrer: String = "",
)

