package com.example.redmineclient

interface Downloader {
    fun downloadFile(url: String, contentType: String, fileName: String): Long
}