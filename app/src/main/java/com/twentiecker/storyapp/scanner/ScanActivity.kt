package com.twentiecker.storyapp.scanner

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.ViewModelFactory
import com.twentiecker.storyapp.bike.BikeActivity
import com.twentiecker.storyapp.databinding.ActivityScanBinding
import com.twentiecker.storyapp.main.MainActivity
import com.twentiecker.storyapp.model.UserPreference
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var scanViewModel: ScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor(getString(R.string.blue)))
        actionBar?.setBackgroundDrawable(colorDrawable)

        codeScanner()
        setPermission()
        setupViewModel()
    }

    private fun setupViewModel() {
        scanViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[ScanViewModel::class.java]

        scanViewModel.getUser().observe(this) {user ->
            binding.cnameTextView.text = user.name
            binding.cuserIdTextView.text = user.userId
        }

        scanViewModel.getBike().observe(this) { bike ->
            if (bike.name == "") {
                binding.detailTextView.visibility  = View.INVISIBLE
                binding.nameTextView.visibility = View.INVISIBLE
                binding.cnameTextView.visibility = View.INVISIBLE
                binding.bikeTextView.visibility = View.INVISIBLE
                binding.cbikeTextView.visibility = View.INVISIBLE
                binding.userIdTextView.visibility = View.INVISIBLE
                binding.cuserIdTextView.visibility = View.INVISIBLE
                binding.durationTextView.visibility = View.INVISIBLE
                binding.cdurationTextView.visibility = View.INVISIBLE
                binding.tvOutput.visibility = View.INVISIBLE
                binding.imgOutput.visibility = View.INVISIBLE
                binding.imgEmpty.visibility = View.VISIBLE

                AlertDialog.Builder(this).apply {
                    setTitle("Maaf.")
                    setMessage("Anda tidak memiliki data sewa sepeda. Silahkan lakukan transaksi sewa sepeda terlebih dahulu. Terima kasih.")
                    setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this@ScanActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    create()
                    show()
                }
            } else {
                binding.detailTextView.visibility  = View.VISIBLE
                binding.nameTextView.visibility = View.VISIBLE
                binding.cnameTextView.visibility = View.VISIBLE
                binding.bikeTextView.visibility = View.VISIBLE
                binding.cbikeTextView.visibility = View.VISIBLE
                binding.cbikeTextView.text = bike.name
                binding.userIdTextView.visibility = View.VISIBLE
                binding.cuserIdTextView.visibility = View.VISIBLE
                binding.durationTextView.visibility = View.VISIBLE
                binding.cdurationTextView.visibility = View.VISIBLE
                binding.tvOutput.visibility = View.VISIBLE
                binding.imgOutput.visibility = View.VISIBLE
                binding.imgEmpty.visibility = View.INVISIBLE
            }
        }
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scanner)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    binding.tvOutput.text = "Unlocked"
                    binding.tvOutput.setTextColor(Color.parseColor(getString(R.string.blue)))
                    binding.imgOutput.setImageResource(R.drawable.ic_baseline_lock_open_24)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }

            binding.scanner.setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.startPreview()
    }

    private fun setPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeReq()
        }
    }

    private fun makeReq() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA), 101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Dibutuhkan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}