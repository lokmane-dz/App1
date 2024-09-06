package me.lokmvne.app1.di

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.util.Clock
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewScoped
import dagger.hilt.components.SingletonComponent

@SuppressLint("VisibleForTests")
@OptIn(UnstableApi::class)
@Module
@InstallIn(SingletonComponent::class)
object ExoplayerModule {
    @Provides
    fun provideExoPlayer(application: Context, trackSelector: DefaultTrackSelector): ExoPlayer {
        return ExoPlayer.Builder(application)
            .setTrackSelector(trackSelector)
            .setClock(Clock.DEFAULT)
            .setHandleAudioBecomingNoisy(true)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setPriority(1)
            .setWakeMode(1)
            .setLooper(Looper.getMainLooper())
            .build()
    }

    @Provides
    fun provideTrackSelector(application: Context): DefaultTrackSelector {
        return DefaultTrackSelector(application).apply {
            setParameters(
                buildUponParameters()
                    .setMaxVideoSizeSd()
                    .setPreferredAudioLanguages()
            )
        }
    }
}