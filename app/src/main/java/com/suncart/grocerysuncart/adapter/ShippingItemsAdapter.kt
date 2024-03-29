package com.suncart.grocerysuncart.adapter

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.activity.ProductDetails
import com.suncart.grocerysuncart.database.tables.ProductItems
import com.suncart.grocerysuncart.util.DbUtils

open class ShippingItemsAdapter(
    var context: Context,
    var productItemModel: MutableList<ProductItems>
) : RecyclerView.Adapter<ShippingItemsAdapter.MyViewHolder>(){

    lateinit var cartNumListener : CartNumberListener
    lateinit var mTriggerPriceFluctuation: TriggerPriceFluctuation

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShippingItemsAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.shipping_lay_reycler,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productItemModel.size
    }

    override fun onBindViewHolder(holder: ShippingItemsAdapter.MyViewHolder, position: Int) {
        Glide.with(context).load(productItemModel[position].productPics).into(holder.productImg)
        holder.productMrp.text = "Rs." + productItemModel[position].productMrp
        holder.productSp.text = "Rs." +productItemModel[position].productSp
        addShowMoreDots(productItemModel[position].productName, holder.productTitle, 35)
        holder.productUnit.text = productItemModel[position].productWeight + " "+productItemModel[position].unitType
        holder.totalQty.text = productItemModel[position].totalQty.toString()

        holder.addBtn?.setOnClickListener(View.OnClickListener { v: View? ->
            if (DbUtils.getTtlQtyByIds(position, productItemModel) == 0L) {
                DbUtils.insertRowDb(position, 1, productItemModel)
                holder.totalQty.text = DbUtils.getTtlQtyByIds(position, productItemModel).toString()
                cartNumListener.setCurrentTotalQty(DbUtils.getTtlQty().toInt())
                mTriggerPriceFluctuation.setPriceFluctuationHappen(true)

            } else if (DbUtils.getTtlQtyByIds(position, productItemModel) > 0L) {
                DbUtils.insertRowDb(position, 1, productItemModel)
                holder.totalQty.text = DbUtils.getTtlQtyByIds(position, productItemModel).toString()
                cartNumListener.setCurrentTotalQty(DbUtils.getTtlQty().toInt())
                mTriggerPriceFluctuation.setPriceFluctuationHappen(true)
            }

        })

        holder.minusBtn?.setOnClickListener(View.OnClickListener {
            if (DbUtils.getTtlQtyByIds(position, productItemModel) == 0L) {
                mTriggerPriceFluctuation.setPriceFluctuationHappen(true)
            } else if (DbUtils.getTtlQtyByIds(position, productItemModel) > 0L) {
                DbUtils.insertRowDb(position, -1, productItemModel)
                var ttQtsByIds = DbUtils.getTtlQtyByIds(position, productItemModel)
                holder.totalQty.text = ttQtsByIds.toString()
                if (ttQtsByIds == 0L) {
                    productItemModel.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount - position);
                }
                mTriggerPriceFluctuation.setPriceFluctuationHappen(true)
                cartNumListener.setCurrentTotalQty(DbUtils.getTtlQty().toInt())
            }
        })

        holder.productImg.setOnClickListener {
            var intent = Intent(context, ProductDetails::class.java)
            intent.putExtra("product_ids", productItemModel[position].ids.toString())
            context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productImg = itemView.findViewById<ImageView>(R.id.product_img)
        var productSp = itemView.findViewById<TextView>(R.id.sp_price)
        var productMrp = itemView.findViewById<TextView>(R.id.mrp_price)
        var productTitle  = itemView.findViewById<TextView>(R.id.product_title)
        var productUnit = itemView.findViewById<TextView>(R.id.product_weight)
        var totalQty = itemView.findViewById<TextView>(R.id.total_quantity)
        var minusBtn = itemView.findViewById<ImageView?>(R.id.minus_btn)
        var addBtn = itemView.findViewById<ImageView?>(R.id.plus_btn)
    }

    interface CartNumberListener{
        fun setCurrentTotalQty(ttlQty: Int)
    }

    interface TriggerPriceFluctuation{
        fun setPriceFluctuationHappen(isFlucationHappen : Boolean)
    }

    fun setTriggerPriceFluctuation(triggerPriceFluctuation: TriggerPriceFluctuation){
        this.mTriggerPriceFluctuation = triggerPriceFluctuation
    }

    fun setCartNumberListener(cartNumberListener: CartNumberListener) {
        this.cartNumListener = cartNumberListener
    }

    open fun addShowMoreDots(
        targetString: String,
        tvStringHolder: TextView,
        charactersLimit: Int
    ) {
        var targetString = targetString
        if (targetString.length > charactersLimit) {
            val dotsString = " ... "
            targetString = targetString.substring(0, charactersLimit) + dotsString
            val spannableDots = SpannableString(targetString)
            tvStringHolder.movementMethod = LinkMovementMethod.getInstance()
            tvStringHolder.setText(spannableDots, TextView.BufferType.SPANNABLE)
        } else {
            tvStringHolder.text = targetString
        }
    }
    
    fun setItems(productItemModel: MutableList<ProductItems>){
        this.productItemModel = productItemModel
        notifyDataSetChanged()
        mTriggerPriceFluctuation.setPriceFluctuationHappen(true)
    }
}