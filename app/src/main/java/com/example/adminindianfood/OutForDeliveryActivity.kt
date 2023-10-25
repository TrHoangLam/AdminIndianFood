package com.example.adminindianfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminindianfood.adapter.DeliveryAdapter
import com.example.adminindianfood.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val customerName = arrayListOf(
            "Lam Tran",
            "Anh Khoa",
            "John Smith",
        )
        val moneyStatus = arrayListOf(
            "received",
            "notReceived",
            "Pending",
        )
        val adapter = DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}