package id.oktoluqman.mygithubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import id.oktoluqman.mygithubuserapp.databinding.ActivityGithubUserDetailBinding
import id.oktoluqman.mygithubuserapp.model.GithubUserOld

class GithubUserDetailActivity : AppCompatActivity() {

    private lateinit var mGithubUserOld: GithubUserOld
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
        supportActionBar?.title = mGithubUserOld.name
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
        mGithubUserOld = intent.getParcelableExtra(EXTRA_GITHUB_USER)!!

        Glide.with(this).load(mGithubUserOld.avatar).into(binding.imgUserPhoto)
        binding.tvUserName.text = mGithubUserOld.name
        binding.tvUserUsername.text = mGithubUserOld.username
        binding.tvUserLocation.text = mGithubUserOld.location
        binding.tvUserCompany.text = getString(R.string.company, mGithubUserOld.company)
        binding.tvUserRepository.text = getString(R.string.repository, mGithubUserOld.repository)
        binding.tvUserFollowers.text = getString(R.string.followers, mGithubUserOld.followers)
        binding.tvUserFollowing.text = getString(R.string.following, mGithubUserOld.following)
    }

    fun shareUser() {
        val sharedText = "https://github.com/${mGithubUserOld.username}/"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "plain/text"
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}
