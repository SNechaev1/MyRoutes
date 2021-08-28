package com.snechaev1.myroutes.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snechaev1.myroutes.databinding.MapBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapBottomSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: MapBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MapBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listAdapter = MapBottomSheetListAdapter(this)
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        binding.btnStartRoute.setOnClickListener {
            Timber.d("btnRoutes clicked")
        }
    }


}