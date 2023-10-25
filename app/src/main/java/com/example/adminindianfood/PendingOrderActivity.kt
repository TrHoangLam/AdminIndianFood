package com.example.adminindianfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminindianfood.adapter.DeliveryAdapter
import com.example.adminindianfood.adapter.PendingOrderAdapter
import com.example.adminindianfood.databinding.ActivityPendingOrderBinding
import com.example.adminindianfood.databinding.PendingOrderItemBinding

class PendingOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPendingOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val orderCustomerName = arrayListOf(
            "Lam Tran",
            "Anh Khoa",
            "John Smith",
        )
        val orderedQuantity = arrayListOf(
            "8",
            "7",
            "5",
        )
        val orderedFoodImage = arrayListOf(R.drawable.food_1,R.drawable.food_2,R.drawable.food_3)
        val adapter = PendingOrderAdapter(orderCustomerName,orderedQuantity,orderedFoodImage,this)
        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}