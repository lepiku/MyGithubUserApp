package id.oktoluqman.mygithubuserapp.db

import android.provider.BaseColumns

internal object DatabaseContract {
    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val HTML_URL = "html_url"
            const val AVATAR_URL = "avatar_url"
        }
    }
}
