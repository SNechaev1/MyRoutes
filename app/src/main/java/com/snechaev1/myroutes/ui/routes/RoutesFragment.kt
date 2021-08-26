package com.snechaev1.myroutes.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.snechaev1.myroutes.databinding.RoutesFrBinding
import com.snechaev1.myroutes.ui.RoutesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RoutesFragment : Fragment() {

    private val viewModel: RoutesViewModel by viewModels()
    lateinit var binding: RoutesFrBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = RoutesFrBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listAdapter = RoutesListAdapter()
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        val checkedRadioButtonId = binding.rgRouteType.checkedRadioButtonId // Returns View.NO_ID if nothing is checked.
        viewLifecycleOwner.lifecycleScope.launch { getRouteId(checkedRadioButtonId) }

        binding.rgRouteType.setOnCheckedChangeListener { group, checkedId ->
            viewLifecycleOwner.lifecycleScope.launch { getRouteId(checkedId) }
            Timber.d("checkedRadioButtonId: $checkedId ")
        }

        viewModel.routes.observe(viewLifecycleOwner) { data ->
            viewLifecycleOwner.lifecycleScope.launch {
                listAdapter.submitData(data)
            }
        }
    }

    private suspend fun getRouteId(checkedId: Int) {
//        when(checkedId) {
//            R.id.radio_btn_active -> viewModel.getRoute(Route.ACTIVE)
//            R.id.radio_btn_finished -> viewModel.getRoute(Route.FINISHED)
//            else -> viewModel.getRoute(Route.ACTIVE)
//        }
    }

}
