package id.oktoluqman.mygithubuserapp.model

import com.squareup.moshi.Json

data class GithubUser(
    @Json(name = "login")
    val username: String,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,
)