package com.twentiecker.storyapp.authentication.login

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
import com.twentiecker.storyapp.api.ApiResult
import com.twentiecker.storyapp.authentication.register.RegisterActivity
import com.twentiecker.storyapp.custom.MyButton
import com.twentiecker.storyapp.custom.MyEditText
import com.twentiecker.storyapp.databinding.ActivityLoginBinding
import com.twentiecker.storyapp.databinding.ActivitySsoBinding
import com.twentiecker.storyapp.main.MainActivity
import com.twentiecker.storyapp.model.DataItem
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SsoActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivitySsoBinding
    private lateinit var user: UserModel
    private lateinit var myButton: MyButton
    private lateinit var edLoginEmail: MyEditText
    private lateinit var edLoginPassword: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySsoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        myButton = findViewById(R.id.my_button)
        edLoginEmail = findViewById(R.id.ed_login_email)
        edLoginPassword = findViewById(R.id.ed_login_password)

        edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
                if (p0.isEmpty()) edLoginEmail.error = "Masukkan username"
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

        edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
                if (p0.isEmpty()) edLoginPassword.error = "Masukkan password"
                else if (p0.length in 1..5) edLoginPassword.error = "Password at least 6 characters"
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user -> this.user = user }
    }

    private fun showUserData(userData: DataItem?) {
        if (userData != null) {
            loginViewModel.saveUser(UserModel(userData.userId, userData.name, userData.token, true))
            val intent = Intent(this@SsoActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupAction() {
        binding.myButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            val email = binding.edLoginEmail.text.toString() + "@itb.ac.id"
            val password = binding.edLoginPassword.text.toString()

            loginViewModel.loginService(email, password).observe(this) { loginResult ->
                when (loginResult) {
                    is ApiResult.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, loginResult.data.message, Toast.LENGTH_SHORT).show()
                        showUserData(loginResult.data.loginResult)
                    }
                    is ApiResult.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, loginResult.error, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val imgLogin = ObjectAnimator.ofFloat(binding.imageLogin, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val imgPassword =
            ObjectAnimator.ofFloat(binding.imagePassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val buttonLogin = ObjectAnimator.ofFloat(binding.myButton, View.ALPHA, 1f).setDuration(500)
        val setEmail = AnimatorSet().apply {
            playTogether(imgLogin, emailEditTextLayout)
        }

        val setPassword = AnimatorSet().apply {
            playTogether(imgPassword, passwordEditTextLayout)
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                setEmail,
                setPassword,
                buttonLogin
            )
            startDelay = 500
        }.start()
    }

    private fun setMyButtonEnable() {
        val email = edLoginEmail.text
        val pass = edLoginPassword.text
        myButton.isEnabled = email != null && email.toString()
            .isNotEmpty() && pass != null && pass.toString()
            .isNotEmpty() && edLoginPassword.length() > 5
    }
}