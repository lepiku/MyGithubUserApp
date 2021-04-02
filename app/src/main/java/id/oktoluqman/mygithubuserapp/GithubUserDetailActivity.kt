package id.oktoluqman.mygithubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import id.oktoluqman.mygithubuserapp.databinding.ActivityGithubUserDetailBinding

class GithubUserDetailActivity : AppCompatActivity() {

    private lateinit var mGithubUser: GithubUser
    private lateinit var binding: ActivityGithubUserDetailBinding

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGithubUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUser()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mGithubUser.name
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

    private fun loadUser() {
        mGithubUser = intent.getParcelableExtra(EXTRA_GITHUB_USER)

        Glide.with(this).load(mGithubUser.avatar).into(binding.imgUserPhoto)
        binding.tvUserName.text = mGithubUser.name
        binding.tvUserUsername.text = mGithubUser.username
        binding.tvUserLocation.text = mGithubUser.location
        binding.tvUserCompany.text = getString(R.string.company, mGithubUser.company)
        binding.tvUserRepository.text = getString(R.string.repository, mGithubUser.repository)
        binding.tvUserFollowers.text = getString(R.string.followers, mGithubUser.followers)
        binding.tvUserFollowing.text = getString(R.string.following, mGithubUser.following)
    }

    fun shareUser() {
        val sharedText = "https://github.com/${mGithubUser.username}/"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "plain/text"
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}
