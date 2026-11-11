package com.amdcruz.alertarotulo.core.di

import com.amdcruz.alertarotulo.history.data.DefaultHistoryRepository
import com.amdcruz.alertarotulo.history.data.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryDataModule {

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        repository: DefaultHistoryRepository,
    ): HistoryRepository
}
