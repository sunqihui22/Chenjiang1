package com.shtoone.chenjiang.common;

import com.shtoone.chenjiang.mvp.model.entity.db.CezhanData;
import com.socks.library.KLog;


/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class FormulaUtils {
    private static final String TAG = FormulaUtils.class.getSimpleName();
    private static float wangCeBiHeCha;

    /**
     * 计算偶然中误差，用于判断测量数据是否符合精度
     * @param gaochabufuzhi   往返测的高差不符值
     * @param ceduanchangdu        测段长度
     * @param cezhanshu            测站数
     * @return  偶然中误差
     */
    public static float jingdu(float gaochabufuzhi, float ceduanchangdu,float cezhanshu){
        float m1= (gaochabufuzhi*gaochabufuzhi/ceduanchangdu/(4*(cezhanshu+1)));
        float zhongwucha= (float) Math.sqrt(m1);
        return zhongwucha;
    }

    /**
     *根据传去的参数计算往测和返测的高差闭合差
     * @param fanceGaocheng  往测或返测时得到的测量高程
     * @param qishidiangaocheng  起始基点或终止基点的实际高程
     * @return  往测或返测的高差闭合差
     */
    public static float gaochabihecha(float fanceGaocheng,float qishidiangaocheng){

        wangCeBiHeCha=fanceGaocheng-qishidiangaocheng;
        return wangCeBiHeCha;
    }

    public static Boolean isQualified(float gaochabihecha,float ceduanchangdu){
        return Math.abs(gaochabihecha) < 4 * Math.sqrt(ceduanchangdu);
    }

    public static float countCezhanchangdu(CezhanData mCezhanData){
        Formula formula = Formula.getInstance(mCezhanData);
        KLog.e("-------countCezhanchangdu--------");
        return (formula.qianshiju()+formula.houshiju());
    }

    public static float countCeduanchangdu(CezhanData mCezhanData){
        return Float.parseFloat(mCezhanData.getQianshijuhe())+Float.parseFloat(mCezhanData.getHoushijuhe());
    }
}
