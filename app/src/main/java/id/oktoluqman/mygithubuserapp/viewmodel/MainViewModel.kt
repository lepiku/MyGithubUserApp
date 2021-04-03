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
import id.oktoluqman.mygithubuserapp.model.GithubUser
import id.oktoluqman.mygithubuserapp.model.GithubUserQueryResponse

class MainViewModel : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val listGithubUsers = MutableLiveData<List<GithubUser>>()

    fun setUsers(query: String) {
        val url = "https://api.github.com/search/users?q=$query"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", Constants.GITHUB_API_KEY)
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
                    val jsonAdapter = moshi.adapter(GithubUserQueryResponse::class.java)
                    val response = jsonAdapter.fromJson(result)

                    listGithubUsers.postValue(response?.items)
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

    fun getUsers(): LiveData<List<GithubUser>> {
        return listGithubUsers
    }
}