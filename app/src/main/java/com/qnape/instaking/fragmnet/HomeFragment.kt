package com.qnape.instaking.fragmnet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.adcolony.sdk.*
import com.qnape.instaking.activity.CompleteSearviceActivity
import com.qnape.instaking.databinding.FragmentHomeFragmnetBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeFragmnetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val APP_ID = "appf741e48e8d914eb894"
    private val ZONE_ID = "vz55c97479e5404660a8"
    private val TAG = "AdColonyBannerDemo"

    private var listener: AdColonyAdViewListener? = null
    private var adOptions: AdColonyAdOptions? = null
    private var adContainer: RelativeLayout? = null
    private var adView: AdColonyAdView? = null
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
        loadBannerAd()

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

    private fun loadBannerAd() {
        adContainer = binding.adContainer
        if (adContainer!!.childCount > 0) {
            adContainer!!.removeView(adView)
        }
        requestBannerAd()
        val appOptions = AdColonyAppOptions()
        AdColony.configure(context as Activity,APP_ID)

        listener = object : AdColonyAdViewListener() {
            override fun onRequestFilled(adColonyAdView: AdColonyAdView) {
                Log.d(TAG, "onRequestFilled")
                adContainer!!.addView(adColonyAdView)
                adView = adColonyAdView
            }

            override fun onRequestNotFilled(zone: AdColonyZone) {
                super.onRequestNotFilled(zone)
                Log.d(TAG, "onRequestNotFilled")
            }

            override fun onOpened(ad: AdColonyAdView) {
                super.onOpened(ad)
                Log.d(TAG, "onOpened")
            }

            override fun onClosed(ad: AdColonyAdView) {
                super.onClosed(ad)
                Log.d(TAG, "onClosed")
            }

            override fun onClicked(ad: AdColonyAdView) {
                super.onClicked(ad)
                Log.d(TAG, "onClicked")
            }

            override fun onLeftApplication(ad: AdColonyAdView) {
                super.onLeftApplication(ad)
                Log.d(TAG, "onLeftApplication")
            }
        }
    }
    private fun requestBannerAd() {
        // Optional Ad specific options to be sent with request
        adOptions = AdColonyAdOptions()

        //Request Ad
        AdColony.requestAdView(ZONE_ID, listener!!, AdColonyAdSize.BANNER, adOptions)
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