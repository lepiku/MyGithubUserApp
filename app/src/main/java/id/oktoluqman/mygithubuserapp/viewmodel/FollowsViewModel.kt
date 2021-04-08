package id.oktoluqman.mygithubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header
import id.oktoluqman.mygithubuserapp.Constants
import id.oktoluqman.mygithubuserapp.model.GithubUser
import java.lang.reflect.Type

class FollowsViewModel : ViewModel() {
    private val githubUsers = MutableLiveData<List<GithubUser>>()

    companion object {
        private val TAG = FollowsViewModel::class.java.simpleName
    }

    fun setUsers(username: String, urlFormat: String) {
        val url = urlFormat.format(username)
        getData(url)
    }

    private fun getData(url: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", Constants.GITHUB_API_KEY)
        client.addHeader("User-Agent", "request")
        val type: Type = Types.newParameterizedType(
            MutableList::class.java,
            GithubUser::class.java,
        )
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
                    val jsonAdapter = moshi.adapter<List<GithubUser>>(type)
                    val response = jsonAdapter.fromJson(result)

                    githubUsers.postValue(response)

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

    fun getUsers(): LiveData<List<GithubUser>> = githubUsers
}