package com.suncart.grocerysuncart.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suncart.grocerysuncart.R
import com.suncart.grocerysuncart.adapter.ProductRecyclerAdapter
import com.suncart.grocerysuncart.bus.ContentLoadedEvent
import com.suncart.grocerysuncart.model.content.ContentItems
import com.suncart.grocerysuncart.service.ContentService
import com.suncart.grocerysuncart.util.DbUtils
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.product_categories_layout.*

class ProductCategories : AppCompatActivity(){
    var eventBus = EventBus.getDefault()
    lateinit var contentService : ContentService
    lateinit var totalCart : TextView

    private lateinit var categoriesRecyclerAdapter : ProductRecyclerAdapter
    private lateinit var categoriesRecyclerView : RecyclerView
    private lateinit var contentItems : MutableList<ContentItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_categories_layout)
        contentItems = mutableListOf()
        val catNames = intent?.getStringExtra("cat_names")
        contentService = ContentService(this)
        contentService.getAllContentItemsByCategories(catNames);

        //toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val nav_icon  = supportActionBar?.customView?.findViewById<ImageView>(R.id.navigation_drawer)
        val cartImg = supportActionBar?.customView?.findViewById<ImageView>(R.id.cart_icons)
        totalCart = supportActionBar?.customView?.findViewById<TextView>(R.id.total_cart)!!
        val titleBar = supportActionBar?.customView?.findViewById<TextView>(R.id.title_appbar)
        titleBar?.text = catNames
        titleBar?.setTextColor(Color.BLACK)

        nav_icon?.visibility = View.GONE
        cartImg?.visibility = View.VISIBLE
        totalCart.visibility = View.VISIBLE

        cartImg?.setOnClickListener {
            val intent = Intent(this, MyCart::class.java)
            startActivity(intent)
        }

        toolbar.setBackgroundColor(Color.parseColor("#FF7B5FAE"))
        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        categoriesRecyclerView = findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        categoriesRecyclerView.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()
        // totalCart.text = DbUtils.getDataForTrack().toString();
    }

    override fun onStart() {
        super.onStart()
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this)
        }
    }

    fun onEvent(contentLoadedEvent: ContentLoadedEvent){
        if (contentLoadedEvent != null){
            lottie_anim.visibility = View.GONE
            categoriesRecyclerView.visibility = View.VISIBLE

            contentItems.addAll(contentLoadedEvent.contentItemsList)

            categoriesRecyclerAdapter =
                ProductRecyclerAdapter(
                    this,
                    contentItems
                );
            categoriesRecyclerView.adapter = categoriesRecyclerAdapter
            categoriesRecyclerView.layoutManager = LinearLayoutManager(this)

            // set cart track of product
            categoriesRecyclerAdapter.setCartTrackListener {
                    currentQty: String? ->  totalCart.text = currentQty
            }
        }
    }
}