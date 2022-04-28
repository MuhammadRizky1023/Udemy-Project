package com.example.bookstoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookstoreapp.R
import com.example.bookstoreapp.firestore.FirestoreClass
import com.example.bookstoreapp.models.SoldProduct
import com.example.bookstoreapp.ui.adapter.SoldProductsListAdapter
import kotlinx.android.synthetic.main.fragment_sold_products.*

class SoldProductsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }

    private fun getSoldProductsList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getSoldProducts(this@SoldProductsFragment)

    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }

    fun successSoLdProducts(soldProductsList: ArrayList<SoldProduct>){
        hideProgressDialog()

        if (soldProductsList.size > 0){
            rv_sold_products_items.visibility = View.VISIBLE
            tv_no_sold_products_found.visibility= View.GONE

            rv_sold_products_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_products_items.setHasFixedSize(true)

            val soldProductsListAdapter = SoldProductsListAdapter(requireActivity(), soldProductsList)
            rv_sold_products_items.adapter = soldProductsListAdapter

        }else {
            rv_sold_products_items.visibility = View.GONE
            tv_no_sold_products_found.visibility = View.VISIBLE
        }
    }

}