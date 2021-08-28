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
import com.snechaev1.myroutes.data.model.Route
import com.snechaev1.myroutes.databinding.RouteItemBinding
import kotlin.time.ExperimentalTime

class RoutesListAdapter : PagingDataAdapter<Route, RoutesListAdapter.ViewHolder>(RouteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RouteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @ExperimentalTime
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            with(holder) {
                descriptionView.text = item.description
                distanceView.text = item.distanceKm()
                dateView.text = item.createdDate()
                durationView.text = item.duration()
                itemView.setOnClickListener {
                    val bundle = bundleOf("route" to item)
                    it.findNavController().navigate(R.id.action_global_detail_fragment, bundle)
                }
            }
        }
    }

    inner class ViewHolder(private val binding: RouteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val descriptionView: TextView = binding.itemDescription
        val distanceView: TextView = binding.itemDistance
        val dateView: TextView = binding.itemDate
        val durationView: TextView = binding.itemDuration
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

