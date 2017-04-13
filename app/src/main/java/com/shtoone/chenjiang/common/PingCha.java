package com.shtoone.chenjiang.common;

import android.content.Context;

import com.shtoone.chenjiang.mvp.model.entity.db.CezhanData;
import com.shtoone.chenjiang.mvp.model.entity.db.RTData;
import com.shtoone.chenjiang.mvp.model.entity.db.ShuizhunxianData;
import com.socks.library.KLog;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */
public class PingCha {

    private static final String TAG = PingCha.class.getSimpleName();
    private static PingCha INSTANCE;
    private static Context mContext;
    private CezhanData cezhanData;
    private ShuizhunxianData shuizhunxianData;
    private  boolean b;
    private float wangCeGaocheng;
    private float zhongdiangaocheng;
    private float fanceGaocheng;
    private float qishidiangaocheng;
//    private static float ceduanchangdu;
    private float ceDuanChangdu;
    private float xiugaihougaocheng;
    private DecimalFormat mDecimalFormat;
    float wangcecezhangaocha=0;
    float fanceCezhangaocha=0;
    float v=0;
    float[] s;

    public  PingCha() {
        mDecimalFormat = new DecimalFormat("0.00000");
    }

    public void initGaocha(List<CezhanData> mCezhanDataList){
        s=new float[mCezhanDataList.size()];
        for(int i=0;i<mCezhanDataList.size();i++){
            CezhanData mCezhanData=mCezhanDataList.get(i);
            if((mCezhanData.getMeasureDirection()).equals(Constants.wangce)){
                float cezhangaocha = Float.parseFloat(mCezhanData.getCezhangaocha());
                s[i]=cezhangaocha;
            }else if((mCezhanData.getMeasureDirection()).equals(Constants.fance)){
                float fancezhangaocha = Float.parseFloat(mCezhanData.getCezhangaocha());
                s[i]=fancezhangaocha;
            }
        }
    }

    public float countWangceGaocha(){
        if(null!=s){
            for(int i=0;i<s.length/2;i++){
                wangcecezhangaocha+=s[i];
            }
        }
        KLog.e(TAG,"==========countWangceGaocha==========");
        return wangcecezhangaocha;
    }

    public float countFanceGaocha(){
        if(null!=s){
            for(int i=s.length/2;i<s.length;i++){
                fanceCezhangaocha+=s[i];
            }
        }
        KLog.e(TAG,"=========countFanceGaocha=========");
        return fanceCezhangaocha;
    }


