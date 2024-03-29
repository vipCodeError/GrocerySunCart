package com.suncart.grocerysuncart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.database.tables.ProductItems

public class ProductShippingPayment(var context: Context, var bestDealModel: MutableList<ProductItems>) : RecyclerView.Adapter<ProductShippingPayment.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductShippingPayment.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shipping_pay_lay_reycler, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bestDealModel.size
    }

    override fun onBindViewHolder(holder: ProductShippingPayment.MyViewHolder, position: Int) {
        Glide.with(context).load(bestDealModel[position].productPics).into(holder.productImg)
        holder.productMrp.text = bestDealModel[position].productMrp
        holder.productSp.text = bestDealModel[position].productSp
        holder.productTitle.text = bestDealModel[position].productName
        holder.productUnit.text = bestDealModel[position].productWeight + " " +bestDealModel[position].unitType
        holder.totalQty.text = bestDealModel[position].totalQty.toString()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImg = itemView.findViewById<ImageView>(R.id.product_img)
        var productSp = itemView.findViewById<TextView>(R.id.sp_price)
        var productMrp = itemView.findViewById<TextView>(R.id.mrp_price)
        var productTitle  = itemView.findViewById<TextView>(R.id.product_title)
        var productUnit = itemView.findViewById<TextView>(R.id.product_weight)
        var totalQty = itemView.findViewById<TextView>(R.id.total_quantity)
    }

}