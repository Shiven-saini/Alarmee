package me.shiven.alarmee.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.shiven.alarmee.data.repository.QrCodeRepositoryImpl
import me.shiven.alarmee.domain.dummy.QrCodeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQrCodeRepository(
        qrCodeRepositoryImpl: QrCodeRepositoryImpl
    ): QrCodeRepository

}