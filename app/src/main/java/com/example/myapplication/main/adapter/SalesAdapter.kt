package com.example.myapplication.main.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemAdapterBinding
import com.example.myapplication.main.model.Sales

class SalesAdapter(
    private val context: Context,
    private val salesList: List<Sales>,
    val salesSelected: (Sales, Int) -> Unit

): RecyclerView.Adapter<SalesAdapter.MyViewHolder>(){

    val SELECT_BACK:Int = 1
    val SELECT_REMOVE:Int = 2
    val SELECT_EDIT:Int = 3
    val SELECT_DETAILS:Int = 4
    val SELECT_NEXT:Int = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sales = salesList[position]
        holder.binding.textTitle.text = sales.description
        holder.binding.btnDelete.setOnClickListener { salesSelected(sales, SELECT_REMOVE) }
        holder.binding.btnEdit.setOnClickListener { salesSelected(sales, SELECT_EDIT) }
        holder.binding.btnDetails.setOnClickListener { salesSelected(sales, SELECT_DETAILS) }
        when (sales.status) {
            0 -> {
                holder.binding.ibBack.isVisible = false
                holder.binding.ibNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.black)
                )
                holder.binding.ibNext.setOnClickListener { salesSelected(sales, SELECT_NEXT) }
            }
            1 -> {
                holder.binding.ibBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.black)
                )
                holder.binding.ibNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.black)
                )
                holder.binding.ibBack.setOnClickListener { salesSelected(sales, SELECT_BACK) }
                holder.binding.ibNext.setOnClickListener { salesSelected(sales, SELECT_NEXT) }
            }
            else -> {
                holder.binding.ibNext.isVisible = false
                holder.binding.ibBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.black)
                )
                holder.binding.ibNext.setOnClickListener { salesSelected(sales, SELECT_BACK) }
            }
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    inner class MyViewHolder(val binding: ItemAdapterBinding):
            RecyclerView.ViewHolder(binding.root)
}