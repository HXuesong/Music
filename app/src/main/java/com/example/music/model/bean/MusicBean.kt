package com.example.music.model.bean

import com.example.music.model.bean.Track

/**
 * Created by tk on 2019/9/1
 * 根据歌曲id获得的歌曲bean类
 */
data class MusicBean(
    val code: Int,
    //val privileges: List<Privilege>,
    val songs: List<Track>
)
