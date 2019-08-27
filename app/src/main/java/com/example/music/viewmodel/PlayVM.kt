package com.example.music.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.preference.PreferenceManager
import android.util.Log
import com.example.music.MusicApp
import com.example.music.PlayManger
import com.example.music.db.table.LocalMusic
import com.example.music.event.RefreshEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.example.music.R

/**
 * Created by tk on 2019/8/24
 */
class PlayVM: ViewModel() {

    val TAG = "PlayVM"
    //当前正在播放的曲目
    val song = ObservableField<LocalMusic>()
    //后台音乐播放器
    val player = PlayManger.player
    //播放/暂停图标
    val playIc = ObservableField<Int>()
    //播放模式
    val modeIndex = MutableLiveData<Int>()
    //当前音乐在播放队列的位置
    val songIndex = MutableLiveData<Int>()
    //是否播放动画
    val isPlaying = MutableLiveData<Boolean>()
    //当前播放音乐的总长度
    val mDuration = MutableLiveData<Int>()



    init {
        EventBus.getDefault().register(this)
        //获取上次结束时的模式(默认顺序播放)
        modeIndex.value = PreferenceManager.getDefaultSharedPreferences(MusicApp.context).getInt("playmode",0)
    }

    /**
     * 检查是否在播放
     */
    fun checkPlaying(){
        song.set(PlayManger.quene[PlayManger.index])
        if (PlayManger.player.isPlaying){
            isPlaying.value = true
            playIc.set(R.drawable.ic_play_running)
        }else{
            isPlaying.value = false
            playIc.set(R.drawable.ic_play_pause)
        }
        songIndex.value = PlayManger.index
        mDuration.value = PlayManger.player.duration
    }
    /**
     * 暂停/播放控制
     */
    fun pause(){
        if (PlayManger.player.isPlaying){
            isPlaying.value = false
            playIc.set(R.drawable.ic_play_pause)
            PlayManger.pause()
        }else{
            isPlaying.value = true
            playIc.set(R.drawable.ic_play_running)
            PlayManger.resume()
        }
    }

    /**
     * 播放上一曲
     */

    fun playPrevious(){
        PlayManger.playPreVious()
    }
    /**
     * 播放下一曲
     */
    fun playNext(){
        PlayManger.playNext()
    }

    /**
     * 当播放完成后刷新UI
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun update(event: RefreshEvent){

        song.set(event.song)
        isPlaying.value = true
        playIc.set(R.drawable.vector_drawable_play_black)
        songIndex.value = PlayManger.index
    }




    /**
     * 拖拽进度条
     */
    fun changeProcess(process: Int){
        PlayManger.changeProgress(process)
    }

    /**
     * 更换播放模式
     */
    fun changeMode(){
        modeIndex.value = (1+ modeIndex.value!!) % 3
        Log.d(TAG,modeIndex.value.toString())
        PlayManger.setMode(modeIndex.value!!)
    }



    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }
}