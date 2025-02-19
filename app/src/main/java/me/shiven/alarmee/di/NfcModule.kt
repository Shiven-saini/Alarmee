package me.shiven.alarmee.di

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.shiven.alarmee.data.repository.NfcRepositoryImpl
import me.shiven.alarmee.domain.dummy.NfcRepository
import me.shiven.alarmee.domain.usecase.DetectNfcTagUseCase
import me.shiven.alarmee.domain.usecase.ProgramNfcTagUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NfcModule {

    @Provides
    @Singleton
    fun provideNfcRepository(@ApplicationContext context: Context): NfcRepository {
        return NfcRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideDetectNfcTagUseCase(nfcRepository: NfcRepository): DetectNfcTagUseCase {
        return DetectNfcTagUseCase(nfcRepository)
    }

    @Provides
    @Singleton
    fun provideProgramNfcTagUseCase(nfcRepository: NfcRepository): ProgramNfcTagUseCase {
        return ProgramNfcTagUseCase(nfcRepository)
    }

}