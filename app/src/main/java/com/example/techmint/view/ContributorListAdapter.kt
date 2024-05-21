package com.example.techmint.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.techmint.R
import com.example.techmint.databinding.RecyclerviewItemContributorBinding
import com.example.techmint.model.ContributorsListResponse


class ContributorListAdapter(
    private val contributorListAdapterListener: Listener
) : RecyclerView.Adapter<ContributorListAdapter.ViewHolder>()  {
    private val dataList = ArrayList<ContributorsListResponse>()

    fun swapData(list : List<ContributorsListResponse>) {
        this.dataList.clear()
        this.dataList.addAll(list)
         notifyDataSetChanged()
    }
    fun clearData() {
        this.dataList.clear()
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
        fun bind(contrbutorItemInfo: ContributorsListResponse,  contributorItemListener: Listener) {
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
        fun onContributorSelected(item : ContributorsListResponse)
    }



    override fun getItemCount(): Int = dataList.size
}