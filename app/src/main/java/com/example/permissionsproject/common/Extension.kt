package com.example.permissionsproject.common

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
//conversion of photo taken by camera to Uri to be used by App
fun photoUriCreator(context: Context): Uri{

    val imageFile=File.createTempFile(
        "temp_photo_file",
        ".jpg",
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) as File?
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )

}