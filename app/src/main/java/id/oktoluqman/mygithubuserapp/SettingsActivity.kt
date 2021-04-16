package id.oktoluqman.mygithubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.SwitchPreference

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction().add(R.id.settings_holder, MyPreferenceFragment()).commit()
    }
}