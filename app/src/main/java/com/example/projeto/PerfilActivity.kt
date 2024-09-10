package com.example.projeto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlin.text.isNotEmpty

class PerfilActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_terceira_tela)
        auth = FirebaseAuth.getInstance()

        val emailTextView = findViewById<TextView>(R.id.profile_email)
        val newEmailEditText = findViewById<EditText>(R.id.new_email)
        val newPasswordEditText = findViewById<EditText>(R.id.new_password)
        val updateButton = findViewById<Button>(R.id.update_button)
        val logoutButton = findViewById<Button>(R.id.logout_button)

        val user = auth.currentUser
        emailTextView.text = "Email: ${user?.email}"

        updateButton.setOnClickListener {
            val newEmail = newEmailEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()

            if (newEmail.isNotEmpty()) {
                updateEmail(newEmail)
            }
            if (newPassword.isNotEmpty()) {
                updatePassword(newPassword)
            }
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateEmail(newEmail: String) {
        val user = auth.currentUser
        if (user != null) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                user.verifyBeforeUpdateEmail(newEmail)
                    .addOnCompleteListener { task ->
                        when {
                            task.isSuccessful -> {
                                Toast.makeText(this, "Verification email sent to $newEmail", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                val exception = task.exception
                                val message = when (exception) {
                                    is FirebaseAuthInvalidCredentialsException -> "Invalid email format"
                                    is FirebaseAuthUserCollisionException -> "Email already in use"
                                    is FirebaseNetworkException -> "Network error"
                                    else -> "Failed to send verification email"
                                }
                                Log.w("UserProfile", "Failed to update email: $message", exception)
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePassword(newPassword: String) {
        val user = auth.currentUser
        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("UserProfile", "Failed to update password", task.exception)
                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                }
            }
    }
}