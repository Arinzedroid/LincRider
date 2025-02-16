package com.mobile.app.lincride.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment

fun AppCompatActivity.openLocationSettings(){
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }catch (ex: Exception){
        ex.printStackTrace()
    }
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
    BundleCompat.getParcelable(this, key, T::class.java)

fun View.toggleVisibility(visible: Boolean){
    if(visible && this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
        return
    }
    if(!visible && this.visibility != View.GONE)
        this.visibility = View.GONE
}

fun View.hideKeyboard() {
    try{
        val inputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputMethodManager.isActive)
            inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }catch (ex: Exception){
        ex.printStackTrace()
    }

}

fun Fragment.toast(msg: String){
    Toast.makeText(this.requireContext(),msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toast(msg: String){
    Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
}
