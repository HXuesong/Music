package com.example.music.network.services


import com.example.music.model.bean.MusicBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by tk on 2019/9/1
 */
interface MusicService {
    @GET("song/detail")
            /**
             * 根据歌曲id获得歌曲信息
             */
    fun getSong(@Query("ids")id: Long) : Observable<MusicBean>
}