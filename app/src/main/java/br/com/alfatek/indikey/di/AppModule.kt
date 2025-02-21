package br.com.alfatek.indikey.di

import android.app.Application
import androidx.room.Room
import br.com.alfatek.indikey.data.AuthRepository
import br.com.alfatek.indikey.data.AuthRepositoryImpl
import br.com.alfatek.indikey.data.AppDatabase
import br.com.alfatek.indikey.data.ClientDao
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "indikey_database"
        ).build()
    }

    @Provides
    fun provideClientDao(appDatabase: AppDatabase): ClientDao {
        return appDatabase.clientDao()
    }

}