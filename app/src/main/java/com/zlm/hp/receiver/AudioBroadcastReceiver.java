package com.zlm.hp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.zlm.down.entity.DownloadTask;
import com.zlm.hp.constants.ConfigInfo;
import com.zlm.hp.entity.AudioInfo;


/**
 * @Description: 音频监听
 * @author: zhangliangming
 * @date: 2018-10-09 21:36
 **/
public class AudioBroadcastReceiver {
    /**
     * audio的receiver的action
     */
    private static final String RECEIVER_ACTION = "com.zlm.hp.receiver.audio.action";

    /**
     * code key
     */
    private static final String ACTION_CODE_KEY = "com.zlm.hp.receiver.audio.action.code.key";

    /**
     * bundle key
     */
    public static final String ACTION_BUNDLEKEY = "com.zlm.hp.receiver.audio.action.bundle.key";

    /**
     * data key
     */
    public static final String ACTION_DATA_KEY = "com.zlm.hp.receiver.audio.action.data.key";

    /**
     * null
     */
    public static final int ACTION_CODE_NULL = 0;

    /**
     * 播放初始化
     */
    public static final int ACTION_CODE_INIT = 1;

    /**
     * 播放
     */
    public static final int ACTION_CODE_PLAY = 2;
    /**
     * 播放
     */
    public static final int ACTION_CODE_PLAYING = 3;

    /**
     * 播放本地歌曲
     */
    public static final int ACTION_CODE_SERVICE_PLAYLOCALSONG = 4;

    /**
     * 播放网络歌曲
     */
    public static final int ACTION_CODE_SERVICE_PLAYNETSONG = 5;

    /**
     * 停止播放歌曲
     */
    public static final int ACTION_CODE_STOP = 6;

    /**
     * seekto歌曲
     */
    public static final int ACTION_CODE_SEEKTO = 7;


    /**
     * 网络歌曲下载中
     */
    public static final int ACTION_CODE_DOWNLOADONLINESONG = 8;

    /**
     * 歌词加载完成
     */
    public static final int ACTION_CODE_LRCLOADED = 9;

    /**
     * 重新加载歌手写真
     */
    public static final int ACTION_CODE_RELOADSINGERIMG = 10;

    /**
     * 歌词重新加载
     */
    public static final int ACTION_CODE_LRCRELOADED = 11;

    /**
     * 歌词重新加载中
     */
    public static final int ACTION_CODE_LRCRELOADING = 12;

    /**
     * 通知栏 play
     */
    public static final int ACTION_CODE_NOTIFY_PLAY = 13;

    /**
     * 通知栏 pause
     */
    public static final int ACTION_CODE_NOTIFY_PAUSE = 14;

    /**
     * 通知栏 next
     */
    public static final int ACTION_CODE_NOTIFY_NEXT = 15;

    /**
     * 通知栏 pre
     */
    public static final int ACTION_CODE_NOTIFY_PRE = 16;

    /**
     * 通知栏 unlock
     */
    public static final int ACTION_CODE_NOTIFY_UNLOCK = 17;

    /**
     * 通知栏 lock
     */
    public static final int ACTION_CODE_NOTIFY_LOCK = 18;

    /**
     * 通知栏 桌面歌词隐藏
     */
    public static final int ACTION_CODE_NOTIFY_DESLRC_HIDE_ACTION = 19;
    public static final int ACTION_CODE_NOTIFY_DESLRC = 20;

    /**
     * 通知栏 桌面歌词显示
     */
    public static final int ACTION_CODE_NOTIFY_DESLRC_SHOW_ACTION = 21;

    /**
     * 通知栏 歌手头像加载完成
     */
    public static final int ACTION_CODE_NOTIFY_SINGERICONLOADED = 22;

    /**
     * 锁屏歌词切换
     */
    public static final int ACTION_CODE_LOCK_LRC_CHANGE = 23;

    /**
     * 更新本地歌曲
     */
    public static final int ACTION_CODE_UPDATE_LOCAL = 24;

    /**
     * 更新喜欢歌曲
     */
    public static final int ACTION_CODE_UPDATE_LIKE = 25;

    /**
     * 更新最近歌曲
     */
    public static final int ACTION_CODE_UPDATE_RECENT = 26;

    /**
     * 更新下载歌曲
     */
    public static final int ACTION_CODE_UPDATE_DOWNLOAD = 27;


    private BroadcastReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter;
    private AudioReceiverListener mReceiverListener;

