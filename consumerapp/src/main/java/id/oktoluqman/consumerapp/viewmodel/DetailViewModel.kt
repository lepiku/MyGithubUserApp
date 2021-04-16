package id.oktoluqman.consumerapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header
import id.oktoluqman.consumerapp.Constants
import id.oktoluqman.consumerapp.model.GithubUser
import id.oktoluqman.consumerapp.model.GithubUserDetail

class DetailViewModel : ViewModel() {
    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
        const val URL_DETAIL_USER = "https://api.github.com/users/%s"
    }

    private lateinit var githubUser: GithubUser
    private val githubUserDetail = MutableLiveData<GithubUserDetail>()

    fun setUser(githubUser: GithubUser) {
        this.githubUser = githubUser
        getData()
    }

    fun getDetail(): LiveData<GithubUserDetail> = githubUserDetail

    private fun getData() {
        val url = URL_DETAIL_USER.format(githubUser.username)
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
                    val jsonAdapter = moshi.adapter(GithubUserDetail::class.java)
                    githubUserDetail.postValue(jsonAdapter.fromJson(result))

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