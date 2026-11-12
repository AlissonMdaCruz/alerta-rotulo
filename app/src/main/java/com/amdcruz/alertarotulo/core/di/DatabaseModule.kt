package com.amdcruz.alertarotulo.core.di

import android.content.Context
import androidx.room.Room
import com.amdcruz.alertarotulo.core.database.AlertaRotuloDatabase
import com.amdcruz.alertarotulo.history.data.local.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AlertaRotuloDatabase {
        return Room.databaseBuilder(
            context,
            AlertaRotuloDatabase::class.java,
            "alerta_rotulo.db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(database: AlertaRotuloDatabase): HistoryDao {
        return database.historyDao()
    }
}
