package id.oktoluqman.mygithubuserapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion.HTML_URL
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion._ID
import id.oktoluqman.mygithubuserapp.model.GithubUser
import java.sql.SQLException

class GithubUserHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: GithubUserHelper? = null

        fun getInstance(context: Context): GithubUserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: GithubUserHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }


    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    private fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    private fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    private fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    fun getAllUsers(): ArrayList<GithubUser> {
        val arrayList = ArrayList<GithubUser>()
        val cursor = queryAll()
        cursor.moveToFirst()

        var user: GithubUser
        if (cursor.count > 0) {
            do {
                user = GithubUser(
                    username = cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)),
                    htmlUrl = cursor.getString(cursor.getColumnIndexOrThrow(HTML_URL)),
                    avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR_URL)),
                )
                user.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))

                arrayList.add(user)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }

        cursor.close()
        return arrayList
    }

    fun insertUser(user: GithubUser): Long {
        val args = ContentValues()
        args.put(USERNAME, user.username)
        args.put(HTML_URL, user.htmlUrl)
        args.put(AVATAR_URL, user.avatarUrl)
        return insert(args)
    }

    fun deleteUser(id: Int): Int {
        return deleteById("$id")
    }
}