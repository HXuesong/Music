package com.example.music.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music.db.table.LocalMusic
import com.example.music.databinding.RecycleItemLocalMusicBinding
import com.example.music.R
import com.example.music.activity.PlayingActivity
import com.example.music.event.IndexEvent
import com.example.music.event.QueneEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity


/**
 * Created by tk on 2019/8/17
 * 本地音乐列表适配器
 */
class LocalMusicAdapter(val list: ArrayList<LocalMusic>,val context: Context) : RecyclerView.Adapter<LocalMusicAdapter.ViewHolder>(){
    //正在播放的位置
    var playingId = -1
    var listener: OnPopMoreClickListener? = null

    var hashSetQuene = false



    interface OnPopMoreClickListener {
        fun onPopMoreClick(music: LocalMusic,position: Int)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding : RecycleItemLocalMusicBinding= DataBindingUtil.inflate(LayoutInflater.from(p0.context)
            ,R.layout.recycle_item_local_music
            ,p0
            ,false)

        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (playingId == p1){
            p0.itembinding.ivPlaying.visibility = View.VISIBLE

        }else{
            p0.itembinding.ivPlaying.visibility = View.GONE

        }
        p0.bind(list[p1])

        //点击整体播放
        p0.itembinding.llLocalMusicParent.setOnClickListener {
            //已经发送播放列表
            if (hashSetQuene){
                if (playingId == p1) {
                    //第二次点击跳转至详情页
                    context.startActivity<PlayingActivity>()
                } else {
                    refreshPlayId(p1)
                    EventBus.getDefault().post(IndexEvent(p1))
                }
            }else{
                if (playingId == p1) {
                    //第二次点击跳转至详情页
                    context.startActivity<PlayingActivity>()
                }else{
                    refreshPlayId(p1)
                    EventBus.getDefault().post(QueneEvent(list,p1))
                }
            }

        }

        //点击右边的弹出更多操作，删除，或添加到歌单
        p0.itembinding.ivPopMore.setOnClickListener {
            listener?.onPopMoreClick(list[p0.adapterPosition],p0.adapterPosition)
        }

    }


    fun refreshPlayId(newPlayId: Int){
        val lastId = playingId
        playingId = newPlayId
        notifyItemChanged(playingId)
        notifyItemChanged(lastId)
    }

    /**
     * 删除某一条
     */
    fun delete(position: Int){
        list.removeAt(position)
        if (position == playingId){
            playingId = -1
        }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,list.size-position)
    }


    class ViewHolder(val itembinding: RecycleItemLocalMusicBinding) : RecyclerView.ViewHolder(itembinding.root) {
        fun bind(localMusic: LocalMusic){
            itembinding.localmusic = localMusic
        }

    }
}