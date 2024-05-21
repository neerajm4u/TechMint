package com.example.techmint.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techmint.databinding.ContributorRepositoryListItemBinding
import com.example.techmint.model.ContributorRepositoryListItem


class ContributorRepositoryListAdapter(

) : RecyclerView.Adapter<ContributorRepositoryListAdapter.ViewHolder>()  {
    private val dataList = ArrayList<ContributorRepositoryListItem>()

    fun swapData(list : List<ContributorRepositoryListItem>) {
        this.dataList.clear()
        this.dataList.addAll(list)
         notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContributorRepositoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contributorRepositoryListItemInfo = dataList[position]
        holder.bind(contributorRepositoryListItemInfo)
    }

    class ViewHolder(private val binding : ContributorRepositoryListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(contributorRepositoryListItem: ContributorRepositoryListItem) {
            val context = binding.root.context

            binding.name.text = "NAME: "+ contributorRepositoryListItem.name.toString()
            binding.id.text = "ID: "+ contributorRepositoryListItem.id.toString()

        }
    }

    override fun getItemCount(): Int = dataList.size
}