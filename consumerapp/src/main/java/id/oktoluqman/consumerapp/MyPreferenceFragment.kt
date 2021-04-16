package id.oktoluqman.consumerapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import java.util.*

class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var REMINDER: String
    private lateinit var reminderPreference: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPref: SharedPreferences, key: String) {
        when (key) {
            REMINDER -> {
                val isEnabled = sharedPref.getBoolean(REMINDER, false)
                reminderPreference.isChecked = isEnabled
                if (isEnabled) {
                    alarmReceiver.setRepeatingAlarm(
                        requireContext(),
                        "09:00",
                        "Let's find popular users on GitHub!",
                    )
                } else {
                    alarmReceiver.cancelRepeatingAlarm(requireContext())
                }
                reminderPreference.summary =
                    alarmReceiver.isAlarmSet(requireContext()).toString()
                        .capitalize(Locale.getDefault())
            }
        }
    }

    private fun setSummaries() {
        val sharedPref = preferenceManager.sharedPreferences

        reminderPreference.isChecked = sharedPref.getBoolean(REMINDER, false)
        reminderPreference.summary =
            alarmReceiver.isAlarmSet(requireContext()).toString().capitalize(Locale.getDefault())
    }

    private fun init() {
        REMINDER = resources.getString(R.string.key_reminder)

        reminderPreference = findPreference(REMINDER)!!
        alarmReceiver = AlarmReceiver()
    }
}