package me.lokmvne.app1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import me.lokmvne.app1.Presentation.MainViewModel
import me.lokmvne.app1.Presentation.MyVideoItem
import me.lokmvne.app1.dataStore.MyVideoslist
import me.lokmvne.app1.ui.theme.App1Theme
import javax.inject.Inject

@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "VisibleForTests")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val mainviewmodel = hiltViewModel<MainViewModel>()
            val darkMode = mainviewmodel.getdarkmode.collectAsState()
            var lifecycle by remember {
                mutableStateOf(Lifecycle.Event.ON_CREATE)
            }
            val lifecycleOwner = LocalLifecycleOwner.current
            val lazyListState = rememberLazyListState()
            val focusIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
            val focusIndexOffset by remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

            App1Theme(
                darkTheme = darkMode.value,
            ) {
                val density = LocalDensity.current

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        lifecycle = event
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    MyTopAppBar(DARKMODE = darkMode.value,
                        oncheckedchange = { mainviewmodel.MyDarkModeState(it) })
                }) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 70.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = lazyListState,
                    ) {

                        items(MyVideoslist.size) { index ->
                            MyVideoItem(
                                mainviewmodel,
                                lifecycle = lifecycle,
                                MyVideoslist[index],
                                focusedVideo = (index == 0 && focusIndexOffset <= with(density) { 48.dp.toPx() }) ||
                                        (index == focusIndex + 1 && focusIndexOffset > with(density) { 48.dp.toPx() }),
                                myExoplayer = player
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

            }
        }
    }
}



@Composable
fun MyTopAppBar(
    DARKMODE: Boolean,
    oncheckedchange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "MyApp",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.width(30.dp))
        Switch(
            checked = DARKMODE, onCheckedChange = {
                oncheckedchange(!DARKMODE)
            },
            modifier = Modifier
                .padding(end = 10.dp)
                .background(Color.Transparent),
            thumbContent = {
                Icon(
                    imageVector = if (DARKMODE) {
                        Icons.Default.Nightlight
                    } else {
                        Icons.Default.WbSunny
                    },
                    contentDescription = "",
                )
            }, colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                uncheckedThumbColor = MaterialTheme.colorScheme.onBackground,
                uncheckedTrackColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.5f
                )
            )
        )
    }
}