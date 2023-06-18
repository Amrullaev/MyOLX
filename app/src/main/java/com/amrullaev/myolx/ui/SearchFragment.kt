package com.amrullaev.myolx.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.amrullaev.myolx.MainActivity
import com.amrullaev.myolx.R
import com.amrullaev.myolx.databinding.FragmentSearchBinding
import com.amrullaev.myolx.sharedPreferences.YourPreference

class SearchFragment : Fragment() {
    lateinit var yourPreference: YourPreference
    lateinit var list: ArrayList<String>
    lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val bundle = Bundle()
                bundle.putString("search", query.toString())
                findNavController().popBackStack()
                findNavController().navigate(R.id.searchViewFragment, bundle)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNawView()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNawView()
    }
}