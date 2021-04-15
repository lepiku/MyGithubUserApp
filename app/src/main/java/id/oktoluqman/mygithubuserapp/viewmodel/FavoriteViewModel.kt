package id.oktoluqman.mygithubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header
import id.oktoluqman.mygithubuserapp.Constants
import id.oktoluqman.mygithubuserapp.db.GithubUserHelper
import id.oktoluqman.mygithubuserapp.model.GithubUser
import id.oktoluqman.mygithubuserapp.model.GithubUserQueryResponse

class FavoriteViewModel : ViewModel() {

    private lateinit var githubUserHelper: GithubUserHelper

    companion object {
        private val TAG = FavoriteViewModel::class.java.simpleName
    }

    private val listGithubUsers = MutableLiveData<List<GithubUser>>()

    fun setUsers(users: List<GithubUser>) {
        listGithubUsers.postValue(users)
    }

    fun getUsers(): LiveData<List<GithubUser>> {
        return listGithubUsers
    }
}