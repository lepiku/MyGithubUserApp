package id.oktoluqman.mygithubuserapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var REMINDER: String
    private lateinit var reminderPreference: SwitchPreference

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
            REMINDER -> reminderPreference.isChecked = sharedPref.getBoolean(REMINDER, false)
        }
    }

    private fun setSummaries() {
        val sharedPref = preferenceManager.sharedPreferences

        reminderPreference.isChecked = sharedPref.getBoolean(REMINDER, false)
    }

    private fun init() {
        REMINDER = resources.getString(R.string.key_reminder)

        reminderPreference = findPreference(REMINDER)!!
    }
}