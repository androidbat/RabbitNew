package com.m.rabbit.main.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m.rabbit.R;
import com.m.rabbit.constant.TestData;
import com.m.rabbit.main.DetailActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;

    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final View view = holder.mView;
        holder.faceIv.setImageResource(TestData.imgIds[position%TestData.imgIds.length]);
        holder.titleTv.setText(TestData.titles[position%TestData.titles.length]);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 20, 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        DetailActivity.position = position;
                        DetailActivity.start(mContext);

//                        Intent i = new Intent(mContext, DetailActivity.class);
//                        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext, Pair.create((View)holder.faceIv, "face"));
//                        mContext.startActivity(i, transitionActivityOptions.toBundle());
                    }
                });
                animator.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        @InjectView(R.id.face_iv)
        ImageView faceIv;
        @InjectView(R.id.title_tv)
        TextView titleTv;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.inject(this, view);
        }
    }
}
