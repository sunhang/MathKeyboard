package sunhang.mathkeyboard.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_symbols")
class SymEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val content: String,
    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long
) {
    constructor() : this("", 0L)
}
