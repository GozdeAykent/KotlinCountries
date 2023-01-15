package com.gozdeaykent.ulkeleruygulamasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gozdeaykent.ulkeleruygulamasi.R
import com.gozdeaykent.ulkeleruygulamasi.databinding.ItemCountryBinding
import com.gozdeaykent.ulkeleruygulamasi.model.Country
import com.gozdeaykent.ulkeleruygulamasi.util.downloadFromUrl
import com.gozdeaykent.ulkeleruygulamasi.util.placeHolderProgressBar
import com.gozdeaykent.ulkeleruygulamasi.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.item_country.view.*

class CountryAdapter(val countryList: ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(), CountryClickListener {

    class CountryViewHolder(var view: ItemCountryBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.view.country = countryList[position]
        holder.view.listener = this
    }

    override fun getItemCount(): Int {
         return countryList.size
    }

    fun updateCountryList(newCountryLİst: List<Country>){
        countryList.clear()
        countryList.addAll(newCountryLİst)
        notifyDataSetChanged()
    }

    override fun onCountryClicked(v: View) {
        val uuid = v.countryUuidText.text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment()
        action.countryUuid =uuid
        Navigation.findNavController(v).navigate(action)
    }
}