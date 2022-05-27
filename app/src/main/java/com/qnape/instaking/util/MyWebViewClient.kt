package com.qnape.instaking.util

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MyWebViewClient : WebViewClient() {

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        if (url != null) {
            view.loadUrl(url)
        }
        CookieManager.getInstance().setAcceptCookie(true)
        return true
    }
}