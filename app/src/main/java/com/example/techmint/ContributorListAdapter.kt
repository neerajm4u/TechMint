package com.rebeltt.app.grn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.techmint.R
import com.example.techmint.databinding.RecyclerviewItemContributorBinding
import com.example.techmint.databinding.RepositoryItemLayoutBinding
import com.example.techmint.model.ContributorResponse
import com.example.techmint.model.Items


class ContributorListAdapter(
    private val contributorListAdapterListener: Listener
) : RecyclerView.Adapter<ContributorListAdapter.ViewHolder>()  {
    private val dataList = ArrayList<ContributorResponse>()

    fun swapData(list : List<ContributorResponse>) {
        this.dataList.clear()
        this.dataList.addAll(list)
         notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewItemContributorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contributorItemInfo = dataList[position]
        holder.bind(contributorItemInfo,  contributorListAdapterListener)
    }

    class ViewHolder(private val binding : RecyclerviewItemContributorBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(contrbutorItemInfo: ContributorResponse,  contributorItemListener: Listener) {
            val context = binding.root.context

            binding.name.text = "NAME: "+ contrbutorItemInfo.login.toString()
            Glide.with(context)
                .load(contrbutorItemInfo.avatarUrl) // image url
                .placeholder(R.drawable.dotted_line_square_background) // any placeholder to load at start
                .error(R.drawable.dotted_line_square_background)  // any image in case of error
                .override(50, 50) // resizing
                .centerCrop()
                .into(binding.image);
            binding.root.setOnClickListener {
                contributorItemListener.onContributorSelected(contrbutorItemInfo)
            }
        }
    }

    interface Listener {
        fun onContributorSelected(item : ContributorResponse)
    }



    override fun getItemCount(): Int = dataList.size
}