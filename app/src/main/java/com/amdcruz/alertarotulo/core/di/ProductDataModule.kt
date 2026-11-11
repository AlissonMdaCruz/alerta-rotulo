package com.amdcruz.alertarotulo.core.di

import com.amdcruz.alertarotulo.product.data.DefaultProductRepository
import com.amdcruz.alertarotulo.product.data.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductDataModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        repository: DefaultProductRepository,
    ): ProductRepository
}
