package id.oktoluqman.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

internal object DatabaseContract {

    const val AUTHORITY = "id.oktoluqman.mygithubuserapp"
    const val SCHEME = "content"

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val HTML_URL = "html_url"
            const val AVATAR_URL = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
