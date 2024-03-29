package com.bruce32.psnprofileviewer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bruce32.psnprofileviewer.model.CurrentUser
import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.Trophy
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE psnId=(SELECT psnId FROM currentUser) COLLATE NOCASE")
    fun getProfile(): Flow<Profile?>

    @Query("SELECT * FROM game WHERE playerPsnId=(SELECT psnId FROM currentUser) COLLATE NOCASE")
    fun getGames(): Flow<List<Game>>

    @Query("SELECT * FROM trophy WHERE gameId=(:gameId) AND playerPsnId=(SELECT psnId FROM currentUser) COLLATE NOCASE")
    fun getTrophies(gameId: String): Flow<List<Trophy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<Game>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrophies(trophies: List<Trophy>)

    @Query("SELECT * from currentUser")
    fun getCurrentUser(): Flow<CurrentUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setCurrentUser(user: CurrentUser)
}
