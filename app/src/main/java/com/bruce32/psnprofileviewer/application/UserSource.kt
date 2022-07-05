package com.bruce32.psnprofileviewer.application

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface UserSource {
    suspend fun currentPsnId(): String?
    suspend fun setCurrentPsnId(id: String)
}

class UserSourceImpl private constructor(
    private val context: Context
) : UserSource {

    companion object {
        private var instance: UserSource? = null
        private val currentUserKey = stringPreferencesKey("CurrentUser")
        private val Context.profileDataStore by preferencesDataStore("profile-data-store")

        fun initialize(context: Context) {
            if (instance == null) {
                instance = UserSourceImpl(context)
            }
        }

        fun get() = checkNotNull(instance) {
            "UserSourceImpl must be initialized"
        }
    }

    override suspend fun currentPsnId(): String? =
        context.profileDataStore.data.map { it[currentUserKey] }.first()

    override suspend fun setCurrentPsnId(id: String) {
        context.profileDataStore.edit {
            Log.d("Pref", "setting value to $id")
            it[currentUserKey] = id
            Log.d("Pref", "set value to $id")
            Log.d("Pref", "current value is ${currentPsnId()}")
        }
    }
}