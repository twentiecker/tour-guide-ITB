package com.twentiecker.storyapp.bike

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.bike.adapter.ListBikeAdapter
import com.twentiecker.storyapp.model.BikeModel
import java.util.ArrayList

class BikeActivity : AppCompatActivity() {
    private lateinit var rvHeroes: RecyclerView
    private val list = ArrayList<BikeModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor(getString(R.string.blue)))
        actionBar?.setBackgroundDrawable(colorDrawable)

        rvHeroes = findViewById(R.id.rv_heroes)
        rvHeroes.setHasFixedSize(true)

        list.addAll(listBikeModels)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvHeroes.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvHeroes.layoutManager = LinearLayoutManager(this)
        }
        val listBikeAdapter = ListBikeAdapter(list)
        rvHeroes.adapter = listBikeAdapter
    }

    private val listBikeModels: ArrayList<BikeModel>
        get() {
            val dataName = resources.getStringArray(R.array.data_name)
            val dataDescription = resources.getStringArray(R.array.data_description)
            val dataPhoto = resources.getStringArray(R.array.data_photo)
            val dataEnergy = resources.getStringArray(R.array.data_energy)
            val dataRating = resources.getStringArray(R.array.data_rating)
            val dataSpeed = resources.getStringArray(R.array.data_speed)
            val listBikeModels = ArrayList<BikeModel>()
            for (i in dataName.indices) {
                val bikeModel = BikeModel(dataName[i], dataDescription[i], dataPhoto[i], dataEnergy[i], dataRating[i], dataSpeed[i], false)
                listBikeModels.add(bikeModel)
            }
            return listBikeModels
        }
}