    public void initData(List<CezhanData> mCezhanData){
        int size = mCezhanData.size();
        float[] s=new float[size];
        for(int i=0;i<size;i++){
            cezhanData=mCezhanData.get(i);
            String qianshi = cezhanData.getQianshi();
            b = qianshi.startsWith("jd");
            shuizhunxianData = new ShuizhunxianData();
//            ceduanchangdu= (FormulaUtils.countCezhanchangdu(cezhanData));

            try{
                //往测
                if(mCezhanData.size()%2==0){
                    KLog.e(TAG,"----------------------------------");
                    KLog.e(TAG,"MeasureDirection=:"+cezhanData.getMeasureDirection());
                    KLog.e(TAG,"b=:"+b);
                    if((cezhanData.getMeasureDirection()).equals(Constants.wangce)) {
                        //水准路线测段总长度
                        ceDuanChangdu=(FormulaUtils.countCeduanchangdu(cezhanData));
                        KLog.e(TAG,"ceDuanChangdu=:"+ceDuanChangdu);
                        //往测的测量高差
//                        float cezhangaocha = Float.parseFloat(cezhanData.getCezhangaocha());
//                        s[i]=cezhangaocha;
//                        wangcecezhangaocha+=cezhangaocha;
                        KLog.e(TAG,"wangcecezhangaocha=:"+wangcecezhangaocha);
                        //往测实际高差和反侧实际高差应当是水准路线两个基点的高程差
                        float wangceshijigaocha=10;
                        wangcecezhangaocha= countWangceGaocha();
                        KLog.e(TAG,"wangcecezhangaocha======:"+wangcecezhangaocha);
                        float wangcegaochabihecha = FormulaUtils.gaochabihecha(wangcecezhangaocha, wangceshijigaocha);
                        //如果往测的高差闭合差小于4*测段总长度开方，那么这次测量是合格的
                        if (FormulaUtils.isQualified(wangcegaochabihecha,ceDuanChangdu)) {
                            KLog.e(TAG,"-------wangcegaochabihecha--------");
                            dopingcha(ceDuanChangdu, cezhanData,wangcegaochabihecha);
                            KLog.e(TAG,"-------------dopingcha11111111-----------------");
                        } else {
                            ToastUtils.showToast(mContext, "本次测量不合格，请重测");
                        }
                    //返测
                    }else if ((cezhanData.getMeasureDirection()).equals(Constants.fance)) {
                        KLog.e(TAG,"=========================================");
//                        float fancezhangaocha = Float.parseFloat(cezhanData.getCezhangaocha());
//                        s[i]=fancezhangaocha;
//                        fanceCezhangaocha+=fancezhangaocha;
                        float fanceshijigaocha=10;
                        KLog.e(TAG,"fancezhangaocha=:"+fanceCezhangaocha);
                        fanceCezhangaocha= countFanceGaocha();
                        KLog.e(TAG,"fancezhangaocha======:"+fanceCezhangaocha);
                        float fancegaochabihecha = FormulaUtils.gaochabihecha(fanceCezhangaocha, fanceshijigaocha);
                        //如果高差闭合差小于4*测段总长度开方，那么这次测量是合格的
                        if (FormulaUtils.isQualified(fancegaochabihecha,ceDuanChangdu)) {
                            dopingcha(ceDuanChangdu, cezhanData,fancegaochabihecha);
                            KLog.e(TAG,"-------------dopingcha22222222-----------------");
                        } else {
                            ToastUtils.showToast(mContext, "本次测量不合格，请重测");
                        }
                    }

                }else{
                    ToastUtils.showToast(mContext,"测站数不是偶数站，无法进行平差");
                }

            }catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 以及做平差操作
     * @param ceduanzongchangdu       测段总长度
     */
    public void dopingcha(float ceduanzongchangdu,CezhanData mCezhanData,float gaochabihecha){
        RTData resultData=new RTData();
            //----------进行平差操作---------
//        for(int i=0;i<mCezhanDataList.size();i++){
            KLog.e(TAG, "===========for--44444444444444444444=============");
//            CezhanData mCezhanData = mCezhanDataList.get(i);
            KLog.e(TAG,"----------111111111111111111-------------");
            //计算往测
            //每km的改正数
            float gaizhengshu=-gaochabihecha/ceduanzongchangdu;
            KLog.e(TAG,"gaizhengshu=:"+gaizhengshu);
            float qianshiju = Float.parseFloat(mCezhanData.getB1hd() + mCezhanData.getB2hd());
            float houshiju = Float.parseFloat(mCezhanData.getF1hd() + mCezhanData.getF2hd());
            KLog.e(TAG,"----------222222222222222-------------");
            //各测段的长度
            float everceduanchanagdu =(qianshiju+houshiju)/2;
            KLog.e(TAG,"everceduanchanagdu=:"+everceduanchanagdu);
            //得到每个测段的改正数
            float v= (float) Math.round(everceduanchanagdu*gaizhengshu);
            KLog.e(TAG,"v=:"+v);
            //得到各测段改正后的高差
            KLog.e(TAG,"gaocha=:"+mCezhanData.getCezhangaocha());
            float xiugaihougaocha=(v+Float.parseFloat(mCezhanData.getCezhangaocha()));
            KLog.e(TAG,"xiugaihougaocha=:"+xiugaihougaocha);
            //得到各测段改正后的高程
//            xiugaihougaocheng=Float.parseFloat(cezhanData.getGaochengzhi())+xiugaihougaocha;
//            KLog.e(TAG,"xiugaihougaocheng=:"+xiugaihougaocheng);
            resultData.setCedianid(mCezhanData.getHoushi());
            resultData.setCedianzhuangtai(mCezhanData.getObserveType());
            resultData.setGuanceshijian(mCezhanData.getB1time());
            resultData.setXiuzhengliang(v+"");
            resultData.setYuanshibiaogao(xiugaihougaocha+"");
//            resultData.setBiaogao(xiugaihougaocheng+"");
            resultData.setBiaogao(11+"");
            resultData.save();
            KLog.e(TAG,"===============dopingcha33333333333333=============");

        }
//    }

}
