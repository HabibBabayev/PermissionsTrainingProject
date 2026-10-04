package com.example.permissionsproject

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import com.example.permissionsproject.common.photoUriCreator

@Composable
fun CameraPermission(modifier: Modifier){
    var isLocationOpen by remember { mutableStateOf(false) }
    var isCameraOpen by remember{mutableStateOf(false)}
    var photoUri by remember{mutableStateOf<Uri?>(null)}
    var takePhotoUri by remember { mutableStateOf<Uri?>(null) }
    val context= LocalContext.current
//here we allow app to go to camera app to
    val cameraOpener=rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {success->
            if (success) takePhotoUri=photoUri

        }
    )
    val permissionLauncher= rememberLauncherForActivityResult(
contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted->
            if (isGranted){
                val newUri= photoUriCreator(context)
                photoUri=newUri
                cameraOpener.launch(newUri)
            }
        }
    )
    Column(modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Button(onClick = {
            if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            )== PackageManager.PERMISSION_GRANTED){
                val newUri= photoUriCreator(context)
                photoUri=newUri
                cameraOpener.launch(newUri)
            }else permissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
            Text(text = "Take Photo")

        }
       takePhotoUri?.let {
           AsyncImage(model = it,
               contentDescription = null,
               modifier=modifier.size(300.dp))

       }
    }
}
@Composable
fun GetPhotoFromGallery(modifier: Modifier){
    var photoUri by remember{mutableStateOf<Uri?>(null)}

    val galleryLauncher=rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri->
          photoUri=uri

        }
    )
    Column(modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Button(onClick = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text(text = "take photo from gallery")
        }
        photoUri?.let {
            AsyncImage(model = it,contentDescription = null)
        }
    }





}