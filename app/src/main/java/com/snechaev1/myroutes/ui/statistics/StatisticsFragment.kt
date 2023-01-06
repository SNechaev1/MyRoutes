package com.snechaev1.myroutes.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.snechaev1.myroutes.databinding.StatisticsFrBinding
import com.snechaev1.myroutes.utils.stripTrailingZeros
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private val viewModel: StatisticsViewModel by viewModels()
    private lateinit var binding: StatisticsFrBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = StatisticsFrBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch {
            viewModel.getRoutes()
        }
        return binding.root
    }

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.routes.asLiveData().observe(viewLifecycleOwner) { routes ->

            with(binding) {
                totalRoutesSize.text = routes.size.toString()
                if (routes.isNotEmpty()) {
                    fun durationMs() = routes.map { (it.finished - it.created) }.sum().div(routes.size).toDuration(DurationUnit.MILLISECONDS)
                    fun duration() = "${durationMs().inWholeHours}:${durationMs().inWholeMinutes.rem(60)}:${durationMs().inWholeSeconds.rem(60).rem(60)} "
                    averageDurationValue.text = duration()

                    val distance = routes.map { it.distance() }.sum() / routes.size
                    val distanceKm = distance.div(1000).stripTrailingZeros()+" km"
                    averageDistanceValue.text = distanceKm
                }
            }
        }

//        binding.statisticsBtn.setOnClickListener {
//            lifecycleScope.launch {
//                Timber.d("onViewCreated: statisticsBtn")
//            }
//        }

    }

}