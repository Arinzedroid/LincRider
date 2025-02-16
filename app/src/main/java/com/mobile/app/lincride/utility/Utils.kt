package com.mobile.app.lincride.utility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.text.NumberFormat

object Utils {

    fun hasPermission(applicationContext: Context): Boolean{
        return (checkFindLocation(applicationContext) && checkCoarseLocation(applicationContext))
    }

    private fun checkFindLocation(applicationContext: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCoarseLocation(applicationContext: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun formatCurrency(amt: Double?): String{
        val ft = NumberFormat.getCurrencyInstance()
        return when{
            (amt == null) ->
                "$ --"
            (amt < 0) ->
                "$-${ft.format(amt).substring(2)}"
            else ->
                "$${ft.format(amt).substring(1)}"
        }
    }

}
