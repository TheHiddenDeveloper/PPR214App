package com.mainapp.droneapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.mainapp.droneapp.databinding.ActivityForgotdialogueBinding
import java.lang.ref.WeakReference

class ForgotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotdialogueBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotdialogueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.forgotSend.setOnClickListener {
            val userEmail = findViewById<EditText>(R.id.forgotEmail)
            compareEmail(WeakReference(userEmail))
        }
        binding.fab002.setOnClickListener {
            val fabIntent002 = Intent(this, LoginActivity::class.java)
            startActivity(fabIntent002)
        }
        binding.forgotCancel.setOnClickListener {
            val cancelIntent = Intent(this, IntroActivity::class.java)
            startActivity(cancelIntent)
        }
    }

    private fun compareEmail(email: WeakReference<EditText>) {
        val emailView = email.get()
        if (emailView?.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailView?.text.toString()).matches()) {
            return
        }
        firebaseAuth.sendPasswordResetEmail(emailView?.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your Email", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
