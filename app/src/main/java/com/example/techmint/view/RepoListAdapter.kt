package com.example.techmint.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techmint.databinding.RepositoryItemLayoutBinding
import com.example.techmint.model.Items


class RepoListAdapter(
    private val repositoryListAdapterListener: Listener
) : RecyclerView.Adapter<RepoListAdapter.ViewHolder>()  {
    private val dataList = ArrayList<Items>()

    fun swapData(list : List<Items>) {
        this.dataList.clear()
        this.dataList.addAll(list)
         notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RepositoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchItemInfo = dataList[position]
        holder.bind(searchItemInfo,  repositoryListAdapterListener)
    }

    class ViewHolder(private val binding : RepositoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(searchItemInfo: Items,  repoItemListener: Listener) {
            val context = binding.root.context
            binding.id.text = "ID: " +searchItemInfo.id.toString()
            binding.name.text = "NAME: "+ searchItemInfo.name.toString()
            binding.owner.text =  "OWNER: "+searchItemInfo.owner?.login.toString()
            binding.description.text = "DESCRIPTION: "+searchItemInfo.description.toString()
            binding.root.setOnClickListener {
                repoItemListener.onRepoItemSelected(searchItemInfo)
            }
        }
    }

    interface Listener {
        fun onRepoItemSelected(item : Items)
    }



    override fun getItemCount(): Int = dataList.size
}