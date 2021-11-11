package com.example.newdemo.resultscreen

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.scale
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.launch

const val IMG_RECOGNITION_SCREEN = "img_recognition"
const val POSE_RECOGNITION_SCREEN = "pose_recognition"
const val FACE_DETECTION_SCREEN = "face_detection"

@Composable
fun ResultScreen(
    imagePath: String?,
    forScreen: String?,
    viewModel: ResultScreenViewModel = hiltViewModel()
) {


    val uri = Uri.parse(imagePath!!.replace("\\", "/"))


    val result by remember {
        viewModel.result
    }
    val bitmap by remember {
        mutableStateOf(viewModel.bitmap)
    }

    when (forScreen) {
        IMG_RECOGNITION_SCREEN -> viewModel.imageRecognition(uri)
        POSE_RECOGNITION_SCREEN -> viewModel.poseRecognition(uri)
        FACE_DETECTION_SCREEN -> viewModel.faceDetection(uri)
    }



    Column() {
        bitmap.value?.let { Image(bitmap = it.asImageBitmap(), "bitmap") }
        Text(text = result, color = Color.Green)

    }


}






