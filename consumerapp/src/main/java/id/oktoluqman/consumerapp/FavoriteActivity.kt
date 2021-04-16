package id.oktoluqman.consumerapp

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.oktoluqman.consumerapp.databinding.ActivityFavoriteBinding
import id.oktoluqman.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import id.oktoluqman.consumerapp.helper.MappingHelper
import id.oktoluqman.consumerapp.model.GithubUser
import id.oktoluqman.consumerapp.viewmodel.FavoriteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    companion object {
        private val TAG = FavoriteActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerList()
    }

    private fun showRecyclerList() {
        val adapter = ListGithubUserAdapter {
            navigateToGithubUserDetail(it)
        }

        binding.rvGithubUser.layoutManager = LinearLayoutManager(this)
        binding.rvGithubUser.adapter = adapter
        binding.rvGithubUser.addItemDecoration(
            DividerItemDecoration(
                binding.rvGithubUser.context,
                DividerItemDecoration.VERTICAL
            )
        )

        favoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteViewModel::class.java)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                loadUsers()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        // get result
        favoriteViewModel.getUsers().observe(this, { listGithubUser ->
            if (listGithubUser != null) {
                adapter.setData(listGithubUser)
            }
        })

        supportActionBar?.title = resources.getString(R.string.app_name)

        loadUsers()
    }

    private fun loadUsers() {
        GlobalScope.launch(Dispatchers.Main) {
            val defferedFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToGithubUserArrayList(cursor)
            }
            val favorites = defferedFavorites.await()

            favoriteViewModel.setUsers(favorites)
        }
    }

    private fun navigateToGithubUserDetail(githubUser: GithubUser) {
        val intent = Intent(this, GithubUserDetailActivity::class.java)
        intent.putExtra(GithubUserDetailActivity.EXTRA_GITHUB_USER, githubUser)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}