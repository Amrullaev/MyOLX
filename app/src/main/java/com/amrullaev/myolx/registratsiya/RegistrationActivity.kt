package com.amrullaev.myolx.registratsiya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.amrullaev.myolx.MainActivity
import com.amrullaev.myolx.R
import com.amrullaev.myolx.databinding.ActivityRegistrationBinding
import com.amrullaev.myolx.sharedPreferences.YourPreference
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var yourPreference: YourPreference
    private var phone: String = ""
    private var username: String = ""
    private var verificationId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        yourPreference = YourPreference.getInstance(binding.root.context)
        auth = FirebaseAuth.getInstance()

        binding.btnSms.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            binding.btnSms.visibility = View.INVISIBLE
            verifyCode(binding.code.text.toString())
        }

    }

    override fun onStart() {
        super.onStart()
        phone = intent.getStringExtra("telnumber").toString()
        username = intent.getStringExtra("username").toString()
        sendVerificationCode("+998$phone")
    }

    override fun onResume() {
        super.onResume()
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.time.text = "00:" + (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                binding.time.text = "00:00"
            }
        }.start()
    }

    fun sendVerificationCode(phoneNumber: String?) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks = object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            binding.progress.visibility = View.GONE
            binding.btnSms.visibility = View.VISIBLE
            verificationId = p0
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            val code = p0.smsCode
            if (code != null) {
                binding.progress.visibility = View.GONE
                binding.btnSms.visibility = View.VISIBLE
                binding.code.setText(code.toString())
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@RegistrationActivity, p0.message, Toast.LENGTH_SHORT).show()
            binding.progress.visibility = View.GONE
            binding.btnSms.visibility = View.VISIBLE
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    yourPreference.saveData("phone", phone)
                    yourPreference.saveData("username", username)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }


}