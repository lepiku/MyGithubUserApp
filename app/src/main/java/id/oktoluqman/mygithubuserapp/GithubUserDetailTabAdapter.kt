package id.oktoluqman.mygithubuserapp

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class GithubUserDetailTabAdapter(
    activity: GithubUserDetailActivity,
    private val username: String,
) :
    FragmentStateAdapter(activity) {

    companion object {
        const val URL_LIST_FOLLOWERS = "https://api.github.com/users/%s/followers"
        const val URL_LIST_FOLLOWING = "https://api.github.com/users/%s/following"
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowsFragment.newInstance(username, URL_LIST_FOLLOWING)
            1 -> fragment = FollowsFragment.newInstance(username, URL_LIST_FOLLOWERS)
        }
        return fragment as Fragment
    }
}