package com.example.techmint

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.techmint.databinding.ActivityMainBinding
import com.example.techmint.model.Items
import com.example.techmint.model.SearchResponse
import com.example.techmint.network.RetrofitInstance
import com.example.techmint.repository.Repository
import com.google.gson.JsonSyntaxException
import com.rebeltt.app.grn.adapter.RepoListAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() , RepoListAdapter.Listener {
    lateinit var binding:ActivityMainBinding
    lateinit var repoListAdapter: RepoListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        repoListAdapter = RepoListAdapter(this)
        binding.repoListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.repoListRecyclerView.adapter = repoListAdapter
        val retrofit = RetrofitInstance()
        val serviceApi = retrofit.getService()
        val repository = Repository(serviceApi)
        var response: SearchResponse
        GlobalScope.launch {
            try {
                response = repository.searchRepo("TheMovieApp").await()
                 Log.d("Neeraj", "  item ${response.items.size}")
                 Log.d("Neeraj" , response.totalCount.toString())
                runOnUiThread{
                    repoListAdapter.swapData(response.items)
                    Log.d("Neeraj" , response.totalCount.toString())
                }
            }
            catch ( exception : IllegalStateException)
            {
                Log.d("Neeraj", "selected item $exception")
            }
            catch(exc: JsonSyntaxException){
                Log.d("Neeraj", "selected item $exc")

            }

        }
        binding.searchButton.setOnClickListener{
            GlobalScope.launch {
                try {

                    response = repository.searchRepo(binding.search.text.toString()).await()
                    Log.d("Neeraj", binding.search.text.toString())
                    Log.d("Neeraj", "  item ${response.items.size}")
                    Log.d("Neeraj" , response.totalCount.toString())
                    runOnUiThread{
                        repoListAdapter.swapData(response.items)
                        Log.d("Neeraj" , response.totalCount.toString())
                    }
                }
                catch ( exception : IllegalStateException)
                {
                    Log.d("Neeraj", "selected item $exception")
                }
                catch(exc: JsonSyntaxException){
                    Log.d("Neeraj", "selected item $exc")
                }
                catch(exeception:Throwable){
                    if(exeception is HttpException){
                        runOnUiThread{
                            Toast.makeText(this@MainActivity , "Nothing found!!" , Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }
        }

    }

    override fun onInvoiceSelected(item: Items) {
        Log.d("Tag" , "selected item ${item.id}")
        Toast.makeText(this , item.id.toString() , Toast.LENGTH_SHORT).show()
    }
}