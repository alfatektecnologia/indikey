package br.com.alfatek.indikey.util

sealed class Resource<out T> {
    data class Success<out T>(val result: T) : Resource<T>()
    data class Error(val execption: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}