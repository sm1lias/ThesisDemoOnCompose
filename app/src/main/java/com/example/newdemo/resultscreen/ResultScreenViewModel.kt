package com.example.newdemo.resultscreen

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ResultScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    var result = mutableStateOf("")
    var bitmap= mutableStateOf<Bitmap?>(null)


    fun imageRecognition(uri: Uri) {

        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.6f)
            .build()
        val labeler = ImageLabeling.getClient(options)
        val resultText: StringBuilder = StringBuilder()
        val image = InputImage.fromFilePath(context, uri)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                labels.forEachIndexed { index, item ->
                    val text = item.text
                    resultText.append("Item ${index + 1}: $text\n")
                }
                result.value = resultText.toString()
            }
            .addOnFailureListener { e ->
                Timber.d(e, "test: ")
            }
            .addOnCompleteListener {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(context.contentResolver, uri)
                bitmap.value =
                    ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)

            }
    }

    fun faceDetection(uri: Uri) {
        val image = InputImage.fromFilePath(context, uri)
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(highAccuracyOpts)

        detector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.isEmpty()) {

                } else {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(context.contentResolver, uri)
                    bitmap.value =
                        ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
                    faceProcess(faces, bitmap.value!!)
                }
            }
            .addOnFailureListener { e ->
            }

    }

    private fun faceProcess(faces: List<Face>, bitmap: Bitmap) {
        val paint = Paint()
        paint.apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

        val canvas = Canvas(bitmap)
        for (face in faces) {
            val bounds = face.boundingBox
            canvas.drawRect(bounds, paint)
//            val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
//            val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees


            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
            // nose available):
//            val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
//            leftEar?.let {
//                val leftEarPos = leftEar.position
//            }
//
//            // If contour detection was enabled:
//            val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
//            val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points
//
//            // If classification was enabled:
//            if (face.smilingProbability != null) {
//                val smileProb = face.smilingProbability
//            }
//            if (face.rightEyeOpenProbability != null) {
//                val rightEyeOpenProb = face.rightEyeOpenProbability
//            }
//
//            // If face tracking was enabled:
//            if (face.trackingId != null) {
//                val id = face.trackingId
//            }
        }
        this.bitmap.value = bitmap

    }

    fun poseRecognition(uri: Uri) {
        val options: AccuratePoseDetectorOptions = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
        val poseDetector = PoseDetection.getClient(options)
        val image = InputImage.fromFilePath(context, uri)
        poseDetector.process(image).addOnSuccessListener {
            val source: ImageDecoder.Source =
                ImageDecoder.createSource(context.contentResolver, uri)
            bitmap.value =
                ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
            processPose(it, bitmap.value!!)

        }
            .addOnFailureListener {
                Log.d(TAG, "poseRecognition: ", it)
            }
    }

    private fun processPose(pose: Pose, bitmap: Bitmap) {
        try {

            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)

            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)


            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)


            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)


            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
            val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)


            val leftEyePosition = leftEye.position
            val lEyeX = leftEyePosition.x
            val lEyeY = leftEyePosition.y
            val rightEyePosition = rightEye.position
            val rEyeX = rightEyePosition.x
            val rEyeY = rightEyePosition.y

            val leftShoulderP = leftShoulder.position
            val lShoulderX = leftShoulderP.x
            val lShoulderY = leftShoulderP.y
            val rightSoulderP = rightShoulder.position
            val rShoulderX = rightSoulderP.x
            val rShoulderY = rightSoulderP.y

            val leftElbowP = leftElbow.position
            val lElbowX = leftElbowP.x
            val lElbowY = leftElbowP.y
            val rightElbowP = rightElbow.position
            val rElbowX = rightElbowP.x
            val rElbowY = rightElbowP.y

            val leftWristP = leftWrist.position
            val lWristX = leftWristP.x
            val lWristY = leftWristP.y
            val rightWristP = rightWrist.position
            val rWristX = rightWristP.x
            val rWristY = rightWristP.y

            val leftHipP = leftHip.position
            val lHipX = leftHipP.x
            val lHipY = leftHipP.y
            val rightHipP = rightHip.position
            val rHipX = rightHipP.x
            val rHipY = rightHipP.y

            val leftKneeP = leftKnee.position
            val lKneeX = leftKneeP.x
            val lKneeY = leftKneeP.y
            val rightKneeP = rightKnee.position
            val rKneeX = rightKneeP.x
            val rKneeY = rightKneeP.y

            val leftAnkleP = leftAnkle.position
            val lAnkleX = leftAnkleP.x
            val lAnkleY = leftAnkleP.y
            val rightAnkleP = rightAnkle.position
            val rAnkleX = rightAnkleP.x
            val rAnkleY = rightAnkleP.y


            displayAll(
                lShoulderX, lShoulderY, rShoulderX, rShoulderY,
                lElbowX, lElbowY, rElbowX, rElbowY,
                lWristX, lWristY, rWristX, rWristY,
                lHipX, lHipY, rHipX, rHipY,
                lKneeX, lKneeY, rKneeX, rKneeY,
                lAnkleX, lAnkleY, rAnkleX, rAnkleY, bitmap, lEyeX, lEyeY, rEyeX, rEyeY
            )
        } catch (e: Exception) {
        }
    }

    private fun displayAll(
        lShoulderX: Float, lShoulderY: Float, rShoulderX: Float, rShoulderY: Float,
        lElbowX: Float, lElbowY: Float, rElbowX: Float, rElbowY: Float,
        lWristX: Float, lWristY: Float, rWristX: Float, rWristY: Float,
        lHipX: Float, lHipY: Float, rHipX: Float, rHipY: Float,
        lKneeX: Float, lKneeY: Float, rKneeX: Float, rKneeY: Float,
        lAnkleX: Float, lAnkleY: Float, rAnkleX: Float, rAnkleY: Float,bitmap: Bitmap,
        lEyeX: Float, lEyeY: Float, rEyeX: Float, rEyeY: Float

    ){
        val paint = Paint()
        paint.color = Color.GREEN
        val strokeWidth = 4.0f
        paint.strokeWidth = strokeWidth
        val drawBitmap = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            bitmap.config
        )

        val canvas = Canvas(bitmap)

        canvas.drawBitmap(bitmap, 0f, 0f, null)

        canvas.drawLine(lEyeX, lEyeY, rEyeX, rEyeY, paint)

        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint)

        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint)

        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, paint)

        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint)

        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, paint)

        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, paint)

        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, paint)

        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, paint)

        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, paint)

        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, paint)

        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, paint)

        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, paint)

        this.bitmap.value=bitmap
    }



}
