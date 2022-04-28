package com.example.bookstoreapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.bookstoreapp.R
import com.example.bookstoreapp.models.SoldProduct
import com.example.bookstoreapp.utils.Constants
import com.example.bookstoreapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_my_order_details.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_sold_product_details.*
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_product_details)

        var productDetails: SoldProduct = SoldProduct()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAIL)){
            productDetails = intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_MY_ORDER_DETAIL)!!
        }

        setupActionBar()
        setUpUI(productDetails)
    }

    private  fun setupActionBar(){
        setSupportActionBar(toolbar_sold_product_details_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_sold_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private  fun setUpUI(productDetails: SoldProduct){
        tv_sold_product_details_id.text= productDetails.order_id

        val dateFormat = "dd MMM yyy H:M"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date
        tv_sold_product_details_date.text = formatter.format(calendar.time)

        GlideLoader(this@SoldProductDetailsActivity).loadProductPicture(
            productDetails.image,
            iv_product_item_image
        )
        tv_product_item_name.text = productDetails.title
        tv_product_item_price.text= "$${productDetails.price}"
        tv_sold_product_quantity.text = productDetails.sold_quantity

        tv_sold_details_address_type.text = productDetails.address.type
        tv_sold_details_full_name.text = productDetails.name
        tv_sold_details_address.text = "${productDetails.address.address} ${productDetails.address.zipCode}"

        tv_sold_details_additional_note.text = productDetails.address.additionalNote

        if(productDetails.address.otherDetails.isNotEmpty()){
            tv_sold_details_other_details.visibility = View.VISIBLE
            tv_sold_details_other_details.text = productDetails.address.otherDetails
        } else {
            tv_sold_details_other_details.visibility = View.GONE
        }
        tv_sold_details_mobile_number.text = productDetails.address.mobileNumber

        tv_sold_product_sub_total.text = productDetails.sub_total_amount
        tv_sold_product_shipping_charge.text = productDetails.shipping_charge
        tv_sold_product_total_amount.text = productDetails.total_amount
    }
}