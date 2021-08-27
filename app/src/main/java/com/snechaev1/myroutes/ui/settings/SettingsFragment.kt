package com.snechaev1.myroutes.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val notificationPreference = SwitchPreferenceCompat(context).apply {
            key = "notifications"
            title = "Enable message notifications"
            setOnPreferenceChangeListener { preference, checked ->
                val enable = checked as Boolean
                viewLifecycleOwner.lifecycleScope.launch {
                    if (enable) viewModel.setNotification(enable)
                }
                Timber.d("newValue: $checked")
                true
            }
        }

        val notificationCategory = PreferenceCategory(context).apply {
            key = "notifications_category"
            title = "Notifications"
        }
        screen.addPreference(notificationCategory)
        notificationCategory.addPreference(notificationPreference)

        val feedbackPreference = Preference(context).apply {
            key = "rate"
            title = "Rate My Routes"
            summary = "Please rate My Routes app in Google Play Store"
            setOnPreferenceClickListener {
                rateApp()
                true
            }
        }

        val helpCategory = PreferenceCategory(context).apply {
            key = "feedback"
            title = "Feedback"
        }
        screen.addPreference(helpCategory)
        helpCategory.addPreference(feedbackPreference)

        preferenceScreen = screen

    }

    private fun rateApp() {
        activity?.let { context ->
            val manager =  ReviewManagerFactory.create(context)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = task.result

                    val flow = manager.launchReviewFlow(context, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }

                } else {
                    // There was some problem, log or handle the error code.
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}
