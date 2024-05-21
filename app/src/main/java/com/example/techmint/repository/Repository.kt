package com.example.techmint.repository

import com.example.techmint.model.SearchResponse
import com.example.techmint.network.ServiceApi
import kotlinx.coroutines.Deferred

class Repository (private val apiService: ServiceApi ){

    suspend fun searchRepo(v: String): Deferred<SearchResponse>{
        return  apiService.searchReposAsynch(v)
    }
}