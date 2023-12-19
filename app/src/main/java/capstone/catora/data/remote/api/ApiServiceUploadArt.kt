package capstone.catora.data.remote.api

import capstone.catora.data.remote.api.response.AllArtworkResponseItem
import capstone.catora.data.remote.api.response.PostLoginResponse
import capstone.catora.data.remote.api.response.PostRegisterResponse
import capstone.catora.data.remote.api.response.PostUploadArtWorkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.GET

import retrofit2.http.Multipart

import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceUploadArt {
    @Multipart
    @POST("artworks")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("user_id") user_id: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("tags") tags: RequestBody,
    ): PostUploadArtWorkResponse

}