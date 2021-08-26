package com.snechaev1.myroutes.ui

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat() {

//    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//        setPreferencesFromResource(R.xml.settings_fr, rootKey)
//    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)

        val notificationPreference = SwitchPreferenceCompat(context)
        notificationPreference.key = "notifications"
        notificationPreference.title = "Enable message notifications"

        val notificationCategory = PreferenceCategory(context).apply {
            key = "notifications_category"
            title = "Notifications"
        }
        screen.addPreference(notificationCategory)
        notificationCategory.addPreference(notificationPreference)

        val feedbackPreference = Preference(context).apply {
            key = "feedback"
            title = "Send feedback"
            summary = "Report technical issues or suggest new features"
        }
        feedbackPreference.setOnPreferenceClickListener {
            Toast.makeText(context, "feedback send success", Toast.LENGTH_LONG).show()
            true
        }

        val helpCategory = PreferenceCategory(context).apply {
            key = "help"
            title = "Help"
        }
        screen.addPreference(helpCategory)
        helpCategory.addPreference(feedbackPreference)

        preferenceScreen = screen

    }


}
