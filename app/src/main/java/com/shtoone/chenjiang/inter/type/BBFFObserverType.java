package com.shtoone.chenjiang.inter.type;

import com.shtoone.chenjiang.common.Constants;
import com.shtoone.chenjiang.inter.common.ObserverRegion;
import com.shtoone.chenjiang.inter.common.ObserverType;
import com.shtoone.chenjiang.mvp.model.entity.db.CezhanData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/21.
 * 此观测类型为BBFF,没有往返测，奇数站和偶数站观测顺序均为BBFF
 */

@ObserverRegion(ORFINAL=Constants.TYPE4)
public class BBFFObserverType implements ObserverType {
    private int[] arrayBBFF = {0, Constants.b1, Constants.b2, Constants.f1, Constants.f2};
    //初步推测BBFF的语音提示如下
    private int[] arrayAudioBBFF = {0, Constants.AUDIO_NEXTB, Constants.AUDIO_NEXTF, Constants.AUDIO_NEXTF, 0};
    int[] arraySequence = {0};
    int[] arrayAudio = {0};

    @Override
    public Map<ArrayType, Object> setObserVerOrder(CezhanData mCezhanData) {
        Map<ArrayType,Object> retMap = new HashMap<ArrayType, Object>();
        if (mCezhanData.getObserveType().equals(Constants.BBFF)) {
            //偶数站
            arraySequence = arrayBBFF;
            arrayAudio = arrayAudioBBFF;
        }
        retMap.put(ArrayType.ARRAYAUDIO,arrayAudio);
        retMap.put(ArrayType.ARRAYSEQUENCE,arraySequence);
        return retMap;
    }
}
