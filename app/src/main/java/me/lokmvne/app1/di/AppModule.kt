package me.lokmvne.app1.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.lokmvne.app1.dataStore.DataStoreRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreRepo(context: Context): DataStoreRepo {
        return DataStoreRepo(context)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

//    @Provides
//    @Singleton
//    fun provideContext(@ApplicationContext context: Context): Context {
//        return context
//    }
}