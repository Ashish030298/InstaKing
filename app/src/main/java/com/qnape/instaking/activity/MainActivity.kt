package com.qnape.instaking.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.qnape.instaking.R
import com.qnape.instaking.adapter.IntroAdapter
import com.qnape.instaking.databinding.ActivityMainBinding
import com.qnape.instaking.model.Intro
import com.qnape.instaking.util.Preferences
import java.util.*

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    var introList: ArrayList<Intro> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        createIntroList()
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding?.introRv)
        binding?.introRv?.adapter = IntroAdapter(introList, applicationContext)
        binding?.introRv?.addItemDecoration(LinePagerIndicatorDecoration())

        binding?.introRv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (binding?.introRv.getCurrentPosition() == introList.size -1){
                        binding?.forward?.text = "Done"
                        binding?.skipBtn?.visibility = View.GONE
                        binding?.forward?.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
                    }else {
                        binding?.skipBtn?.visibility = View.VISIBLE
                        binding?.forward?.text = ""
                        binding?.forward?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_forward,0,0,0)
                    }
                }
            }
        })

        binding?.forward?.setOnClickListener {
            if (binding?.forward?.text == "") {
                binding?.introRv?.smoothScrollToPosition(binding?.introRv.getCurrentPosition()+1)
                if (binding?.introRv.getCurrentPosition() == introList.size -2){
                    binding?.forward?.text = "Done"
                    binding?.forward?.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
                }else{
                    binding?.forward?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_forward,0,0,0)
                    binding?.skipBtn?.visibility = View.VISIBLE
                    binding?.forward?.text = ""
                }
            }else{
                openHomeActivity()
            }
        }


        binding?.skipBtn?.setOnClickListener {
            openHomeActivity()
        }
        binding?.letsGoBtn?.setOnClickListener {
            openHomeActivity()
        }
    }

    fun RecyclerView?.getCurrentPosition() : Int {
        return (this?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    fun openHomeActivity(){
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun createIntroList() {
        val introData = Intro()
        introData.img = R.drawable.img1
        introData.title = "Lighting ⚡ Fast Delivery"
        introData.subtitle = "Start Receiving Your Order within seconds after you complete checkout"
        introList.add(introData)

        val introData1 = Intro()
        introData1.img = R.drawable.img2
        introData1.title = "Increase Your Social Worth"
        introData1.subtitle = "Stop struggling to increase your social worth and take advantage of our service 😊"
        introList.add(introData1)

        val introData2 = Intro()
        introData2.img = R.drawable.img3
        introData2.title = "Boost Your Instagram Ranking Higher than Ever"
        introData2.subtitle = "Instagram is one of the fastest growing platform in the world, which is why wee have expertise you need to grow your ranking"
        introList.add(introData2)

        val introData3 = Intro()
        introData3.img = R.drawable.img4
        introData3.title = "Guaranteed Delivery"
        introData3.subtitle = "All the Services will guarantee full delivery. If problem occurs wee have 24X7 customer support."
        introList.add(introData3)


    }

    override fun onStart() {
        super.onStart()
        if (Preferences.hasKey(applicationContext, Preferences.PreferencesKey.UserID.name)){
           openHomeActivity()

        }
    }
}