package com.twentiecker.storyapp.liststory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.ViewModelFactory
import com.twentiecker.storyapp.addstory.AddStoryActivity
import com.twentiecker.storyapp.databinding.ActivityListStoryBinding
import com.twentiecker.storyapp.liststory.adapter.LoadingStateAdapter
import com.twentiecker.storyapp.maps.MapsActivity
import com.twentiecker.storyapp.model.ListStoryItem
import com.twentiecker.storyapp.model.UserPreference
import com.twentiecker.storyapp.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ListStoryActivity : AppCompatActivity() {
    private lateinit var listStoryViewModel: ListStoryViewModel
    private lateinit var binding: ActivityListStoryBinding
    private lateinit var rvHeroes: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#0064fe"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        binding.progressBar.visibility = View.VISIBLE
        setupViewModel()
        playAnimation()

        rvHeroes = findViewById(R.id.rv_heroes)
        rvHeroes.setHasFixedSize(true)

        val fab: FloatingActionButton = findViewById(R.id.fab_add_story)
        fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
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
            R.id.map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun setupViewModel() {
        listStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[ListStoryViewModel::class.java]

        listStoryViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                listStoryViewModel.listStory(StringBuilder("Bearer ").append(user.token).toString())
                    .observe(this) { listStory ->
                        binding.progressBar.visibility = View.INVISIBLE
                        showRecyclerList(listStory)
                    }
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

//        listStoryViewModel.listStory.observe(this) { listStory -> showRecyclerList(listStory) }
//        listStoryViewModel.messageData.observe(this) { message ->
//            binding.progressBar.visibility = View.INVISIBLE
//            Toast.makeText(
//                this@ListStoryActivity,
//                message.toString(),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }

    private fun playAnimation() {
        val rvStory =
            ObjectAnimator.ofFloat(binding.rvHeroes, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(rvStory)
            startDelay = 500
        }.start()
    }

    private fun showRecyclerList(list: PagingData<ListStoryItem>) {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvHeroes.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvHeroes.layoutManager = LinearLayoutManager(this)
        }

        val listStoryAdapter = ListStoryAdapter()
        rvHeroes.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoryAdapter.retry()
            }
        )
        listStoryAdapter.submitData(lifecycle, list)
    }

}