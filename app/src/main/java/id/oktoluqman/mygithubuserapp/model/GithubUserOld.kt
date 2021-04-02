package id.oktoluqman.mygithubuserapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubUserOld(
    var username: String,
    var name: String,
    var location: String,
    var repository: Int,
    var company: String,
    var followers: Int,
    var following: Int,
    var avatar: Int,
) : Parcelable