package com.bruce32.psnprofileviewer.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.Trophy
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [Profile::class, Game::class, Trophy::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ProfileTypeConverters::class)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE psnId=(:psnId)")
    suspend fun getProfile(psnId: String): Profile

    @Query("SELECT * FROM game WHERE playerPsnId=(:psnId)")
    fun getGames(psnId: String): Flow<List<Game>>

    @Query("SELECT * FROM trophy WHERE gameId=(:gameId) AND playerPsnId=(:psnId)")
    fun getTrophies(gameId: String, psnId: String): Flow<List<Trophy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<Game>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrophies(trophies: List<Trophy>)
}
