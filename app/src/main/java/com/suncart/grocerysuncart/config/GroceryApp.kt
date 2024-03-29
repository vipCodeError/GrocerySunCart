package com.suncart.grocerysuncart.config

import android.app.Application
import android.content.Context
import com.dbflow5.config.DatabaseConfig
import com.dbflow5.config.FlowConfig
import com.dbflow5.config.FlowManager
import com.dbflow5.database.AndroidSQLiteOpenHelper
import com.suncart.grocerysuncart.api.ContentApi
import com.suncart.grocerysuncart.api.TokenUpdaterApi
import com.suncart.grocerysuncart.api.UserApi
import com.suncart.grocerysuncart.database.AppDatabase
import com.suncart.grocerysuncart.util.SharedPrefsUtils


open class GroceryApp : Application(){

    companion object {
        fun saveLoginNumber(context: Context, number : String){
            SharedPrefsUtils.setStringPreference(context, "loginNumber", number)
        }

        fun saveLogin(context: Context, yesNo : Boolean){
            SharedPrefsUtils.setBooleanPreference(context, "isLogin", yesNo)
        }

        fun getLoginNumber(context: Context) : String? {
            return SharedPrefsUtils.getStringPreference(context, "loginNumber")
        }

        fun isUserLogged(context: Context) : Boolean?{
            return SharedPrefsUtils.getBooleanPreference(context, "isLogin", false)
        }

        fun setTokenLocally(context: Context, tokenVerfication : String){
            SharedPrefsUtils.setStringPreference(context,"token_str" ,tokenVerfication)
        }

        fun getTokenString(context: Context) : String?{
            return SharedPrefsUtils.getStringPreference(context, "token_str")
        }

        fun saveUserId(context: Context, id : String){
            SharedPrefsUtils.setStringPreference(context, "user_id", id)
        }

        fun getUserId(context: Context) : String?{
            return SharedPrefsUtils.getStringPreference(context, "user_id")
        }

        fun getGroceryData() : ContentApi {
           return APIClient.getClient().create(ContentApi::class.java)
        }

        fun tokenUpdatation() : TokenUpdaterApi{
            return APIClient.getClient().create(TokenUpdaterApi::class.java)
        }

        fun getPhoneValidation() : UserApi{
            return APIClient.getClient().create(UserApi::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(
            FlowConfig.Builder(this)
                .database(
                    DatabaseConfig.builder(AppDatabase::class, AndroidSQLiteOpenHelper.createHelperCreator(this))
                        .databaseName("GroceryDb")
                        .build())
                .build());
    }



}