package com.qnape.instaking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qnape.instaking.databinding.IntroItemBinding
import com.qnape.instaking.model.Intro

class IntroAdapter(val introList: ArrayList<Intro>, val context: Context): RecyclerView.Adapter<IntroAdapter.IntroViewHolder>() {
    class IntroViewHolder(val binding: IntroItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val binding = IntroItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return IntroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        val item = introList[position]
        item.img?.let { holder.binding.introItemImg.setImageResource(it) }
        holder.binding.introTitle.text = item.title
        holder.binding.introDescription.text = item.subtitle
    }

    override fun getItemCount(): Int {
        return introList.size
    }
}