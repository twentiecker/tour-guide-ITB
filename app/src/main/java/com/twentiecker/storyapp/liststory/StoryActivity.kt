package com.twentiecker.storyapp.liststory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twentiecker.storyapp.R
//import com.twentiecker.storyapp.liststory.model.Story

class StoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero)

        setupData()
    }

    private fun setupData() {
        val story = intent.getParcelableExtra<ListStoryItem>("Hero")
        if (story != null) {
            Glide.with(applicationContext)
                .load(story.photoUrl)
                .circleCrop()
                .into(findViewById(R.id.profileImageView))
        }
        if (story != null) {
            findViewById<TextView>(R.id.nameTextView).text = story.name
        }
        if (story != null) {
            findViewById<TextView>(R.id.descTextView).text = story.description
        }
    }
}