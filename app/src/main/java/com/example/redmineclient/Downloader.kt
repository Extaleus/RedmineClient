package com.example.redmineclient

interface Downloader {
    fun downloadFile(url: String): Long
}