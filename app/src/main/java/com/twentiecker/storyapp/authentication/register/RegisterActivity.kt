package com.twentiecker.storyapp.authentication.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.twentiecker.storyapp.R
import com.twentiecker.storyapp.custom.MyButton
import com.twentiecker.storyapp.custom.MyEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var myButton: MyButton
    private lateinit var edRegisterName: MyEditText
    private lateinit var edRegisterEmail: MyEditText
    private lateinit var edRegisterPassword: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        myButton.setOnClickListener {
            val name = edRegisterName.text.toString()
            val email = edRegisterEmail.text.toString()
            val pass = edRegisterPassword.text.toString()

            val service = RegisterConfig().getRegisterService().registerUser(name, email, pass)
            service.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(this@RegisterActivity, responseBody.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_SHORT).show()
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

//            Toast.makeText(
//                this@LoginActivity,
//                "${edLoginEmail.text} and ${edLoginPassword.text}",
//                Toast.LENGTH_SHORT
//            ).show()
        }
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
}