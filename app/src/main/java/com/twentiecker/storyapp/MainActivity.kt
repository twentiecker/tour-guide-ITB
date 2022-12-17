package com.twentiecker.storyapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.twentiecker.storyapp.bike.BikeActivity
import com.twentiecker.storyapp.databinding.ActivityMainBinding
import com.twentiecker.storyapp.liststory.ListStoryActivity
import com.twentiecker.storyapp.liststory.ListStoryViewModel
import com.twentiecker.storyapp.maps.direction.DirectionActivity
import com.twentiecker.storyapp.model.UserPreference
import com.twentiecker.storyapp.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var listStoryViewModel: ListStoryViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#0064fe"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        setupViewModel()

        binding.cvReview.setOnClickListener {
            val intent = Intent(this@MainActivity, ListStoryActivity::class.java)
            startActivity(intent)
        }

        binding.cvMap.setOnClickListener {
            val intent = Intent(this@MainActivity, DirectionActivity::class.java)
            startActivity(intent)
        }

        binding.cvSepeda.setOnClickListener {
            val intent = Intent(this@MainActivity, BikeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                listStoryViewModel.logout()
                true
            }
            R.id.language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun setupViewModel() {
        listStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[ListStoryViewModel::class.java]

        listStoryViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                // binding ke nama user nya ya
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

}