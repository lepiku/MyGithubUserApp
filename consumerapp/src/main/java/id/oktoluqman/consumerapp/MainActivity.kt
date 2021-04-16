package id.oktoluqman.consumerapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.oktoluqman.consumerapp.databinding.ActivityMainBinding
import id.oktoluqman.consumerapp.model.GithubUser
import id.oktoluqman.consumerapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        // set search
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    showLoading(true)
                    mainViewModel.setUsers(p0)
                    binding.searchView.clearFocus()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        // get result
        mainViewModel.getUsers().observe(this, { listGithubUser ->
            if (listGithubUser != null) {
                adapter.setData(listGithubUser)
                showLoading(false)
            }
        })
    }

    private fun navigateToGithubUserDetail(githubUser: GithubUser) {
        val intent = Intent(this@MainActivity, GithubUserDetailActivity::class.java)
        intent.putExtra(GithubUserDetailActivity.EXTRA_GITHUB_USER, githubUser)
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBackground.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.progressBackground.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
