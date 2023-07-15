package com.mainapp.droneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mainapp.droneapp.databinding.ActivityRegisterBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val email = binding.regEmailField.text.toString()
            val name = binding.regNameLabel.text.toString()
            val password = binding.regPassField.text.toString()
            val repeat = binding.regPassRepeatField.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repeat.isNotEmpty()) {
                if (password == repeat) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val registerIntent = Intent(this, LoginActivity::class.java)
                                startActivity(registerIntent)
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        binding.fab003.setOnClickListener {
            val returnIntent = Intent(this, IntroActivity::class.java)
            startActivity(returnIntent)
        }
    }
}
