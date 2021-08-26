package com.snechaev1.myroutes.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.snechaev1.myroutes.R
import com.snechaev1.myroutes.databinding.BaseProfileFrBinding
import com.snechaev1.myroutes.ui.routes.RoutesFragment
import com.snechaev1.myroutes.ui.statistics.StatisticsFragment

class BaseProfileFragment : Fragment() {

    private lateinit var adapter: Adapter
    private val baseProfileViewModel: BaseProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BaseProfileFrBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewPager = binding.profileViewpager
        setupViewPager(viewPager)
        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.routes)
                1 -> tab.text = getString(R.string.statistics)
            }
        }.attach()

        return binding.root
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        adapter = Adapter(context as FragmentActivity)
        adapter.addFragment(RoutesFragment())
        adapter.addFragment(StatisticsFragment())
        viewPager.adapter = adapter
    }

    internal class Adapter(frActivity: FragmentActivity) : FragmentStateAdapter(frActivity) {
        private val fragments = mutableListOf<Fragment>()
//        private val fragmentTitles = mutableListOf<String>()

        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
//            fragmentTitles.add(title)
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            val fragment = fragments[position]
            val args = Bundle()
            args.putString("parent", "profile")
            fragment.arguments = args
            return fragment
        }
    }
}