package com.example.projeto

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Button

class VisualizarActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_quarta_tela)

        webView = findViewById(R.id.webview)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        webView.loadUrl("https://firebasestorage.googleapis.com/v0/b/autorizacao-3689b.appspot.com/o/images%2F1000081669?alt=media&token=dbd0c164-8944-4fb0-8e38-80d4ff6dfff2")

        val downloadButton = findViewById<Button>(R.id.download_button)
        downloadButton.setOnClickListener {
            val documentUrl = "https://firebasestorage.googleapis.com/v0/b/autorizacao-3689b.appspot.com/o/images%2F1000081669?alt=media&token=dbd0c164-8944-4fb0-8e38-80d4ff6dfff2"
            val fileName = "documento.pdf"
            downloadDocument(this, documentUrl, fileName)
        }
    }

    private fun downloadDocument(context: Context, url: String, fileName: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        downloadManager.enqueue(request)
    }
}