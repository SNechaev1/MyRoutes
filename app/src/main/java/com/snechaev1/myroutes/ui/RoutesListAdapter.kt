package com.snechaev1.myroutes.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.data.Route
import com.snechaev1.myroutes.databinding.RouteItemBinding

class RoutesListAdapter : PagingDataAdapter<Route, RoutesListAdapter.ViewHolder>(RouteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RouteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.titleView.text = item.description
//            holder.dateView.text = item.createdDate()
//            holder.stateView.text = item.statusString(holder.itemView.context)
            holder.itemView.setOnClickListener {
                val bundle = bundleOf("route" to item)
                it.findNavController().navigate(R.id.action_global_detail_fragment, bundle)
            }
        }
    }

    inner class ViewHolder(private val binding: RouteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.title
        val dateView: TextView = binding.itemDate
        val stateView: TextView = binding.itemState
    }

    private class RouteDiffCallback : DiffUtil.ItemCallback<Route>() {
        override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
            return oldItem.created == newItem.created
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
            return oldItem == newItem
        }
    }


}

