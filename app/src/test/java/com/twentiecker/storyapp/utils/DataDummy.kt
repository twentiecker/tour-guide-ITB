package com.twentiecker.storyapp.utils

import com.twentiecker.storyapp.model.*

object DataDummy {

    fun generateDummyListStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://dicoding.com/test-url-foto$i.png",
                "01-01-2022 $i:00",
                "Tes Nama $i",
                "Tes deskripsi $i",
                0.0,
                "Tes-Id-$i",
                0.0
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyMapsResponse(): ListStoryResponse {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val stories = ListStoryItem(
                "https://dicoding.com/test-url-foto$i.png",
                "01-01-2021 $i:00",
                "Tes Nama $i",
                "Tes deskripsi $i",
                0.0,
                "Tes-Id-$i",
                0.0
            )
            storyList.add(stories)
        }
        return ListStoryResponse(storyList, false, "Success")
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = DataItem(
            userId = "user-oGiZSSythng9kHte",
            name = "joko",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLW9HaVpTU3l0aG5nOWtIdGUiLCJpYXQiOjE2NjgzNTUyNjF9.bXL9IADdgtwcJ3dJfIjle-rL_bdXIoPAF1vBZ6gfcIY"
        )

        return LoginResponse(
            error = false,
            message = "success",
            loginResult = loginResult
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyAddStoryResponse(): FileUploadResponse {
        return FileUploadResponse(
            error = false,
            message = "Story created successfully"
        )
    }

    fun generateDummyStoryResponse(): ListStoryResponse {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val stories = ListStoryItem(
                "https://dicoding.com/test-url-foto$i.png",
                "01-01-2021 $i:00",
                "Tes Nama $i",
                "Tes deskripsi $i",
                0.0,
                "Tes-Id-$i",
                0.0
            )
            storyList.add(stories)
        }
        return ListStoryResponse(storyList, false, "Success")
    }
}