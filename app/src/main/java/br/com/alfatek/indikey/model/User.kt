package br.com.alfatek.indikey.model

import java.util.Date

data class User(
    var userId: String? = null,
    var email: String? = null,
    var password: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var cnpj: String? = null,
    var services: List<String>? = null,
    var isAdmin: Boolean = false,
    var isActive : Boolean = true,
    var registerDate: String? = null
)
