package me.shiven.alarmee.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.shiven.alarmee.data.repository.VibrationRepositoryImpl
import me.shiven.alarmee.domain.dummy.VibrationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VibrationRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVibrationRepository(
        vibrationRepositoryImpl: VibrationRepositoryImpl
    ): VibrationRepository

}