package com.wujing.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cchip.eq.TextCustomSeekBar;
import com.wujing.view.R;
import com.wujing.view.utils.SharePreferecnceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EqListviewAdapter extends RecyclerView.Adapter<EqListviewAdapter.EqListHolder> {

    //    private List<String> specailList;
    private LayoutInflater mInflater;
    private Context mContext = null;
    private OnTextCustomSeekbarChangeListenner onTextCustomSeekbarChangeListenner;
    private int selectposition;
    private List<Integer> mProgresses = new ArrayList<>();


    private String[] text = new String[]{"32", "64", "125", "250", "500", "1K", "2K", "4K", "8K", "16K"};
    private int[] Popular = new int[]{6, 5, -3, -2, 5, 4, -4, -3, 6, 4};         //流行
    private int[] Dance = new int[]{4, 3, -4, -6, 0, 0, 3, 4, 4, 5};         //舞曲
    private int[] Rock = new int[]{7, 6, 2, 1, -3, -4, 2, 1, 4, 5};          //摇滚
    private int[] Cassical = new int[]{6, 7, 1, 2, -1, 1, -4, -6, -7, -8};        //古典
    private int[] Vocal = new int[]{-5, -6, -4, -3, 3, 4, 5, 4, -3, -3};         //人声
    private int[] Bass = new int[]{8, 7, 5, 4, 0, 0, 0, 0, 0, 0};          //低音
    private int[] Soft = new int[]{-5, -5, -4, -4, 3, 2, 4, 4, 0, 0};          //柔和
    private int[] Custom = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};         //自定义
    private int[] EQreset = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};         //重置
    private int[][] EQ = new int[][]{Popular, Dance, Rock, Cassical, Vocal, Bass, Soft, Custom};

    public int getMode() {
        return mode;
    }

    private int mode;

    public EqListviewAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setList(int... list) {
        mProgresses.clear();
        for (int i : list) {
            mProgresses.add(i);
        }
        notifyDataSetChanged();
    }


    public OnItemClickListener mOnItemClickListener;

    public void setMode(int mode) {
        this.mode = mode;
        setList(EQ[mode]);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickListener = mOnItemClickLitener;
    }

    @Override
    public EqListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EqListHolder holder = null;
        View view = mInflater.inflate(R.layout.item_speedhour_layout, parent, false);
        holder = new EqListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final EqListHolder holder, int position) {
        String bean = text[position];
        int progress = mProgresses.get(position);
        if (!TextUtils.isEmpty(bean)) {
            holder.mTextCustomSeekbar.setTag(position);
            holder.mTextCustomSeekbar.getFrequency().setText(bean);
            holder.mTextCustomSeekbar.setProgress(progress);
            holder.mTextCustomSeekbar.setOnTextCustomSeekbarChangeListenner(new TextCustomSeekBar.OnTextCustomSeekbarChangeListenner() {
                @Override
                public void onTouchResponse(int progress) {
                    if (onTextCustomSeekbarChangeListenner != null) {
                        onTextCustomSeekbarChangeListenner.onTouchResponse(holder.mTextCustomSeekbar, progress);
                    }
                }
            });
        }
    }

    public interface OnTextCustomSeekbarChangeListenner {
        void onTouchResponse(View view, int progress);
    }

    public void setOnTextCustomSeekbarChangeListenner(OnTextCustomSeekbarChangeListenner listener) {
        onTextCustomSeekbarChangeListenner = listener;
    }

    @Override
    public int getItemCount() {
        return mProgresses != null ? mProgresses.size() : null;
//        return text.length;
    }


    class EqListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_custom_seekbar)
        TextCustomSeekBar mTextCustomSeekbar;

        public EqListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }

    }

//    public void remove(int position) {
//        mProgresses.remove(position);
//        notifyItemRemoved(position);
//    }
//    //RecyclerView列表进行批量UI数据更新
//                    mRecyclerViewAdapter.notifyItemRangeInserted(0,4);
//    // scrollToPosition（0）作用是把列表移动到顶端
//                    mRecyclerView.scrollToPosition(0)
//
//
//                            case R.id.menu_del:
//            //删除模拟数据第一项
//            mTitle.remove(0);
//    //RecyclerView 列表进行UI数据更新
//                    mRecyclerViewAdapter.notifyItemRemoved(0);
//                    break;
//
//                case R.id.menu_move:
//            //列表中第二项移到第三项 进行UI数据更新
//            mRecyclerViewAdapter.notifyItemMoved(1,2);
//                    break;

//    public void add(int position) {
//        mProgresses.add(position);
//        notifyItemInserted(position);
//    }

//    public void addChangeIten(int progress, int position) {
//        mProgresses.remove(position);
//        mProgresses.add(position, progress);
//        notifyItemInserted(position);
//    }

    //在销毁之前要保存自定义时调用
    public void onShow(boolean isShow) {

        if (isShow) {
//            if (SppManager.getInstance().isConnected()) {
//                return;
//            }
            setEdit(SharePreferecnceUtil.getSpEq());
        } else {
            SharePreferecnceUtil.setSpEq(EQ[EQ.length - 1]);
        }
    }

    //重置
    public void reset() {
        setEdit(EQreset);
        setMode(EQ.length - 1);
    }

    public void setEdit(int eqMode) {
        EQ[EQ.length - 1] = EQ[eqMode];
    }

    public void setEditItem( int id, int p) {
        if (mode!=EQ.length - 1) {
            int[] ints = EQ[mode];
            for (int i = 0; i < ints.length; i++) {
                EQ[EQ.length - 1][i]=ints[i] ;
            }
        }
        EQ[EQ.length - 1][id] = p;
    }

    public void setEdit(int[] EQreset) {
//        for (int i = 0; i < EQreset.length; i++) {
        EQ[EQ.length - 1] = EQreset;
//        }
    }

    public int[] getTenEq() {
        int[] integers = EQ[EQ.length - 1];
        return integers;
    }

}
