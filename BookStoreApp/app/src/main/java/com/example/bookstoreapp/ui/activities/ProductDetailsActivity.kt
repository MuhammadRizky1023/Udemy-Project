package com.example.bookstoreapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bookstoreapp.R
import com.example.bookstoreapp.firestore.FirestoreClass
import com.example.bookstoreapp.models.Cart
import com.example.bookstoreapp.models.Product
import com.example.bookstoreapp.utils.Constants
import com.example.bookstoreapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.item_cart_layout.view.*

/**
 * Product Details Screen.
 */
class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mProductDetails: Product


    private var mProductId: String = ""


    private var mProductOwnerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_product_details)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mProductOwnerId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        setupActionBar()

        if (FirestoreClass().getCurrentUserID() == mProductOwnerId) {
            btn_add_to_cart.visibility = View.VISIBLE
            btn_go_to_cart.visibility = View.GONE
        } else {
            btn_add_to_cart.visibility = View.GONE
        }

        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)

        getProductDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart -> {
                    addToCart()
                }

                R.id.btn_go_to_cart->{
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }
        }
    }

    /**
     * A function to prepare the cart item to add it to the cart in cloud firestore.
     */
    private fun addToCart() {

        val addToCart = Cart(
            FirestoreClass().getCurrentUserID(),
            // TODO Step 4: Pass the required param here.
            // START
            mProductOwnerId,
            // END
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addCartItems(this@ProductDetailsActivity, addToCart)
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }


    fun productDetailsSuccess(product: Product) {

        mProductDetails = product

        // Populate the product details in the UI.
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )

        tv_product_details_title.text = product.title
        tv_product_details_price.text = "Rp${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_stock_quantity.text = product.stock_quantity


        if(product.stock_quantity.toInt() == 0){
            hideProgressDialog()
            btn_add_to_cart.visibility = View.GONE

            tv_product_details_stock_quantity.text =
                resources.getString(R.string.lbl_out_of_stock)

            tv_product_details_stock_quantity.setTextColor(
                ContextCompat.getColor(
                    this@ProductDetailsActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{

            // There is no need to check the cart list if the product owner himself is seeing the product details.
            if (FirestoreClass().getCurrentUserID() == product.user_id) {
                // Hide Progress dialog.
                hideProgressDialog()
            } else {
                FirestoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
            }
        }
    }

    fun productExistsInCart() {

        // Hide the progress dialog.
        hideProgressDialog()

        // Hide the AddToCart button if the item is already in the cart.
        btn_add_to_cart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        btn_go_to_cart.visibility = View.VISIBLE
    }


    fun addToCartSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
    }
}