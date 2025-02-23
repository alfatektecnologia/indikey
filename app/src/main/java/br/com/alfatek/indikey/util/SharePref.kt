package br.com.alfatek.indikey.util

import android.content.Context
import android.content.SharedPreferences
import br.com.alfatek.indikey.model.Cliente
import kotlinx.serialization.json.Json

fun saveJsonToSharedPreferences(context: Context, key: String, value: Cliente) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val jsonValue = Json.encodeToString(Cliente.serializer(), value)
    with(sharedPreferences.edit()) {
        putString(key, jsonValue)
        apply()
    }

}

fun getClientFromSharedPreferences(context: Context): Cliente? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("cliente_key", null) ?: return null
    return Json.decodeFromString<Cliente>(json)
}