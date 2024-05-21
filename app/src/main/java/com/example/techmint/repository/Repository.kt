package com.example.techmint.repository

import com.example.techmint.model.ContributorRepositoryListItem
import com.example.techmint.model.ContributorsListResponse
import com.example.techmint.model.SearchResponse
import com.example.techmint.network.ServiceApi
import kotlinx.coroutines.Deferred

class Repository (private val apiService: ServiceApi ){

    suspend fun searchRepo(v: String): Deferred<SearchResponse>{
        return  apiService.searchReposAsynch(v)
    }

    suspend fun getContributorsAsynch(v: String): Deferred<ArrayList<ContributorsListResponse>>{
        return  apiService.getContributorsListAsynch(v)
    }
    suspend fun getContributorRepositoryListAsynch(v: String): Deferred<ArrayList<ContributorRepositoryListItem>>{
        return  apiService.getContributorRepoListAsynch(v)
    }
}