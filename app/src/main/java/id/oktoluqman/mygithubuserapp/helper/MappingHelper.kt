package id.oktoluqman.mygithubuserapp.helper

import android.database.Cursor
import id.oktoluqman.mygithubuserapp.db.DatabaseContract
import id.oktoluqman.mygithubuserapp.model.GithubUser

object MappingHelper {
    fun mapCursorToGithubUserArrayList(cursor: Cursor?): ArrayList<GithubUser> {
        val arrayList = ArrayList<GithubUser>()

        cursor?.apply {
            while (moveToNext()) {
                val user = GithubUser(
                    id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID)),
                    username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME)),
                    htmlUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.HTML_URL)),
                    avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL)),
                )

                arrayList.add(user)
            }
        }

        return arrayList
    }
}