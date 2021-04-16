package id.oktoluqman.mygithubuserapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import id.oktoluqman.mygithubuserapp.db.DatabaseContract.FavoriteColumns.Companion._ID
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

    fun queryAll(): Cursor {
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

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = ?", arrayOf(username))
    }

    fun getByUsername(username: String): Cursor? {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
    }
}