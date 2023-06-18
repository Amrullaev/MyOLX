package com.amrullaev.myolx.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amrullaev.myolx.R
import com.amrullaev.myolx.adapter.ProductAdapter
import com.amrullaev.myolx.database.database.AppDatabase
import com.amrullaev.myolx.database.entity.Product
import com.amrullaev.myolx.databinding.FragmentSaveBinding
import com.amrullaev.myolx.databinding.ItemProductBinding

class SaveFragment : Fragment() {
    lateinit var appDatabase: AppDatabase
    lateinit var productAdapter: ProductAdapter

    private lateinit var binding: FragmentSaveBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSaveBinding.inflate(layoutInflater)
        appDatabase = AppDatabase.getDatabase(requireContext())

        appDatabase.productDao().getProduct().observe(viewLifecycleOwner, Observer {
            Log.d("AAA", "onCreateView: $it")
            if (it.isNotEmpty()) {
                binding.errorImage.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
                productAdapter =
                    ProductAdapter(requireContext(), it, object : ProductAdapter.OnclickListener {
                        override fun onItemClickListener(product: Product) {
                            val bundle = Bundle()
                            bundle.putString("key", product.id)
                            bundle.putString("type", product.type)
                            findNavController().navigate(
                                R.id.productViewFragment,
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
                binding.rv.adapter = productAdapter
            } else {
                binding.errorImage.visibility = View.VISIBLE
                binding.rv.visibility = View.GONE
            }
        })
        return binding.root
    }
}

