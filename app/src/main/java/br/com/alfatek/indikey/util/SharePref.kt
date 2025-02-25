package br.com.alfatek.indikey.util

import android.content.Context
import android.content.SharedPreferences
import br.com.alfatek.indikey.model.Cliente
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun <T> saveJsonToSharedPreferences(context: Context, key: String, value: T, serializer: KSerializer<T>) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val jsonValue = Json.encodeToString(serializer, value)
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

fun <T> getGenericFromSharedPreferences(context: Context, key: String, serializer: KSerializer<T>): T? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString(key, null) ?: return null
    return Json.decodeFromString(serializer,json)
}
fun removeClientFromSharedPreferences(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        remove("cliente_key")
        apply()
    }
}

fun getIsAdminFromSharedPreferences(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isAdmin", false)

}