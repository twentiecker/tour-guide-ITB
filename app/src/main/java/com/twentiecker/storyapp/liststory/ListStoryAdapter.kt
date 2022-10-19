package com.twentiecker.storyapp.liststory

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.model.ListStoryItem

class ListStoryAdapter(private val listStories: List<ListStoryItem>) :
    RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStories[position])
    }

    override fun getItemCount(): Int = listStories.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.profileImageView)
        private var tvName: TextView = itemView.findViewById(R.id.nameTextView)
        private var tvDescription: TextView = itemView.findViewById(R.id.descTextView)
        private var imgStory: ImageView = itemView.findViewById(R.id.image_story)

        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(R.drawable.ic_undraw_male_avatar_re_8fid)
                .circleCrop()
                .into(imgPhoto)
            tvName.text = story.name
            tvDescription.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(imgStory)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, StoryActivity::class.java)
                intent.putExtra("Hero", story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(tvName, "name"),
                        Pair(tvDescription, "description"),
                        Pair(imgStory, "photo")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

}