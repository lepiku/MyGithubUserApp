package id.oktoluqman.mygithubuserapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.oktoluqman.mygithubuserapp.databinding.FragmentFollowsBinding
import id.oktoluqman.mygithubuserapp.model.GithubUser
import id.oktoluqman.mygithubuserapp.viewmodel.FollowsViewModel

private const val ARG_PARAM1 = "username"
private const val ARG_PARAM2 = "url_format"

class FollowsFragment : Fragment() {
    private var binding: FragmentFollowsBinding? = null
    private lateinit var followsViewModel: FollowsViewModel
    private lateinit var username: String
    private lateinit var urlFormat: String

    companion object {
        @JvmStatic
        fun newInstance(username: String, urlFormat: String) =
            FollowsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, username)
                    putString(ARG_PARAM2, urlFormat)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            username = it.getString(ARG_PARAM1)!!
            urlFormat = it.getString(ARG_PARAM2)!!
        }

        binding = FragmentFollowsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        createRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun navigateToGithubUserDetail(githubUser: GithubUser) {
        val intent = Intent(context, GithubUserDetailActivity::class.java)
        intent.putExtra(GithubUserDetailActivity.EXTRA_GITHUB_USER, githubUser)
        startActivity(intent)
    }

    private fun createRecyclerView() {
        val adapter = ListGithubUserAdapter {
            navigateToGithubUserDetail(it)
        }

        binding?.rvGithubUser?.layoutManager = LinearLayoutManager(context)
        binding?.rvGithubUser?.adapter = adapter
        binding?.rvGithubUser?.addItemDecoration(
            DividerItemDecoration(
                binding?.rvGithubUser?.context,
                DividerItemDecoration.VERTICAL
            )
        )

        followsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowsViewModel::class.java)

        followsViewModel.setUsers(username, urlFormat)
        followsViewModel.getUsers().observe(viewLifecycleOwner) {
            adapter.setData(it)
            showLoading(false)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.progressBackground?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
            binding?.progressBackground?.visibility = View.GONE
        }
    }
}