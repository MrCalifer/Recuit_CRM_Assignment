package edu.califer.recuit_crmassignment.Utils

import android.content.Context

class HelperClass {

    companion object {

        fun loginSharedPreference(ctx: Context?) {
            val editor = ctx!!.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE).edit()
            editor.apply()
        }

        /**
         * Get Logged in for user
         */
        @JvmStatic
        fun getUserLoggedIn(context: Context): Boolean {
            val prefs = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
            return prefs.getBoolean("loggedIn", false)
        }

        /**
         * Function to set logged in for user.
         */
        fun setUserLoggedIn(context: Context, data: Boolean) {
            val editor = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE).edit()
            editor.putBoolean("loggedIn", data)
            editor.apply()
        }

        /**
         * Function to get weather user manual is displayed to user for first time or not.
         */
        @JvmStatic
        fun isManualShowed(context: Context): Boolean {
            val editor = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
            return editor.getBoolean("Manual", false)
        }

        /**
         * Function to set weather user manual is displayed to user for first time or not.
         */
        fun setManualShowed(context: Context, data: Boolean) {
            val editor = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE).edit()
            editor.putBoolean("Manual", data)
            editor.apply()
        }

        /**
         * Function to logout user.
         */
        fun logout(context: Context?) {
            val editor = context!!.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
            editor.edit().clear().apply()
        }

    }

}