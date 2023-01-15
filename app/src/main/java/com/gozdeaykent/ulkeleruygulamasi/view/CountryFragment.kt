package com.gozdeaykent.ulkeleruygulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gozdeaykent.ulkeleruygulamasi.R
import com.gozdeaykent.ulkeleruygulamasi.databinding.FragmentCountryBinding
import com.gozdeaykent.ulkeleruygulamasi.model.Country
import com.gozdeaykent.ulkeleruygulamasi.util.downloadFromUrl
import com.gozdeaykent.ulkeleruygulamasi.util.placeHolderProgressBar
import com.gozdeaykent.ulkeleruygulamasi.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country.*

class CountryFragment : Fragment() {

    private lateinit var viewModel : CountryViewModel
    private var countryUuid =0
    private lateinit var dataBİnding : FragmentCountryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBİnding = DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)
        return dataBİnding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryUuid =CountryFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryUuid)


        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let {
                dataBİnding.selectedCountry = country

            }
        })
    }

}