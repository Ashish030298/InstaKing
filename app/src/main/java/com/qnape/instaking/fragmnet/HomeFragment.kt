package com.qnape.instaking.fragmnet

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.qnape.instaking.activity.CompleteSearviceActivity
import com.qnape.instaking.databinding.FragmentHomeFragmnetBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeFragmnetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeFragmnetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.followers.setOnClickListener {
            openCompleteActivity("Followers")
        }


        binding.likes.setOnClickListener {
            openCompleteActivity("Likes")
        }

        binding.views.setOnClickListener {
            openCompleteActivity("Views")
        }

        binding.reelView.setOnClickListener {
            openCompleteActivity("Reel Views")
        }

        binding.reelLikes.setOnClickListener {
            openCompleteActivity("Reel Likes")
        }

        binding.comments.setOnClickListener {
            openCompleteActivity("Comments")
        }



    }

    fun openCompleteActivity(type: String){
        val intent = Intent(context, CompleteSearviceActivity::class.java)
        intent.putExtra("type", type)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}