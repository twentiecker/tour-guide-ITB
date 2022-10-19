package com.twentiecker.storyapp.authentication.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.ViewModelFactory
import com.twentiecker.storyapp.authentication.login.LoginActivity
import com.twentiecker.storyapp.custom.MyButton
import com.twentiecker.storyapp.custom.MyEditText
import com.twentiecker.storyapp.databinding.ActivityRegisterBinding
import com.twentiecker.storyapp.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var myButton: MyButton
    private lateinit var edRegisterName: MyEditText
    private lateinit var edRegisterEmail: MyEditText
    private lateinit var edRegisterPassword: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        myButton = findViewById(R.id.my_button)
        edRegisterName = findViewById(R.id.ed_register_name)
        edRegisterEmail = findViewById(R.id.ed_register_email)
        edRegisterPassword = findViewById(R.id.ed_register_password)

        edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

        edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
                if (p0.isEmpty()) edRegisterEmail.error = "Masukkan email"
                else if (!p0.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) edRegisterEmail.error =
                    "Format email tidak sesuai"
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

        edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
                if (p0.isEmpty()) edRegisterPassword.error = "Masukkan password"
                else if (p0.length in 1..5) edRegisterPassword.error =
                    "Password at least 6 characters"
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.myButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            registerViewModel.registerService(name, email, password)
            registerViewModel.messageData.observe(this) { message ->
                binding.progressBar.visibility = View.INVISIBLE
                if (message == "User created") {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@RegisterActivity, "Email has been registered.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.messageLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val imgName = ObjectAnimator.ofFloat(binding.imageName, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val imgEmail = ObjectAnimator.ofFloat(binding.imageEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val imgPassword =
            ObjectAnimator.ofFloat(binding.imagePassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val btnSignup = ObjectAnimator.ofFloat(binding.myButton, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.linearMessage, View.ALPHA, 1f).setDuration(500)

        val setName = AnimatorSet().apply {
            playTogether(imgName, nameEditTextLayout)
        }

        val setEmail = AnimatorSet().apply {
            playTogether(imgEmail, emailEditTextLayout)
        }

        val setPassword = AnimatorSet().apply {
            playTogether(imgPassword, passwordEditTextLayout)
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                setName,
                setEmail,
                setPassword,
                btnSignup,
                message
            )
            startDelay = 500
        }.start()
    }

    private fun setMyButtonEnable() {
        val name = edRegisterName.text
        val email = edRegisterEmail.text
        val pass = edRegisterPassword.text

        myButton.isEnabled = name != null && name.toString()
            .isNotEmpty() && email != null && email.toString()
            .isNotEmpty() && pass != null && pass.toString()
            .isNotEmpty() && edRegisterPassword.length() > 5
                && email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
    }
}