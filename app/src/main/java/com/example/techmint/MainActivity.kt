package com.example.techmint

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.techmint.databinding.ActivityMainBinding
import com.example.techmint.databinding.ContributorRepositoryListBinding
import com.example.techmint.databinding.RepoDetailFragmentBinding
import com.example.techmint.model.ContributorRepositoryListItem
import com.example.techmint.model.ContributorsListResponse
import com.example.techmint.model.Items
import com.example.techmint.model.SearchResponse
import com.example.techmint.network.RetrofitInstance
import com.example.techmint.repository.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonSyntaxException
import com.rebeltt.app.grn.adapter.ContributorListAdapter
import com.rebeltt.app.grn.adapter.ContributorRepositoryListAdapter
import com.rebeltt.app.grn.adapter.RepoListAdapter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException


class MainActivity : AppCompatActivity() , RepoListAdapter.Listener , ContributorListAdapter.Listener {
    lateinit var binding:ActivityMainBinding
    lateinit var repoListAdapter: RepoListAdapter
    lateinit var repository:Repository
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
        repository = Repository(serviceApi)
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
            val handler = CoroutineExceptionHandler { _, exception ->
                runOnUiThread{
                    Toast.makeText(this@MainActivity , "Api limit exceeded 403 Nothing found!!" , Toast.LENGTH_SHORT).show()
                }
            }
            try {
                lifecycleScope.launch(Dispatchers.IO + handler) {

                    response = repository.searchRepo(binding.search.text.toString()).await()
                    Log.d("Neeraj", binding.search.text.toString())
                    Log.d("Neeraj", "  item ${response.items.size}")
                    Log.d("Neeraj", response.totalCount.toString())
                    runOnUiThread {
                        repoListAdapter.swapData(response.items)
                        Log.d("Neeraj", response.totalCount.toString())
                    }
                }

            } catch (exception: IllegalStateException) {
                Log.d("Neeraj", "selected item $exception")
            } catch (exc: JsonSyntaxException) {
                Log.d("Neeraj", "selected item $exc")
            } catch (exeception: HttpException) {

                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Api limit exceeded 403 Nothing found!!", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

     private var contributorList  = MutableLiveData<ArrayList<ContributorsListResponse>>()

    override fun onRepoItemSelected(item: Items) {

            val repoDetail = RepoDetailFragmentBinding.inflate(layoutInflater)
            // val repoDetail = layoutInflater.inflate(R.layout.repo_detail_fragment , null)
            //repoDetail.findViewById<TextView>(R.id.name).text = item.name
            repoDetail.name.text = item.name
            repoDetail.projectLink.text = item.htmlUrl
            repoDetail.projectLink.isClickable = true
            repoDetail.projectLink.setOnClickListener{
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(repoDetail.projectLink.text as String?))
                startActivity(i)
            }
            Glide.with(this)
                .load(item.owner?.avatarUrl) // image url
                .placeholder(R.drawable.dotted_line_square_background) // any placeholder to load at start
                .error(R.drawable.dotted_line_square_background)  // any image in case of error
                .override(100, 100) // resizing
                .centerCrop()
                .into(repoDetail.image);
            // Glide.with(this).load(image_url).apply(options).into(imageView);
            repoDetail.description.text = item.description
            repoDetail.contributors.text = "Wait Fetching contributor Data..." //item.contributorsUrl
            fetchContributorsList(item.contributorsUrl)

            contributorList.observe(this){
                val contributorAdapter = ContributorListAdapter(this)
                repoDetail.contributorsListRecyclerView.layoutManager = LinearLayoutManager(this)
                repoDetail.contributorsListRecyclerView.adapter = contributorAdapter
                contributorAdapter.swapData(it)
                repoDetail.contributors.text = "CONTRIBUTRS LIST"
            }

            val bottomsheet = BottomSheetDialog(this)
            bottomsheet.setContentView(repoDetail.root)
            bottomsheet.show()
            Log.d("Tag" , "selected item ${item.id}")
            //Toast.makeText(this , item.id.toString() , Toast.LENGTH_SHORT).show()


    }

    private fun fetchContributorsList(contributorsUrl: String?) {

        val handler = CoroutineExceptionHandler { _, exception ->
            runOnUiThread{
                Toast.makeText(this@MainActivity , "Api limit exceeded 403 Nothing found!!" , Toast.LENGTH_SHORT).show()
            }
        }
            lifecycleScope.launch(Dispatchers.IO + handler) {
                val list = repository.getContributorsAsynch(contributorsUrl!!).await()
                contributorList.postValue(list)
            }
    }


    private var contributorRepositoryList  = MutableLiveData<ArrayList<ContributorRepositoryListItem>>()

    override fun onContributorSelected(item: ContributorsListResponse) {
        Log.d("Tag" , "selected item ")
        val handler = CoroutineExceptionHandler { _, exception ->
            runOnUiThread{
                Toast.makeText(this@MainActivity , "Api limit exceeded 403 Nothing found!!" , Toast.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launch(Dispatchers.IO + handler) {
            val list = repository.getContributorRepositoryListAsynch(item.reposUrl!!).await()
            contributorRepositoryList.postValue(list)
        }
        contributorRepositoryList.observe(this){
            val bottomsheet = ContributorRepositoryListBinding.inflate(layoutInflater)
            bottomsheet.contributorName.text = item.login.toString() + "\'s REPOSITORY LIST"
            val adapter = ContributorRepositoryListAdapter()
            bottomsheet.reposList.layoutManager = LinearLayoutManager(this)
            bottomsheet.reposList.adapter = adapter
            adapter.swapData(it)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(bottomsheet.root)
            dialog.show()
            contributorRepositoryList.removeObservers(this)
        }
    }
}