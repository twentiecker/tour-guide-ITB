package com.twentiecker.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var myButton: MyButton
    private lateinit var edLoginEmail: MyEditText
    private lateinit var edLoginPassword: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                if (edLoginPassword.length() < 6) edLoginPassword.error = "Password at least 6 characters"
            }

            override fun afterTextChanged(p0: Editable) {
            }
        })

        myButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "${edLoginEmail.text} and ${edLoginPassword.text}",
                Toast.LENGTH_SHORT
            ).show()
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