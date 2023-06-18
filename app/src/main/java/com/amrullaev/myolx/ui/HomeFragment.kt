package com.amrullaev.myolx.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amrullaev.myolx.R
import com.amrullaev.myolx.adapter.CategoryAdapter
import com.amrullaev.myolx.adapter.ProductAdapter
import com.amrullaev.myolx.database.database.AppDatabase
import com.amrullaev.myolx.database.entity.Product
import com.amrullaev.myolx.databinding.FragmentHomeBinding
import com.amrullaev.myolx.databinding.ItemProductBinding
import com.amrullaev.myolx.models.Category
import com.amrullaev.myolx.utils.LoadProduct
import com.amrullaev.myolx.utils.Status
import com.google.firebase.firestore.FirebaseFirestore
import com.amrullaev.myolx.utils.LoadData
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)
    lateinit var productAdapter: ProductAdapter
    lateinit var list: ArrayList<Product>
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var categoryList: ArrayList<Category>
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var appDatabase: AppDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)



        binding.searchHome.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.searchFragment)
        }
        firebaseFirestore = FirebaseFirestore.getInstance()
        appDatabase = AppDatabase.getDatabase(requireContext())
        categoryList = LoadData.loadCategory() as ArrayList<Category>

        categoryAdapter = CategoryAdapter(categoryList, object : CategoryAdapter.OnClickListener {
            override fun onItemClickListener(category: Category) {
                val bundle = Bundle()
                bundle.putString("type", category.type)
                findNavController().navigate(R.id.typeFragment, bundle)
            }
        })
        binding.rvCategory.adapter = categoryAdapter

        list = ArrayList()
        LoadProduct(requireContext()).loadProductAll().observe(viewLifecycleOwner) {
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
                        Collections.sort(it.data
                        ) { o1, o2 ->
                            o2.id.toLong().compareTo(o1.id.toLong())
                        }
                        binding.progressBar.visibility = View.GONE
                        binding.errorImage.visibility = View.GONE
                        binding.rvList.visibility = View.VISIBLE
                        productAdapter =
                            ProductAdapter(requireContext(),
                                it.data,
                                object : ProductAdapter.OnclickListener {
                                    override fun onItemClickListener(product: Product) {
                                        val bundle = Bundle()
                                        bundle.putString("key", product.id)
                                        bundle.putString("type", product.type)
                                        findNavController().navigate(
                                            R.id.productViewFragment,
                                            bundle
                                        )
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
        }

        binding.refresh.setOnRefreshListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.homeFragment)
            Handler().postDelayed({
                binding.refresh.isRefreshing = false
            }, 1500)
        }

        return binding.root
    }


}