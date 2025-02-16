package me.shiven.alarmee.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.shiven.alarmee.data.local.alarm.AlarmDao
import me.shiven.alarmee.data.local.alarm.AlarmDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAlarmDatabase(@ApplicationContext context: Context): AlarmDatabase {
        return Room.databaseBuilder(
            context,
            AlarmDatabase::class.java,
            "alarm_database"
        ).fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    fun provideAlarmDao(alarmDatabase: AlarmDatabase): AlarmDao {
        return alarmDatabase.alarmDao
    }

}