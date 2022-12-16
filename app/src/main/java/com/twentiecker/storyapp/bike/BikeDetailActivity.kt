package com.twentiecker.storyapp.bike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.bike.model.Bike

class BikeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_bike)

        setupData()
    }

    private fun setupData() {
        val bike = intent.getParcelableExtra<Bike>("Hero") as Bike
        Glide.with(applicationContext)
            .load(bike.photo)
            .into(findViewById(R.id.profileImageView))
        findViewById<TextView>(R.id.nameTextView).text = bike.name
        findViewById<TextView>(R.id.descTextView).text = bike.description
    }
}