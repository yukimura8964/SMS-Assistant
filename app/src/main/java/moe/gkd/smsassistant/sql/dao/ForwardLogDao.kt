package moe.gkd.smsassistant.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import moe.gkd.smsassistant.entity.ForwardLogEntity

@Dao
interface ForwardLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forwardLogEntity: ForwardLogEntity)

    @Query("select * from forward_log order by date desc")
    fun getAll(): Single<List<ForwardLogEntity>>

    @Query("select count(*) from forward_log")
    fun getCount(): Single<Int>

}