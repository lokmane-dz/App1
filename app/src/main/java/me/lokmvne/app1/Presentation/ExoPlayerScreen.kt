package me.lokmvne.app1.Presentation

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import me.lokmvne.app1.dataStore.MyVideos

@Composable
fun ExoPlayerScreen() {

}


@OptIn(UnstableApi::class)
@Composable
fun MyVideoItem(
    mainViewModel: MainViewModel,
    lifecycle: Lifecycle.Event,
    video: MyVideos,
    focusedVideo: Boolean,
    myExoplayer: ExoPlayer
) {

    if (focusedVideo) {
        LaunchedEffect(video.url) {
            val videoUri = mainViewModel.getSource(Uri.parse(video.url))
            myExoplayer.setMediaSource(videoUri)
            myExoplayer.prepare()
            myExoplayer.play()
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).also { playerview ->
                playerview.player = myExoplayer
            }
        },
        update = {
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                    //it.player?.play()
                }

                else -> Unit
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .aspectRatio(16f / 9f)
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = video.name,
        color = if (focusedVideo) Color.Red else MaterialTheme.colorScheme.onBackground
    )
}