    public AudioBroadcastReceiver() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(RECEIVER_ACTION);
    }

    /**
     * 注册广播
     *
     * @param context
     */
    public void registerReceiver(Context context) {

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (mReceiverListener != null) {
                    int code = intent.getIntExtra(ACTION_CODE_KEY, -1);
                    if (code != -1) {
                        mReceiverListener.onReceive(context, intent, code);
                    }
                }
            }
        };
        context.registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    /**
     * 发广播
     *
     * @param context
     * @param code
     * @param bundleKey
     * @param bundleValue
     */
    public static void sendReceiver(Context context, int code, String bundleKey, Bundle bundleValue) {
        Intent intent = new Intent(RECEIVER_ACTION);
        intent.putExtra(ACTION_CODE_KEY, code);
        if (!TextUtils.isEmpty(bundleKey) && bundleValue != null) {
            intent.putExtra(bundleKey, bundleValue);
        }
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }


    /**
     * 发广播
     *
     * @param context
     * @param code
     */
    public static void sendReceiver(Context context, int code) {
        sendReceiver(context, code, null, null);
    }

    /**
     * 发null广播
     *
     * @param context
     */
    public static void sendNullReceiver(Context context) {

        //清空当前的播放的索引
        ConfigInfo configInfo = ConfigInfo.obtain();
        configInfo.setPlayHash("");
        configInfo.save();

        sendReceiver(context, ACTION_CODE_NULL, null, null);
    }

    /**
     * 发null广播
     *
     * @param context
     */
    public static void sendStopReceiver(Context context) {
        sendReceiver(context, ACTION_CODE_STOP, null, null);
    }

    /**
     * 发播放中广播
     */
    public static void sendPlayingReceiver(Context context, AudioInfo audioInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTION_DATA_KEY, audioInfo);
        sendReceiver(context, ACTION_CODE_PLAYING, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * 发播放广播
     */
    public static void sendPlayReceiver(Context context, AudioInfo audioInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTION_DATA_KEY, audioInfo);
        sendReceiver(context, ACTION_CODE_PLAY, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * 播放网络歌曲
     *
     * @param audioInfo
     */
    public static void sendPlayNetSongReceiver(Context context, AudioInfo audioInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTION_DATA_KEY, audioInfo);
        sendReceiver(context, ACTION_CODE_SERVICE_PLAYNETSONG, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * init歌曲
     *
     * @param audioInfo
     */
    public static void sendPlayInitReceiver(Context context, AudioInfo audioInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTION_DATA_KEY, audioInfo);
        sendReceiver(context, ACTION_CODE_INIT, ACTION_BUNDLEKEY, bundle);
    }


    /**
     * 播放本地歌曲
     *
     * @param audioInfo
     */
    public static void sendPlayLocalSongReceiver(Context context, AudioInfo audioInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTION_DATA_KEY, audioInfo);
        sendReceiver(context, ACTION_CODE_SERVICE_PLAYLOCALSONG, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * 网络歌曲下载中
     *
     * @param context
     * @param task
     */
    public static void sendDownloadingOnlineSongReceiver(Context context, DownloadTask task) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTION_DATA_KEY, task);
        sendReceiver(context, ACTION_CODE_DOWNLOADONLINESONG, ACTION_BUNDLEKEY, bundle);

    }

    /**
     * seekto歌曲
     *
     * @param audioInfo
     */
    public static void sendSeektoSongReceiver(Context context, AudioInfo audioInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTION_DATA_KEY, audioInfo);
        sendReceiver(context, ACTION_CODE_SEEKTO, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * 发lrc loaded
     *
     * @param context
     */
    public static void sendLrcLoadedReceiver(Context context, String hash) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_DATA_KEY, hash);
        sendReceiver(context, ACTION_CODE_LRCLOADED, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * 发lrc reloading
     *
     * @param context
     */
    public static void sendLrcReLoadingReceiver(Context context, String hash) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_DATA_KEY, hash);
        sendReceiver(context, ACTION_CODE_LRCRELOADING, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * 发lrc reloaded
     *
     * @param context
     */
    public static void sendLrcReLoadedReceiver(Context context, String hash) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_DATA_KEY, hash);
        sendReceiver(context, ACTION_CODE_LRCRELOADED, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * @param context
     * @param hash
     */
    public static void sendReloadSingerImgReceiver(Context context, String hash) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_DATA_KEY, hash);
        sendReceiver(context, ACTION_CODE_RELOADSINGERIMG, ACTION_BUNDLEKEY, bundle);
    }

    /**
     * 获取通知栏 intent
     *
     * @param code
     * @return
     */
    public static Intent getNotifiyIntent(int code) {
        Intent intent = new Intent(RECEIVER_ACTION);
        intent.putExtra(ACTION_CODE_KEY, code);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        return intent;
    }

    /**
     * 发送notify 图片加载完成广播
     *
     * @param context
     */
    public static void sendNotifiyImgLoadedReceiver(Context context, AudioInfo audioInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTION_DATA_KEY, audioInfo);
        sendReceiver(context, ACTION_CODE_NOTIFY_SINGERICONLOADED, ACTION_BUNDLEKEY, bundle);
    }


    /**
     * 取消注册广播
     */
    public void unregisterReceiver(Context context) {
        if (mBroadcastReceiver != null) {
            context.unregisterReceiver(mBroadcastReceiver);
        }
    }

    public interface AudioReceiverListener {
        void onReceive(Context context, Intent intent, int code);
    }

    public void setReceiverListener(AudioReceiverListener audioReceiverListener) {
        this.mReceiverListener = audioReceiverListener;
    }
}
