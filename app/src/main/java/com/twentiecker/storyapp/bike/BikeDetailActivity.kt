package com.twentiecker.storyapp.bike

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.bike.model.Bike
import com.twentiecker.storyapp.databinding.ActivityDetailBikeBinding
import com.twentiecker.storyapp.databinding.ActivityMainBinding

class BikeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBikeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor(getString(R.string.blue)))
        actionBar?.setBackgroundDrawable(colorDrawable)

        setupData()

        binding.btnRent.setOnClickListener {
            Toast.makeText(this, "Pemesanan berhasil dilakukan!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupData() {
        val bike = intent.getParcelableExtra<Bike>("Hero") as Bike
        Glide.with(applicationContext)
            .load(bike.photo)
            .into(findViewById(R.id.profileImageView))
        binding.nameTextView.text = bike.name
        binding.descTextView.text = bike.description
        binding.energyTextView.text = bike.energy
        binding.ratingTextView.text = bike.rating
        binding.speedTextView.text = bike.speed
    }
}