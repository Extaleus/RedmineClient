package com.example.redmineclient

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import okhttp3.Credentials

class AndroidDownloader(
    context: Context
) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadFile(url: String, contentType: String, fileName: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType(contentType)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//            .setTitle("image.jpg")
            .addRequestHeader(
                "Authorization", Credentials.basic(
                    (if (App.getAuthData().first != null) {
                        App.getAuthData().first
                    } else {
                        null
                    }).toString(),
                    if (App.getAuthData().second != null) {
                        App.getAuthData().second
                    } else {
                        null
                    }.toString()
                )
            )
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        return downloadManager.enqueue(request)
    }
}