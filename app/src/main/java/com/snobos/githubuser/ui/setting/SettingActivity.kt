package com.snobos.githubuser.ui.setting

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.snobos.githubuser.databinding.ActivitySettingBinding
import com.snobos.githubuser.viewmodel.SettingViewModel
import com.snobos.githubuser.viewmodel.ViewModelFactorySetting

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchTheme = binding.switchTheme
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactorySetting(pref))[SettingViewModel::class.java]

        val themeObserver = { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        settingViewModel.getThemeSettings().observe(this, themeObserver)

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}