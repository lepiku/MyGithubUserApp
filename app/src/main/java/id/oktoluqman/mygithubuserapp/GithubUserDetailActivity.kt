package id.oktoluqman.mygithubuserapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.oktoluqman.mygithubuserapp.databinding.ActivityGithubUserDetailBinding
import id.oktoluqman.mygithubuserapp.db.GithubUserHelper
import id.oktoluqman.mygithubuserapp.model.GithubUser
import id.oktoluqman.mygithubuserapp.viewmodel.DetailViewModel
import kotlin.properties.Delegates

class GithubUserDetailActivity : AppCompatActivity() {
    private lateinit var mGithubUser: GithubUser
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding: ActivityGithubUserDetailBinding
    private lateinit var githubUserHelper: GithubUserHelper
    private var statusFavorite = false

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.list_follower,
            R.string.list_following,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGithubUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mGithubUser = intent.getParcelableExtra(EXTRA_GITHUB_USER)!!

        createHeader()
        createTabLayout()

        githubUserHelper = GithubUserHelper.getInstance(applicationContext)
        githubUserHelper.open()
        val user = githubUserHelper.getByUsername(mGithubUser.username)
        setStatusFavorite(user != null)
        githubUserHelper.close()

        binding.btnFavorite.setOnClickListener {
            setStatusFavorite(!statusFavorite)
            githubUserHelper.open()
            if (statusFavorite)
                githubUserHelper.insertUser(mGithubUser)
            else
                githubUserHelper.deleteByUsername(mGithubUser.username)
            githubUserHelper.close()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                shareUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareUser() {
        val sharedText = "https://github.com/${mGithubUser.username}"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "plain/text"
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
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

    private fun createHeader() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mGithubUser.username
        showLoading(true)

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        detailViewModel.setUser(mGithubUser)

        detailViewModel.getDetail().observe(this) {
            showLoading(false)
            binding.tvUserName.setTextOrGone(it.name)
            binding.tvUserHtmlUrl.text = it.htmlUrl
            binding.tvUserLocation.setTextOrGone(it.location)
            binding.tvUserCompany.setTextOrGone(it.company, R.string.company)
            binding.tvUserRepository.text =
                resources.getQuantityString(R.plurals.n_repository, it.publicRepos, it.publicRepos)
            binding.tvUserFollowers.text =
                resources.getQuantityString(R.plurals.n_followers, it.followers, it.followers)
            binding.tvUserFollowing.text = getString(R.string.n_following, it.following)

            Glide.with(this).load(it.avatarUrl).into(binding.imgUserPhoto)
        }
    }

    private fun TextView.setTextOrGone(str: String?, strFormat: Int? = null) {
        if (str != null) {
            if (strFormat != null) {
                this.text = resources.getString(strFormat, str)
            } else {
                this.text = str
            }
        } else {
            this.visibility = View.GONE
        }
    }

    private fun createTabLayout() {
        val adapter = GithubUserDetailTabAdapter(this, mGithubUser.username)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = adapter

        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setStatusFavorite(status: Boolean) {
        statusFavorite = status
        if (status)
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        else
            binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }
}
