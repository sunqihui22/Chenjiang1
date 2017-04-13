package com.shtoone.chenjiang.mvp.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shtoone.chenjiang.R;
import com.shtoone.chenjiang.mvp.model.entity.db.OriginData;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class OriginalDataRVAdapter extends BaseQuickAdapter<OriginData, BaseViewHolder> {
    private static final String TAG = OriginalDataRVAdapter.class.getSimpleName();

    public OriginalDataRVAdapter() {
        super(R.layout.item_rv_measured_original_data_fragment, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, OriginData mOriginalData) {
//        DataSupport.deleteAll(OriginData.class);
        holder.setText(R.id.tv_number_item_rv_measured_original_data_fragment, mOriginalData.getShuizhunxianluid())
                .setText(R.id.tv_type_item_rv_measured_original_data_fragment, mOriginalData.getQianhoushibiaojifu())
                .setText(R.id.tv_distance_item_rv_measured_original_data_fragment, mOriginalData.getQianhoushijuli())
                .setText(R.id.tv_value_item_rv_measured_original_data_fragment, mOriginalData.getQianhoushichidushu())
                .setText(R.id.tv_date_item_rv_measured_original_data_fragment, mOriginalData.getGuancetime());
        KLog.e(TAG,"--------convert---------");
    }
    
}
