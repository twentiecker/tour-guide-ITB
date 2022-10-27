package com.twentiecker.storyapp.utils

import com.twentiecker.storyapp.model.ListStoryItem

object DataDummy {
    fun generateDummyStoryList(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val story = ListStoryItem(
                "Title $i",
                "2022-02-22T22:22:22Z",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "https://www.dicoding.com/",
                0.0,
                "id",
                0.0
            )
            storyList.add(story)
        }
        return storyList
    }
}