package id.oktoluqman.mygithubuserapp.model

import com.squareup.moshi.Json

data class GithubUserDetail(
    @Json(name = "login")
    val username: String,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    val name: String?,

    val company: String?,

    val blog: String?,

    val location: String?,

    val bio: String?,

    @Json(name = "public_repos")
    val publicRepos: Int,

    val followers: Int,

    val following: Int,
)