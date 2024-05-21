package com.example.techmint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.techmint.model.ContributorRepositoryListItem
import com.example.techmint.model.ContributorsListResponse
import com.example.techmint.model.SearchResponse
import com.example.techmint.network.RetrofitInstance
import com.example.techmint.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import javax.inject.Inject

@HiltViewModel
class SearchResponseViewModel @Inject constructor( private val repository: Repository): ViewModel(){

    private var _response = MutableLiveData<SearchResponse>()
    var response: LiveData<SearchResponse> = _response
    private var _contributorList =  MutableLiveData<ArrayList<ContributorsListResponse>>()
    var contributorList: LiveData<ArrayList<ContributorsListResponse>> = _contributorList
    private var _contributorRepositoryList =  MutableLiveData<ArrayList<ContributorRepositoryListItem>>()
    var contributorRepositoryList: LiveData<ArrayList<ContributorRepositoryListItem>> = _contributorRepositoryList

    suspend fun searchRepository(v:String){
         val v = repository.searchRepo(v).await()
         _response.postValue(v)
    }
    suspend fun getContributorsAsynch(v:String){
        val v =  repository.getContributorsAsynch(v).await()
        _contributorList.postValue(v)
    }
    suspend fun getContributorRepositoryListAsynch(v:String) {
        val v =  repository.getContributorRepositoryListAsynch(v).await()
        _contributorRepositoryList.postValue(v)
    }

}