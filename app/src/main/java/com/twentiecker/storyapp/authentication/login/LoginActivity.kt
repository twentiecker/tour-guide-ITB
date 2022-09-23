package com.twentiecker.storyapp.authentication.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.addstory.ApiConfig
import com.twentiecker.storyapp.addstory.FileUploadResponse
import com.twentiecker.storyapp.custom.MyButton
import com.twentiecker.storyapp.custom.MyEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var myButton: MyButton
    private lateinit var edLoginEmail: MyEditText
    private lateinit var edLoginPassword: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        myButton = findViewById(R.id.my_button)
        edLoginEmail = findViewById(R.id.ed_login_email)
        edLoginPassword = findViewById(R.id.ed_login_password)

        setMyButtonEnable()

        edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

        edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
//                if (edLoginPassword.length() < 6) edLoginPassword.error = "Password at least 6 characters"
                if (p0.length < 6) edLoginPassword.error = "Password at least 6 characters"
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

        myButton.setOnClickListener {
            val email = edLoginEmail.text.toString()
            val pass = edLoginPassword.text.toString()

            val service = LoginConfig().getLoginService().loginUser(email, pass)
            service.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "iki ta ${response.message()}", Toast.LENGTH_SHORT).show()
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

//            Toast.makeText(
//                this@LoginActivity,
//                "${edLoginEmail.text} and ${edLoginPassword.text}",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    private fun setMyButtonEnable() {
        val email = edLoginEmail.text
        val pass = edLoginPassword.text
        myButton.isEnabled = email != null && email.toString()
            .isNotEmpty() && pass != null && pass.toString()
            .isNotEmpty() && edLoginPassword.length() > 5
    }
}