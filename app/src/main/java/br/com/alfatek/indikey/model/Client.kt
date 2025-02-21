package br.com.alfatek.indikey.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "company_name") val companyName: String = "",
    val email: String = "",
    @ColumnInfo(name = "phone_number") val phoneNumber: String = "",
    @ColumnInfo(name = "contact_person") val contactPerson: String = "",
    val cnpj: String = "",
    val project: String = "",
    val date: String = "",
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @ColumnInfo(name = "is_pending") val isPending: Boolean = true,
    val referrer: String = "",
)