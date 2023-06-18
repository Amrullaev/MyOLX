package com.amrullaev.myolx.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amrullaev.myolx.R
import com.amrullaev.myolx.database.entity.Product
import com.amrullaev.myolx.databinding.FragmentProfileBinding
import com.amrullaev.myolx.sharedPreferences.YourPreference
import com.amrullaev.myolx.utils.LoadProduct
import com.amrullaev.myolx.utils.Status
import kotlin.system.exitProcess

class ProfileFragment : Fragment() {
    lateinit var yourPreference: YourPreference

    lateinit var binding: FragmentProfileBinding

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        yourPreference = YourPreference.getInstance(requireContext())
        LoadProduct(requireContext()).loadProduct("myProduct")
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data != null) {
                            val list = ArrayList<Product>()
                            it.data.forEach { product ->
                                if (product.number == yourPreference.getData("phone")) {
                                    list.add(product)
                                }
                            }
                            binding.myProductCount.text = list.size.toString()
                        }
                    }
                    else -> {}
                }
            })
        binding.addProduct.setOnClickListener {
            findNavController().navigate(R.id.addProductFragment)
        }
        binding.activeComment.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.messageFragment)
        }
        binding.myProduct.setOnClickListener {
            findNavController().navigate(R.id.myProductFragment)
        }
        binding.rating.setOnClickListener {
            Toast.makeText(requireContext(), "Sizda xali baho olinmagan!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.balance.setOnClickListener {
            Toast.makeText(requireContext(), "Xamyoningizda 0.0 so'm pul bor!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }
        binding.help.setOnClickListener {
            val url = Uri.parse("https://help.olx.uz/hc/uz")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        binding.termsConditions.setOnClickListener {
            val url = Uri.parse("https://help.olx.uz/hc/ru/articles/360009197178")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        binding.userCovenant.setOnClickListener {
            val url =
                Uri.parse("https://help.olx.uz/hc/uz/sections/360002533258-Maxfiylik-siyosati")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        binding.about.setOnClickListener {
            val url =
                Uri.parse("https://help.olx.uz/hc/ru/sections/360002647937-%D0%9E-%D0%BA%D0%BE%D0%BC%D0%BF%D0%B0%D0%BD%D0%B8%D0%B8-Kompaniya-haqida")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        binding.exit.setOnClickListener {
            yourPreference.clear()
            activity?.finish()
            exitProcess(0)
        }

        return binding.root
    }
}