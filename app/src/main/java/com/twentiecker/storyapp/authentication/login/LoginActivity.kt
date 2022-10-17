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
import com.twentiecker.storyapp.custom.MyButton
import com.twentiecker.storyapp.custom.MyEditText
import com.twentiecker.storyapp.databinding.ActivityLoginBinding
import com.twentiecker.storyapp.liststory.ListStoryActivity
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel

    private lateinit var myButton: MyButton
    private lateinit var edLoginEmail: MyEditText
    private lateinit var edLoginPassword: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        // custom initialisasi
        myButton = findViewById(R.id.my_button)
        edLoginEmail = findViewById(R.id.ed_login_email)
        edLoginPassword = findViewById(R.id.ed_login_password)

        setMyButtonEnable()

        edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
                if (!p0.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) edLoginEmail.error =
                    "Format email tidak sesuai"
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

        edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
                // if (edLoginPassword.length() < 6) edLoginPassword.error = "Password at least 6 characters"
                if (p0.length < 6) edLoginPassword.error = "Password at least 6 characters"
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

        loginViewModel.getUser().observe(this, { user ->
            this.user = user
        })

        loginViewModel.userData.observe(this, { userData -> showUserData(userData) })
    }

    private fun showUserData(userData: DataItem?) {
        Toast.makeText(this, userData.toString(), Toast.LENGTH_SHORT).show()
        if (userData != null) {
            loginViewModel.saveUser(UserModel(userData.userId, userData.name, userData.token, true))
            val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupAction() {
        binding.myButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edLoginPassword.error = "Masukkan password"
                }
//                email != user.email -> {
//                    binding.edLoginEmail.error = "Email tidak sesuai"
//                }
//                password != user.password -> {
//                    binding.edLoginPassword.error = "Password tidak sesuai"
//                }
                else -> {
                    loginViewModel.loginService(email, password)
                    Toast.makeText(
                        this@LoginActivity,
                        "Anda berhasil login. Sedang memuat data...",
                        Toast.LENGTH_SHORT
                    ).show()
//                    AlertDialog.Builder(this).apply {
//                        setTitle("Yeah!")
//                        setMessage("Anda berhasil login. Sedang memuat data...")
//                        create()
//                        show()
//                    }
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

        // val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        // val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        // val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        // val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.myButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            // playSequentially(title, message, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, login)
            playSequentially(emailEditTextLayout, passwordEditTextLayout, login)
            startDelay = 500
        }.start()
    }

    private fun setMyButtonEnable() {
        val email = edLoginEmail.text
        val pass = edLoginPassword.text
        myButton.isEnabled = email != null && email.toString()
            .isNotEmpty() && pass != null && pass.toString()
            .isNotEmpty() && edLoginPassword.length() > 5
                && email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
    }

    private fun loginService(email: String, pass: String) {
        val service = LoginConfig().getLoginService().loginUser(email, pass)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(
                            this@LoginActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Id tidak ditemukan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
}