package com.amrullaev.myolx.registratsiya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.amrullaev.myolx.R
import com.amrullaev.myolx.databinding.ActivityLoginBinding
import com.amrullaev.myolx.sharedPreferences.YourPreference
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var yourPreference: YourPreference = YourPreference.getInstance(binding.root.context)
        var auth: FirebaseAuth = FirebaseAuth.getInstance()
        binding.numberBtn.setOnClickListener {
            binding.progressbar.visibility = View.VISIBLE
            binding.numberBtn.visibility = View.INVISIBLE
            val number = binding.telNumber.text.toString()
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.putExtra("telnumber", number)
            intent.putExtra("username", number)
            startActivity(intent)
            finish()
        }
    }
}