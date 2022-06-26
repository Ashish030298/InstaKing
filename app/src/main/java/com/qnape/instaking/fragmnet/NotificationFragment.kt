package com.qnape.instaking.fragmnet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.qnape.instaking.activity.HomeActivity
import com.qnape.instaking.databinding.FragmentNotificationBinding
import com.qnape.instaking.interfaces.BackPress
import com.qnape.instaking.util.MyWebViewClient
import com.qnape.instaking.util.NetworkState


class NotificationFragment : Fragment() {
    lateinit var binding : FragmentNotificationBinding
    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container,false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = binding.webView
        binding.pb.max = 100
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            webView.settings.javaScriptEnabled = true
            webView.settings.useWideViewPort = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.setSupportZoom(true)
            webView.settings.setSupportMultipleWindows(true)
            webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            webView.setBackgroundColor(Color.WHITE)

            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress < 100 && binding.pb.visibility == View.GONE) {
                        binding.pb.visibility = View.VISIBLE
                        binding.pb.progress = newProgress
                    }
                    if (newProgress == 100) {
                        binding.pb.visibility = ProgressBar.GONE
                    }else{
                        binding.pb.visibility = ProgressBar.VISIBLE
                    }
                }

            }

            webView.webViewClient = MyWebViewClient()
            webView.loadUrl("https://istagmly.blogspot.com/?m=1")

        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()){
                    webView.goBack()
                }
            }
        })
        binding.searchEngine.requestFocus()
        binding.searchEngine.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchBtn.performClick()
                return@OnEditorActionListener true
            }
            false

        } )
        binding.searchBtn.setOnClickListener {
            try {
                if (!context?.let { it1 -> NetworkState.connectionAvailable(it1) }!!) {
                    Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val inputMethodManager: InputMethodManager? = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    //                    inputMethodManager?.hideSoftInputFromWindow(binding.searchEngine.windowToken, 0)
                    webView.loadUrl("https://www.google.co.in/search?q=" + binding.searchEngine.text.toString())
    //                    binding.searchEngine.setText("")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
                view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()){
                        webView.goBack()
                    }
                    return true
                }
                return false
            }
        })
    }

    companion object {
        fun newInstance(): NotificationFragment {
            val args = Bundle()
            val fragment = NotificationFragment()
            fragment.arguments = args
            return fragment
        }
    }


}