package com.platform45.fx45.persistance.sharedprefs

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.platform45.fx45.models.UserModel

class SharedPrefs(private val application: Application) {
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    //private val sharedPreferences: SharedPreferences = getSharedPreferences("com.platform45.fx45_preferences", Context.MODE_PRIVATE);
    private val FIRSTTIME = "firstTime"
    private val USER = "user"

    var skipIntro: Boolean
        get() {
            val json = sharedPreferences.getString(FIRSTTIME, "")
            return Gson().fromJson(json, Boolean::class.java) ?: false
        }
        set(isFirstTime) {
            val editor = sharedPreferences.edit()
            val connectionsJSONString = Gson().toJson(isFirstTime)
            editor.putString(FIRSTTIME, connectionsJSONString)
            editor.commit()
        }

        var user: UserModel?
            get() {
                val json = sharedPreferences.getString(USER, "")
                return Gson().fromJson(json, UserModel::class.java)
            }
            set(userModel) {
                val editor = sharedPreferences.edit()
                val connectionsJSONString = Gson().toJson(userModel)
                editor.putString(USER, connectionsJSONString)
                editor.commit()
            }

    companion object{
        fun getInstance(application: Application): SharedPrefs {
            synchronized(this){
                var instance: SharedPrefs? = null

                if(instance == null){
                    instance = SharedPrefs(application)
                }

                return  instance
            }
        }
    }
}