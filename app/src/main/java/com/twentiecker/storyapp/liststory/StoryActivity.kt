package com.twentiecker.storyapp.liststory

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.model.ListStoryItem

class StoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#0064fe"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        setupData()
    }

    private fun setupData() {
        val story = intent.getParcelableExtra<ListStoryItem>("Hero")
        if (story != null) {
            Glide.with(applicationContext)
                .load(story.photoUrl)
                .circleCrop()
                .into(findViewById(R.id.image_story))
        }
        if (story != null) {
            findViewById<TextView>(R.id.nameTextView).text = story.name
        }
        if (story != null) {
            findViewById<TextView>(R.id.descTextView).text = story.description
        }
    }
}