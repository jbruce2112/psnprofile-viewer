package com.bruce32.psnprofileviewer.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.Trophy
import kotlinx.coroutines.flow.Flow

@Database(entities = [Profile::class, Game::class, Trophy::class], version = 2, exportSchema = false)
@TypeConverters(ProfileTypeConverters::class)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE psnId=(:psnId)")
    fun getProfile(psnId: String): Flow<Profile>

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

class ProfilePersistence private constructor(context: Context) {

    private val database: ProfileDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ProfileDatabase::class.java,
            "profile"
        )
        .fallbackToDestructiveMigration()
        .build()

    companion object {
        private var instance: ProfilePersistence? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = ProfilePersistence(context)
            }
        }

        fun get() = checkNotNull(instance) {
            "ProfilePersistence must be initialized"
        }
    }

    fun getProfile(psnId: String) = database.profileDao().getProfile(psnId)

    fun getGames(psnId: String) = database.profileDao().getGames(psnId)

    fun getTrophies(gameId: String, psnId: String) = database.profileDao().getTrophies(gameId, psnId)

    suspend fun insertProfile(profile: Profile) = database.profileDao().insertProfile(profile)

    suspend fun insertGames(games: List<Game>) = database.profileDao().insertGames(games)

    suspend fun insertTrophies(trophies: List<Trophy>) = database.profileDao().insertTrophies(trophies)
}