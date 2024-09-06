package me.lokmvne.app1.Presentation

import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.BaseMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.lokmvne.app1.dataStore.DataStoreRepo
import javax.inject.Inject

@OptIn(UnstableApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
//    val player: ExoPlayer
) : ViewModel() {
    private val _getdarkmode = MutableStateFlow<Boolean>(false)
    val getdarkmode: StateFlow<Boolean> = _getdarkmode

    init {
        viewModelScope.launch {
            _getdarkmode.value = dataStoreRepo.getDarkMode()
        }
    }

    fun MyDarkModeState(bool: Boolean = _getdarkmode.value) {
        viewModelScope.launch {
            _getdarkmode.value = dataStoreRepo.changeMode(bool)
        }
    }


    //------------------------exoplayer-------------------------------

    fun getSource(uri: Uri): BaseMediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        val type = Util.inferContentType(uri)
        val source = when (type) {
            C.CONTENT_TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))

            C.CONTENT_TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))

            else -> ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
        }
        return source
    }
}