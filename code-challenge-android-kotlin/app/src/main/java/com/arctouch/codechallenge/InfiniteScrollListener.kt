package com.arctouch.codechallenge

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView

abstract class InfiniteScrollListener(private var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    private var isScrolling = false
    private var currentItems = 0
    private var scrolledOutItems = 0
    private var totalItems = 0

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        currentItems = layoutManager.childCount
        scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
        totalItems = layoutManager.itemCount

        if(isScrolling && (currentItems + scrolledOutItems == totalItems)){
            isScrolling = false
            loadMore()
        }
    }


    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            isScrolling = true
    }

    abstract fun loadMore()
}