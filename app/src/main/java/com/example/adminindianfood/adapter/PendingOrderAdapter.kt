package com.example.adminindianfood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adminindianfood.databinding.PendingOrderItemBinding

class PendingOrderAdapter(private val customerNames:ArrayList<String>,
                          private val quantity:ArrayList<String>,
                          private val foodImage:ArrayList<Int>,
    private val context: Context)
    :RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
       val binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOrderViewHolder(binding)
    }



    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
       holder.bind(position)
    }
    override fun getItemCount(): Int = customerNames.size
    inner class PendingOrderViewHolder(private val binding:PendingOrderItemBinding):RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                pedingOrderQuantity.text = quantity[position]
                orderFoodImage.setImageResource(foodImage[position])

                orderedAcceptButton.apply {
                    if (!isAccepted){
                        text = "Accept"
                    }else{
                        text = "Dispath"
                    }
                    setOnClickListener {
                        if (!isAccepted){
                            text = "Dispath"
                            isAccepted = true
                            showToast("Order Is Accepted")
                        }else{
                                customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order Is Dispatched")
                        }
                    }
                }
            }

        }
       private fun showToast(meesage:String){
            Toast.makeText(context,meesage,Toast.LENGTH_SHORT).show()
        }
    }
}