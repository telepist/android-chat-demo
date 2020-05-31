package com.example.chatdemo.utils

import android.os.Handler
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.smoothScrollToEnd() {
    Handler().post() {
        adapter?.let {
            this.smoothScrollToPosition(it.itemCount - 1)
        }
    }
}