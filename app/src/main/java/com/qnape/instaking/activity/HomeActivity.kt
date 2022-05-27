package com.qnape.instaking.activity

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.qnape.instaking.R
import com.qnape.instaking.databinding.ActivityHomeBinding
import com.qnape.instaking.fragmnet.HomeFragment
import com.qnape.instaking.fragmnet.NotificationFragment
import com.qnape.instaking.fragmnet.PrivacyFragment
import com.qnape.instaking.interfaces.BackPress
import com.qnape.instaking.navigation.MeowBottomNavigation
import com.qnape.instaking.util.Preferences
import java.util.*

class HomeActivity : AppCompatActivity() {
    var binding: ActivityHomeBinding? = null
    var webView: WebView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.bottomNavigation?.add(MeowBottomNavigation.Model(1,R.drawable.ic_outline_search_24))
        binding?.bottomNavigation?.add(MeowBottomNavigation.Model(2, R.drawable.ic_outline_home_24))
        binding?.bottomNavigation?.add(MeowBottomNavigation.Model(3,R.drawable.ic_outline_privacy_tip_24))
        if (Preferences.hasKey(applicationContext, Preferences.PreferencesKey.UserID.name) == false) {
            val min = 11
            val max = 999
            val random = Random().nextInt(max - min + 1) + min
            Preferences.saveIntData(applicationContext, Preferences.PreferencesKey.UserID.name, random)
        }
        binding?.bottomNavigation?.setOnShowListener {item->
            var fragment: Fragment? = null
            when(item.id){
                1->{
                    fragment = NotificationFragment.newInstance()
                }
                2->{
                    fragment = HomeFragment.newInstance()
                }
                3->{
                    fragment = PrivacyFragment.newInstance()
                }

            }
            loadFragmnet(fragment)
        }

        binding?.bottomNavigation?.show(2, true)

        binding?.bottomNavigation?.setOnClickMenuListener {

        }

        binding?.bottomNavigation?.setOnReselectListener {

        }

    }

    private fun loadFragmnet(fragment: Fragment?) {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
        }
    }

}