package id.oktoluqman.mygithubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.oktoluqman.mygithubuserapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var githubUsers = arrayListOf<GithubUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addItem()
        showRecyclerList()
    }

    private fun addItem() {
        val dataUsername = resources.getStringArray(R.array.username)
        val dataName = resources.getStringArray(R.array.name)
        val dataLocation = resources.getStringArray(R.array.location)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataFollowers = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)

        for (position in dataUsername.indices) {
            val mGithubUser = GithubUser(
                dataUsername[position],
                dataName[position],
                dataLocation[position],
                dataRepository[position].toInt(),
                dataCompany[position],
                dataFollowers[position].toInt(),
                dataFollowing[position].toInt(),
                dataAvatar.getResourceId(position, -1),
            )
            githubUsers.add(mGithubUser)
        }

        dataAvatar.recycle()
    }

    private fun showRecyclerList() {
        binding.rvGithubUser.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = ListGithubUserAdapter(githubUsers) {
            navigateToGithubUserDetail(it)
        }
        binding.rvGithubUser.adapter = listHeroAdapter

        binding.rvGithubUser.setHasFixedSize(true)
        binding.rvGithubUser.addItemDecoration(
            DividerItemDecoration(
                binding.rvGithubUser.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun navigateToGithubUserDetail(githubUser: GithubUser) {
        val intent = Intent(this@MainActivity, GithubUserDetailActivity::class.java)
        intent.putExtra(GithubUserDetailActivity.EXTRA_GITHUB_USER, githubUser)
        startActivity(intent)
    }
}