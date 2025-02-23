package br.com.alfatek.indikey.util

import android.content.Context
import android.widget.Toast



    fun validateEmail(context: Context, email: String): Boolean {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            true
        } else {
            Toast.makeText(context, "E-mail inválido: formato inválido", Toast.LENGTH_LONG).show()
            false
        }
    }

    fun validatePassword(context: Context, password: String, confirmPass: String): Boolean {
        return if (password.length >= 6 && password == confirmPass){
            true
        } else {
            Toast.makeText(context, "Senha/confirmação inválida: mínimo de 6 caracteres", Toast.LENGTH_LONG).show()
            false
        }
    }

    fun validatePhone(context: Context, phone: String): Boolean {
        return if (phone.length >= 11){
            true
        } else {
            Toast.makeText(context, "Telefone inválido: mínimo de 11 caracteres", Toast.LENGTH_LONG).show()
            false
        }
    }

    fun validateCnpj(context: Context, cnpj: String): Boolean {
        return if (cnpj.length == 18) { //74.309.915/0001-10
            true
        } else {
            Toast.makeText(context, "CNPJ inválido: mínimo de 18 caracteres", Toast.LENGTH_LONG).show()
            false

        }
    }

    fun validateName(context: Context, name: String): Boolean {
        return if (name.length >= 3){
            true
        } else {
            Toast.makeText(context, "Nome inválido: mínimo de 3 caracteres", Toast.LENGTH_LONG).show()
            false
        }
    }

    fun string2List(string: String): List<String> {
        return string.split(",").map { it.trim()}
    }


