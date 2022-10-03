package com.twentiecker.storyapp.authentication.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.ViewModelFactory
import com.twentiecker.storyapp.custom.MyButton
import com.twentiecker.storyapp.custom.MyEditText
import com.twentiecker.storyapp.databinding.ActivityRegisterBinding
import com.twentiecker.storyapp.model.UserModel
import com.twentiecker.storyapp.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var signupViewModel: SignupViewModel

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

        setMyButtonEnable()

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
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

        edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
//                if (edLoginPassword.length() < 6) edLoginPassword.error = "Password at least 6 characters"
                if (p0.length < 6) edRegisterPassword.error = "Password at least 6 characters"
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

//        myButton.setOnClickListener {
//            val name = edRegisterName.text.toString()
//            val email = edRegisterEmail.text.toString()
//            val pass = edRegisterPassword.text.toString()
//
//            val service = RegisterConfig().getRegisterService().registerUser(name, email, pass)
//            service.enqueue(object : Callback<RegisterResponse> {
//                override fun onResponse(
//                    call: Call<RegisterResponse>,
//                    response: Response<RegisterResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        if (responseBody != null && !responseBody.error) {
//                            Toast.makeText(
//                                this@RegisterActivity,
//                                responseBody.message,
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                        }
//                    } else {
//                        Toast.makeText(
//                            this@RegisterActivity,
//                            response.message(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                    Toast.makeText(
//                        this@RegisterActivity,
//                        "Id tidak ditemukan",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            })
//
////            Toast.makeText(
////                this@LoginActivity,
////                "${edLoginEmail.text} and ${edLoginPassword.text}",
////                Toast.LENGTH_SHORT
////            ).show()
//        }
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
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]
    }

    private fun setupAction() {
        binding.myButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.edRegisterName.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.edRegisterEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edRegisterPassword.error = "Masukkan password"
                }
                else -> {
                    registerService(name, email, password)
                    signupViewModel.saveUser(UserModel(name, email, password, false))
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akunnya sudah jadi nih. Yuk, login dan belajar coding.")
                        setPositiveButton("Lanjut") { _, _ ->
                            finish()
                        }
                        create()
                        show()
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

//        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
//        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
//        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
//        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.myButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
//                title,
//                nameTextView,
                nameEditTextLayout,
//                emailTextView,
                emailEditTextLayout,
//                passwordTextView,
                passwordEditTextLayout,
                signup
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
    }

    private fun registerService(name: String, email: String, pass: String) {
        val service = RegisterConfig().getRegisterService().registerUser(name, email, pass)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Id tidak ditemukan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}