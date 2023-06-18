package com.amrullaev.myolx.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amrullaev.myolx.MainActivity
import com.amrullaev.myolx.R
import com.amrullaev.myolx.databinding.ActivitySplashBinding
import com.amrullaev.myolx.sharedPreferences.YourPreference
import com.amrullaev.myolx.utils.SharedPref
import com.amrullaev.myolx.utils.languageUpdate
import java.util.concurrent.Executors

class SplashActivity : AppCompatActivity() {
    private lateinit var yourPreference: YourPreference
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lang = SharedPref.getLanguage(this) ?: "uz"
        Log.d("TAG", "onCreate: $lang")
        languageUpdate(requireActivity = this, language = lang)

        yourPreference = YourPreference.getInstance(binding.root.context)
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(2000)
            if (yourPreference.getData("phone")!!.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}