package me.shiven.alarmee.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.shiven.alarmee.data.AlarmSoundPlayerImpl
import me.shiven.alarmee.domain.dummy.AlarmSoundPlayer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmPlayerModule {

    @Provides
    @Singleton
    fun provideAlarmSoundPlayer(
        @ApplicationContext context: Context
    ): AlarmSoundPlayer {
        return AlarmSoundPlayerImpl(context)
    }


}