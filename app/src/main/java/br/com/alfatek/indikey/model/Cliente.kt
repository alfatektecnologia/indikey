package br.com.alfatek.indikey.model

import kotlinx.serialization.Serializable

@Serializable
data class Cliente(
    var companyName: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var contactPerson: String = "",
    var cnpj: String = "",
    var project: String = "",
    var date: String = "",
    var isActive: Boolean = true ,
    var isPending: Boolean = true ,
    var referrer: String = "",
    )

