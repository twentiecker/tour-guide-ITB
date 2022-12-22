package com.twentiecker.storyapp.bike

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.ViewModelFactory
import com.twentiecker.storyapp.model.BikeModel
import com.twentiecker.storyapp.databinding.ActivityDetailBikeBinding
import com.twentiecker.storyapp.main.MainActivity
import com.twentiecker.storyapp.model.UserPreference
import com.twentiecker.storyapp.scanner.ScanActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class BikeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBikeBinding
    private lateinit var bikeViewModel: BikeViewModel
    private lateinit var bike: BikeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor(getString(R.string.blue)))
        actionBar?.setBackgroundDrawable(colorDrawable)

        setupViewModel()
        setupData()

        binding.btnRent.setOnClickListener {
            bikeViewModel.getBike().observe(this) { bikePref ->
                if (bikePref.name == "")
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Anda telah berhasil melakukan pemesanan sepeda listrik. Silahkam masuk menu QR Code untuk membuka kunci sepeda.")
                        setPositiveButton("OK") { _, _ ->
                            saveBikeData(bike)
                        }
                        create()
                        show()
                    }
                else
                    AlertDialog.Builder(this).apply {
                        setTitle("Maaf.")
                        setMessage("Satu pengguna hanya bisa menyewa satu sepeda listrik. Silahkan selesaikan masa sewa sepeda listrik terlebih dahulu sebelum melakukan sewa sepeda yang lain.")
                        setPositiveButton("OK") { _, _ ->
                            val intent = Intent(this@BikeDetailActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                        create()
                        show()
                    }
            }
        }
    }

    private fun setupData() {
        val bikeModel = intent.getParcelableExtra<BikeModel>("Hero") as BikeModel
        Glide.with(applicationContext)
            .load(bikeModel.photo)
            .into(findViewById(R.id.profileImageView))
        binding.nameTextView.text = bikeModel.name
        binding.descTextView.text = bikeModel.description
        binding.energyTextView.text = bikeModel.energy
        binding.ratingTextView.text = bikeModel.rating
        binding.speedTextView.text = bikeModel.speed

        bike = bikeModel

        bikeViewModel.getUser().observe(this) { user ->
            if (user.name == "Dr. Ir. Albarda, M.T.") {
                binding.priceTextView.text = "Free"
            } else {
                binding.priceTextView.text = "Rp 10.000"
            }
        }

    }

    private fun saveBikeData(bikeData: BikeModel?) {
        if (bikeData != null) {
            bikeViewModel.saveBike(
                BikeModel(
                    bikeData.name,
                    bikeData.description,
                    bikeData.photo,
                    bikeData.energy,
                    bikeData.rating,
                    bikeData.speed,
                    false
                )
            )
            val intent = Intent(this@BikeDetailActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewModel() {
        bikeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[BikeViewModel::class.java]
    }
}