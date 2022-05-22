package com.qnape.instaking.fragmnet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qnape.instaking.R
import com.qnape.instaking.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {
    lateinit var binding : FragmentNotificationBinding
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

    companion object {
        fun newInstance(): NotificationFragment {
            val args = Bundle()

            val fragment = NotificationFragment()
            fragment.arguments = args
            return fragment
        }
    }
}