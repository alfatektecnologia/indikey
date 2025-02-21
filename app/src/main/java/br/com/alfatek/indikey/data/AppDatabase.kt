package br.com.alfatek.indikey.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.alfatek.indikey.model.Client

@Database(entities = [Client::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
}