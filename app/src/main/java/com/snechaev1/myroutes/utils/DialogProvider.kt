package com.snechaev1.myroutes.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snechaev1.myroutes.R


object DialogProvider {

    fun getErrorDialog(context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context, R.style.dialogTheme)
        builder.setTitle(context.getString(R.string.dialog_title)).setNegativeButton(context.getString(R.string.cancel)) { dialog, id -> dialog.cancel() }
        return builder
    }

    fun getInformationDialog(context: Context): MaterialAlertDialogBuilder {
        val builder = MaterialAlertDialogBuilder(context, R.style.dialogTheme)
        builder.setTitle(context.getString(R.string.dialog_title)).setNegativeButton(context.getString(R.string.ok)) { dialog, id -> dialog.cancel() }
        return builder
    }

    fun getNoGpsDialog(context: Context, action: DialogInterface.OnClickListener): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context, R.style.dialogTheme)
        builder.setTitle(context.getString(R.string.dialog_title))
                .setMessage(R.string.main_text_notGPS)
                .setCancelable(true)
                .setPositiveButton(R.string.positive_btn_notGPS) { _, _ ->
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton(R.string.no, action)
        return builder
    }

    fun gpsPermissionDialog(context: Context, action: DialogInterface.OnClickListener): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context, R.style.dialogTheme)
        builder.setTitle(context.getString(R.string.dialog_title))
                .setMessage(R.string.main_text_GPS_permission)
                .setCancelable(true)
                .setPositiveButton(R.string.positive_btn_GPS_permission, action)
                .setNegativeButton(context.getString(R.string.cancel)) { dialog, id -> dialog.cancel() }
        return builder
    }

}