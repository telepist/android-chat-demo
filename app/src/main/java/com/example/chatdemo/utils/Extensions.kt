package com.example.chatdemo.utils

import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.smoothScrollToEnd() {
    Handler().post() {
        adapter?.let {
            this.smoothScrollToPosition(it.itemCount - 1)
        }
    }
}

fun RecyclerView.isScrolledToBottom(): Boolean {
    layoutManager.let {
        if (it is LinearLayoutManager) {
            return it.findLastVisibleItemPosition() < 0 ||
                    it.itemCount - it.findLastVisibleItemPosition() <= 2
        }
    }
    return true
}

fun String.mask() =
    this.replace(".".toRegex(), "*")
