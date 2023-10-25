package com.example.adminindianfood

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminindianfood.databinding.ActivityAddItemBinding
import com.example.adminindianfood.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AddItemActivity : AppCompatActivity() {

    //food item details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private  var foodImageUri: Uri? = null
    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database:FirebaseDatabase

    private val binding : ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //init FireBase
        auth = FirebaseAuth.getInstance()
        //init FireBase database
        database = FirebaseDatabase.getInstance()

        binding.AddItemButton.setOnClickListener {
            //get data from file
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredint.text.toString().trim()

            if (!(foodName.isBlank()||foodPrice.isBlank()||foodDescription.isBlank()||foodIngredient.isBlank())){
                uploadData()
                Toast.makeText(this,"Item Add Successfully",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                    Toast.makeText(this,"Fill All The Details",Toast.LENGTH_SHORT).show()
            }
        }
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }


            binding.backButton.setOnClickListener {
                finish()
            }
    }
    private fun uploadData() {
        //get a reference to the menu node in the database
        val menuRef = database.getReference("menu")
        // genrate a unique key for the new menu item
        val newItemKey = menuRef.push().key
        if (foodImageUri != null){
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnCompleteListener {
                        downloadUrl ->
                    //create new menu item
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredient,
                        foodImage = downloadUrl.toString(),

                    )
                    newItemKey?.let {
                        key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this,"Data uploaded succesful",Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this,"Data uploaded failed",Toast.LENGTH_SHORT).show()
                            }
                    }
                }

            }  .addOnFailureListener {
                Toast.makeText(this,"Image uploaded failed",Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this,"Please select an image",Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if (uri != null)
        {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}