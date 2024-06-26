package com.example.techmint.network

import com.example.techmint.model.ContributorRepositoryListItem
import com.example.techmint.model.ContributorsListResponse
import com.example.techmint.model.SearchResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ServiceApi {

    @GET("/search/repositories")
     fun searchReposAsynch(@Query("q") search: String ) : Deferred<SearchResponse>

     //repos/notJust-dev/TheMovieApp
     @GET
     fun getContributorsListAsynch(@Url url:String ) : Deferred<ArrayList<ContributorsListResponse>>

    @GET
    fun getContributorRepoListAsynch(@Url url:String ) : Deferred<ArrayList<ContributorRepositoryListItem>>



}