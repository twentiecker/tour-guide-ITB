package com.twentiecker.storyapp.bike.adapter

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
import com.twentiecker.storyapp.bike.BikeDetailActivity
import com.twentiecker.storyapp.model.BikeModel

class ListBikeAdapter(private val listBikeModels: ArrayList<BikeModel>) : RecyclerView.Adapter<ListBikeAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_bike, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listBikeModels[position])
    }

    override fun getItemCount(): Int = listBikeModels.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.profileImageView)
        private var tvName: TextView = itemView.findViewById(R.id.nameTextView)
        private var tvDescription: TextView = itemView.findViewById(R.id.descTextView)

        fun bind(bikeModel: BikeModel) {
            Glide.with(itemView.context)
                .load(bikeModel.photo)
                .into(imgPhoto)
            tvName.text = bikeModel.name
            tvDescription.text = bikeModel.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, BikeDetailActivity::class.java)
                intent.putExtra("Hero", bikeModel)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "profile"),
                        Pair(tvName, "name"),
                        Pair(tvDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

}