package id.oktoluqman.mygithubuserapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubUser(
    @Json(name = "login")
    val username: String,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,
) : Parcelable

data class GithubUserQueryResponse(
    @Json(name = "total_count")
    val totalCount: Int,
    val items: List<GithubUser>,
)