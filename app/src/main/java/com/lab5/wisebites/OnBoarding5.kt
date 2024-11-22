package com.lab5.wisebites

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.lab5.wisebites.databinding.ActivityOnBoarding5Binding

class OnBoarding5 : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoarding5Binding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOnBoarding5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Status...")
        progressDialog.setMessage("Please wait")

        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this, OnBoarding4::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {
            when {
                binding.tietUsername.text.toString().isEmpty() -> binding.tietUsername.error =
                    "Username cannot be empty"

                binding.tietEmail.text.toString().isEmpty() -> binding.tietEmail.error =
                    "Email cannot be empty"

                binding.tietCreatePassword.text.toString().isEmpty() ->
                    binding.tietCreatePassword.error = "Password cannot be empty"

                binding.tietConfirmPassword.text.toString().isEmpty() ->
                    binding.tietConfirmPassword.error = "Confirm Password cannot be empty"

                else -> {
                    val username = binding.tietUsername.text.toString()
                    val email = binding.tietEmail.text.toString()
                    val password = binding.tietCreatePassword.text.toString()
                    val confirmPassword = binding.tietConfirmPassword.text.toString()

                    if (password != confirmPassword) {
                        binding.tietConfirmPassword.error = "Password does not match"
                        return@setOnClickListener
                    }

                    progressDialog.show()

                    signUp(email, username, password)
                }
            }
        }
    }

    private fun signUp(email: String, username: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val userProfile = userProfileChangeRequest {
                        displayName = username
                    }
                    val firebaseUser: FirebaseUser = it.result!!.user!!

                    firebaseUser.updateProfile(userProfile)
                        .addOnCompleteListener {
                            progressDialog.dismiss()

                            val intent = Intent(
                                this,
                                HomeActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        }
                } else {
                    Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}