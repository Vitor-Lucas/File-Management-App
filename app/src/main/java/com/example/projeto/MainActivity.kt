package com.example.projeto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var email_edit_text: EditText
    private lateinit var password_edit_text: EditText
    private lateinit var register_button: Button
    private lateinit var login_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)


        register_button = findViewById(R.id.registerButton)
        login_button = findViewById(R.id.loginButton)
        email_edit_text = findViewById(R.id.emailEditText)
        password_edit_text = findViewById(R.id.passwordEditText)

        login_button.setOnClickListener {
            val email = email_edit_text.text.toString().trim()
            val password = password_edit_text.text.toString().trim()
            val intent = Intent(this, StoragePage::class.java)
            login_user(email, password)
            startActivity(intent)
        }

        register_button.setOnClickListener {
            val email = email_edit_text.text.toString().trim()
            val password = password_edit_text.text.toString().trim()
            register_user(email, password)
        }

    }

    fun register_user(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Registrado com sucesso: ${user?.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao registrar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun login_user(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Logado com sucesso: ${user?.email}",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao logar: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}