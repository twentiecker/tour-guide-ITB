package com.twentiecker.storyapp.scanner

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.twentiecker.storyapp.databinding.ActivityPeduliDetailBinding
import com.twentiecker.storyapp.main.MainActivity

class PeduliDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeduliDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeduliDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.imgClose.setOnClickListener {
            val intent = Intent(this@PeduliDetailActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}