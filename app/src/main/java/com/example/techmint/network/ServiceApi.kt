package com.example.techmint.network

import com.example.techmint.model.RepositoryInfoDetails
import com.example.techmint.model.SearchResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("/search/repositories")
     fun searchReposAsynch(@Query("q") search: String ) : Deferred<SearchResponse>

     //repos/notJust-dev/TheMovieApp
     @GET("/repos/{user}/{repo}")
     fun getRepositoryDetailAsynch (@Path("user")  user:String , @Path("repo") repo:String) : Deferred<RepositoryInfoDetails>



}