package com.mainapp.droneapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mainapp.droneapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLoginMainpage.setOnClickListener {
            val email = binding.loginEmailField.text.toString()
            val password = binding.loginPassField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val mainIntent = Intent(this, PermissionActivity::class.java)
                            startActivity(mainIntent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerRedirectText.setOnClickListener {
            val signupintent = Intent(this, SignUpActivity::class.java)
            startActivity(signupintent)
        }

        binding.fab.setOnClickListener {
            val fabIntent = Intent(this, IntroActivity::class.java)
            startActivity(fabIntent)
        }

        binding.forgotPass.setOnClickListener {
            val forgotIntent = Intent(this, ForgotActivity::class.java)
            startActivity(forgotIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release any resources associated with FirebaseAuth
        firebaseAuth.signOut()
    }
}
