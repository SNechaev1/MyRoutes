package com.snechaev1.myroutes.ui.map

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snechaev1.myroutes.data.model.Route
import com.snechaev1.myroutes.databinding.RouteItemBinding

class MapBottomSheetListAdapter(
    private val fragment: BottomSheetDialogFragment
) : ListAdapter<Route, MapBottomSheetListAdapter.ViewHolder>(RouteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RouteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.titleView.text = item.description
//            holder.dateView.text = item.createdDate()
            holder.itemView.setOnClickListener {
//                val bundle = bundleOf("route" to item)
//                fragment.findNavController().navigate(R.id.nav_detail, bundle)
                fragment.dismiss()
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

