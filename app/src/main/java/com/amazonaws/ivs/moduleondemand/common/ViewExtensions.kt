package com.amazonaws.ivs.moduleondemand.common

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}
