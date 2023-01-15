package com.gozdeaykent.ulkeleruygulamasi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gozdeaykent.ulkeleruygulamasi.model.Country
import com.gozdeaykent.ulkeleruygulamasi.service.CountryAPIService
import com.gozdeaykent.ulkeleruygulamasi.service.CountryDatabase
import com.gozdeaykent.ulkeleruygulamasi.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.concurrent.ScheduledExecutorService

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryAPIService = CountryAPIService()
    private val disposable =CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L


    val countries = MutableLiveData<List<Country>>()
    val countryEror = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData(){
        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
           getDataFromSQLite()
        }else{
            getDataFromAPI()
        }
    }

    fun refreshFromAPI(){
        getDataFromAPI()
    }
    private fun getDataFromSQLite(){
        countryLoading.value = true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Country From SQLite",Toast.LENGTH_LONG).show()

        }
    }
    private fun getDataFromAPI(){
        countryLoading.value=true

        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Country From API",Toast.LENGTH_LONG).show()

                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryEror.value = true
                        e.printStackTrace()
                    }

                })

        )
    }

    private fun showCountries(countryList: List<Country>){
        countries.value = countryList
        countryEror.value =false
        countryLoading.value = false
    }

    private fun storeInSQLite(list: List<Country>){
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.getAllCountries()
            val listLong =dao.insertAll(*list.toTypedArray())
            var i =0
            while(i < list.size){
                list[i].uuid = listLong[i].toInt()
                i = i+1
            }

            showCountries(list)
        }

        customPreferences.saveTime(System.nanoTime())

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}