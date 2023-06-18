package com.amrullaev.myolx.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amrullaev.myolx.MainActivity
import com.amrullaev.myolx.R
import com.amrullaev.myolx.adapter.ProductAdapter
import com.amrullaev.myolx.database.database.AppDatabase
import com.amrullaev.myolx.database.entity.Product
import com.amrullaev.myolx.databinding.FragmentTypeBinding
import com.amrullaev.myolx.databinding.ItemProductBinding
import com.amrullaev.myolx.utils.LoadProduct
import com.amrullaev.myolx.utils.Status
import java.util.*
import kotlin.Comparator

class TypeFragment : Fragment() {
    private var type = "all"
    lateinit var binding: FragmentTypeBinding
    lateinit var productAdapter: ProductAdapter
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString("type") as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTypeBinding.inflate(layoutInflater)
        appDatabase = AppDatabase.getDatabase(requireContext())
        loadProduct(type)
        binding.searchHome.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.searchFragment)
        }
        return binding.root
    }

    private fun loadProduct(type: String) {
        LoadProduct(requireContext()).loadProduct(type).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorImage.visibility = View.INVISIBLE
                    binding.rvList.visibility = View.INVISIBLE
                }
                Status.ERROR -> {
                    binding.rvList.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.errorImage.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.progressBar.visibility = View.GONE
                        binding.errorImage.visibility = View.GONE
                        binding.rvList.visibility = View.VISIBLE
                        val productList = it.data
                        Collections.sort(productList,
                            Comparator<Product> { o1, o2 ->
                                o2.id.toLong().compareTo(o1.id.toLong())
                            })
                        productAdapter =
                            ProductAdapter(requireContext(),
                                productList,
                                object : ProductAdapter.OnclickListener {
                                    override fun onItemClickListener(product: Product) {
                                        val bundle = Bundle()
                                        bundle.putString("key", product.id)
                                        bundle.putString("type", product.type)
                                        findNavController().navigate(R.id.productViewFragment,
                                            bundle)
                                    }

                                    override fun likeOnClick(
                                        itemBinding: ItemProductBinding,
                                        product: Product,
                                    ) {
                                        if (appDatabase.productDao().getProductForLike()
                                                .contains(product)
                                        ) {
                                            itemBinding.like.setImageResource(R.drawable.ic_like)
                                            appDatabase.productDao().deleteProduct(product)

                                        } else {
                                            appDatabase.productDao().insertProduct(product)
                                            itemBinding.like.setImageResource(R.drawable.ic_liked)
                                        }
                                    }
                                })
                        binding.rvList.adapter = productAdapter

                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.errorImage.visibility = View.VISIBLE
                        binding.rvList.visibility = View.INVISIBLE
                    }
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNawView()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideBottomNawView()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNawView()
    }
}