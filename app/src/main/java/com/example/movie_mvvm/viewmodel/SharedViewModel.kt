package com.example.movie_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    var bundleFromFragmentDetailToFragmentSearch = MutableLiveData<String>()
}