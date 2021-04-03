package id.oktoluqman.mygithubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header
import id.oktoluqman.mygithubuserapp.Constants
import id.oktoluqman.mygithubuserapp.model.GithubUser
import id.oktoluqman.mygithubuserapp.model.GithubUserDetail
import java.lang.reflect.Type


class DetailViewModel {
    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
        const val URL_DETAIL_USER = "https://api.github.com/users/%s"
        const val URL_LIST_FOLLOWERS = "https://api.github.com/users/%s/followers"
        const val URL_LIST_FOLLOWING = "https://api.github.com/users/%s/following"
    }

    private lateinit var githubUser: GithubUser
    private val githubUserDetail = MutableLiveData<GithubUserDetail>()
    private val githubUserFollowers = MutableLiveData<List<GithubUser>>()
    private val githubUserFollowing = MutableLiveData<List<GithubUser>>()

    fun setUser(githubUser: GithubUser) {
        this.githubUser = githubUser
        getData<GithubUserDetail>(URL_DETAIL_USER, GithubUserDetail::class.java) {
            githubUserDetail.postValue(it)
        }
        val type: Type = Types.newParameterizedType(
            MutableList::class.java,
            GithubUser::class.java,
        )
        getData<List<GithubUser>>(URL_LIST_FOLLOWERS, type) {
            githubUserFollowers.postValue(it)
        }
        getData<List<GithubUser>>(URL_LIST_FOLLOWING, type) {
            githubUserFollowing.postValue(it)
        }
    }

    fun getDetail(): LiveData<GithubUserDetail> = githubUserDetail

    fun getFollowers(): LiveData<List<GithubUser>> = githubUserFollowers

    fun getFollowing(): LiveData<List<GithubUser>> = githubUserFollowing

    private fun <T> getData(urlFormat: String, classFile: Type, save: (response: T?) -> Unit) {
        val url = urlFormat.format(githubUser.username)
        val client = AsyncHttpClient()
        client.addHeader("Authorization", Constants.GITHUB_APIKEY)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                    val jsonAdapter = moshi.adapter<T>(classFile)
                    save(jsonAdapter.fromJson(result))

                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess: ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, "onFailure: ${error?.message}")
            }
        })
    }
}