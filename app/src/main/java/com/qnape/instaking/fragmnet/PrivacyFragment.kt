package com.qnape.instaking.fragmnet

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.qnape.instaking.databinding.FragmentPrivacyBinding


class PrivacyFragment : Fragment() {
    lateinit var binding:FragmentPrivacyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPrivacyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            binding.pv.visibility = View.VISIBLE
            binding.webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.pv.visibility = View.GONE
                }
            }
            binding.webView.webChromeClient = WebChromeClient()
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.settings.setSupportZoom(true)
            binding.webView.settings.allowContentAccess = true
            binding.webView.settings.builtInZoomControls = true
            binding.webView.settings.displayZoomControls = false
            binding.webView.loadUrl("https://makepopularonsocialmedia.xyz/privacy-policy.php")

        } catch (e: Exception) {
            Log.w(TAG, "setUpNavigationView", e)
        }
    }

    companion object {
        fun newInstance(): PrivacyFragment {
            val args = Bundle()

            val fragment = PrivacyFragment()
            fragment.arguments = args
            return fragment
        }

    }
}