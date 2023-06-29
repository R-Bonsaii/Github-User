package com.snobos.githubuser.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.snobos.githubuser.R
import com.snobos.githubuser.databinding.SplashScreenBinding
import com.snobos.githubuser.MainActivity
import com.snobos.githubuser.viewmodel.SettingViewModel
import com.snobos.githubuser.viewmodel.ViewModelFactorySetting
import com.snobos.githubuser.ui.setting.SettingPreferences
import com.snobos.githubuser.ui.setting.dataStore

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: SplashScreenBinding
    private lateinit var ivShop: ImageView
    private lateinit var splashLayout: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelFactorySetting(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            setThemeSplashScreen(isDarkModeActive)
        }

        splashLayout = binding.splashLayout
        ivShop = binding.ivLogo

        ivShop.alpha = 0f
        ivShop.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    private fun setThemeSplashScreen(isDark: Boolean) {
        val drawableResId = if (isDark) R.drawable.github_logo_white else R.drawable.github_logo_black
        ivShop.setImageDrawable(ContextCompat.getDrawable(ivShop.context, drawableResId))

        val colorResId = if (isDark) R.color.black else R.color.white
        splashLayout.setBackgroundColor(ContextCompat.getColor(splashLayout.context, colorResId))
    }
}