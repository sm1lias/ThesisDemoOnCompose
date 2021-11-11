package com.example.newdemo.mainscreen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newdemo.Utils.createImageFile
import android.graphics.ImageDecoder
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.example.newdemo.BuildConfig
import com.example.newdemo.Screen
import com.example.newdemo.Utils.saveBitmap
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun MainScreen(
    navController: NavController, viewModel: MainScreenViewModel = hiltViewModel()
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .padding(bottom = 32.dp)

    ) {
        MySpacer(value = 32)
        MyButton(
            "Image Recognition", navController, "img_recognition"
        )
        MySpacer(value = 32)
        MyButton(
            "Pose Recognition", navController, "pose_recognition"
        )
        MySpacer(value = 32)
        MyButton(
            "Face Recognition", navController, "face_detection"
        )


    }
}


@Composable
fun MyButton(
    text: String,
    navController: NavController,
    forScreen: String,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val launcherForPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Timber.d("PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Timber.d("PERMISSION DENIED")
        }
    }
    val context = LocalContext.current

    val file=createImageFile(context)!!
    var uri: Uri = FileProvider.getUriForFile(
        context, BuildConfig.APPLICATION_ID + ".provider",
        file
    )
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            coroutineScope.launch {
                var source: ImageDecoder.Source =
                    ImageDecoder.createSource(context.contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                saveBitmap(context, bitmap, Bitmap.CompressFormat.JPEG, "image/jpeg", "temp")

                navController.navigate(
                    Screen.ResultScreen.withArgs(
                        (uri.toString()).replace(
                            "/",
                            "\\"
                        ), forScreen
                    )
                )
            }


        }
    }
    Button(
        onClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) -> {
                    launcher.launch(uri)
                }
                else -> {
                    // Asking for permission
                    launcherForPermission.launch(Manifest.permission.CAMERA)
                }
            }


        }
    ) {
        Text(text = text)

    }

}

@Composable
fun MySpacer(value: Int) {
    Spacer(modifier = Modifier.height(value.dp))
}







