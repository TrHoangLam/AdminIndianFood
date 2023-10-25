package com.example.adminindianfood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminindianfood.databinding.ActivityLoginBinding
import com.example.adminindianfood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private  var userName:String ?= null
    private  var nameOfRestaurant:String?= null
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference

    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // init Firebase Auth
        auth = Firebase.auth
        // init Firebase database
        database = Firebase.database.reference
        // init google signin
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)
        binding.loginButton.setOnClickListener {
            //get text form edit text
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isBlank()||password.isBlank()){
                Toast.makeText(this,"Please Fill All Details",Toast.LENGTH_SHORT).show()
            }else{
                    creatUserAccount(email,password)
            }
        }
        binding.googleButton.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.dontHaveAccountButton.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun creatUserAccount(email: String, password: String) {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show()
                    updateUI(user)
                }else{
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val user = auth.currentUser
                            Toast.makeText(this,"Create User & Login Successfully",Toast.LENGTH_SHORT).show()
                            saveUserData()
                            updateUI(user)
                        }else{
                            Toast.makeText(this,"Authentication failed",Toast.LENGTH_SHORT).show()
                            Log.d("Account","createdUserAccount: Authentication failed",task.exception)
                        }
                    }
                }
            }
    }
    private fun saveUserData() {
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = UserModel(userName,nameOfRestaurant,email,password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful){
                        Toast.makeText(
                            this,
                            "Successfully Sign-in with Google",
                            Toast.LENGTH_SHORT
                        ).show()

                        //check if user is already logged in

                            updateUI(authTask.result?.user)
                        finish()

                    }else{
                        Toast.makeText(this,"Google Sign-in failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
            Toast.makeText(this,"Google Sign-in failed",Toast.LENGTH_SHORT).show()
        }
        }
    }


//    //check if user is already logged in luu dang nhap
//    override fun onStart(){
//        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser != null){
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
//        }
//    }


    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}