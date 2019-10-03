package sunhang.mathkeyboard.data

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class KbdDb(context: Context) {
    companion object {
        private const val DATABASE_NAME = "kbd_db"
    }

    @Database(entities = [SymEntity::class], version = 1, exportSchema = false)
    abstract class KbdRoom : RoomDatabase() {
        abstract fun recentSymbolDao(): RecentSymDao
    }

    private val dbCallback = object : RoomDatabase.Callback() {
        override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }

        override fun onOpen(@NonNull db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }
    }

    private val appDatabase = Room.databaseBuilder(context, KbdRoom::class.java, DATABASE_NAME)
        .addCallback(dbCallback)
        .fallbackToDestructiveMigration() // 尝试使用migration在增加表， 如果报错，会重新创建数据库
        .build()

    fun recentDao(): RecentSymDao {
        return appDatabase.recentSymbolDao()
    }

    fun dispose() {
        appDatabase.close()
    }

}

