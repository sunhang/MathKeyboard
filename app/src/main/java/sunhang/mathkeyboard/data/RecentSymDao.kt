package sunhang.mathkeyboard.data

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface RecentSymDao {
    @Query("SELECT * FROM recent_symbols ORDER BY time_stamp DESC")
    fun getAll(): Flowable<List<SymEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: SymEntity)
    @Delete
    fun delete(entity: SymEntity)
    @Query("DELETE FROM recent_symbols WHERE time_stamp = (SELECT MIN(time_stamp) FROM recent_symbols)")
    fun deleteOldest()
    @Query("SELECT COUNT(*) FROM recent_symbols")
    fun getCount(): Int
}