package id.oktoluqman.mygithubuserapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.AUTHORITY
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import id.oktoluqman.mygithubuserapp.db.GithubUserHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_USERNAME = 2

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var githubUserHelper: GithubUserHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", FAVORITE_USERNAME)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (sUriMatcher.match(uri)) {
            FAVORITE_USERNAME -> githubUserHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (sUriMatcher.match(uri)) {
            FAVORITE -> githubUserHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        githubUserHelper = GithubUserHelper.getInstance(context as Context)
        githubUserHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> githubUserHelper.queryAll()
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}