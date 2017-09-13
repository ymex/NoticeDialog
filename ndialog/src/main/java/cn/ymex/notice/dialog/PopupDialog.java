package cn.ymex.notice.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.TypedArrayUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * popupwindow
 */

public class PopupDialog extends PopupWindow {
    // TODO: 2017/9/13 增加对话框管理功能

    private ViewGroup mRootView;
    private View contextView;
    private Context mContext;
    private Animation inAnimation;
    private Animation outAnimation;


    private PopupDialog(Context context) {
        this(context, R.anim.pulse_modal_in);
    }


    private PopupDialog(Context context, @AnimRes int anim) {
        super(context);
        this.init(context, anim);
    }


    private void init(Context context,@AnimRes int anim) {
        this.mContext = context;
        this.mRootView = new FrameLayout(context);

        this.setContentView(this.mRootView);
        this.setClippingEnabled(false);
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        try {
            this.inAnimation = AnimationUtils.loadAnimation(context, anim);
        } catch (Exception e) {
            this.inAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_modal_in);
        }
        this.outAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse_modal_out);
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOutsideTouchable()) {
                    dismiss();
                }
            }
        });
    }

    public PopupDialog clippingEnabled(boolean able) {
        this.setClippingEnabled(able);
        return this;
    }

    /**
     * 添加布局
     *
     * @param view 布局
     */
    public void addView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("PopupDialog content createView cont allow null!");
        }
        this.contextView = view;
        view.setVisibility(View.VISIBLE);
        view.setClickable(true);
        if (this.mRootView.getChildCount() != 1) {
            this.mRootView.removeAllViews();
        }
        this.mRootView.addView(view);
    }

    /**
     * 移除布局
     */
    public void removeCurrentView() {
        if (this.contextView != null) {
            this.mRootView.removeView(this.contextView);
        }
    }

    /**
     * 当前布局
     *
     * @return 当前布局
     */
    public View getCurrentView() {
        return this.contextView;
    }

    /**
     * 获取组件
     *
     * @param id
     * @param <T>
     * @return 查找不到为 null
     */
    public <T extends View> T find(@IdRes int id) {
        return (T) contextView.findViewById(id);
    }


    public static PopupDialog create(Context context) {
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("context is an invalid type!");
        }
        return new PopupDialog(context);
    }

    public static PopupDialog create(Fragment fragment) {
        return create(fragment.getActivity());
    }

    public static PopupDialog create(android.app.Fragment fragment) {
        return create(fragment.getActivity());
    }


    /**
     * 添加布局
     *
     * @param layout 布局
     * @return
     */
    public PopupDialog view(@LayoutRes int layout) {
        return view(layout, null);
    }

    /**
     * 添加布局
     *
     * @param layout             布局
     * @param onBindViewListener 添加布局后回调
     * @return PopupDialog
     */
    public PopupDialog view(@LayoutRes int layout, OnBindViewListener onBindViewListener) {
        return view(LayoutInflater.from(this.mContext).inflate(layout, this.mRootView, false), onBindViewListener);
    }

    /**
     *
     * @param controlable
     * @return
     */
    public PopupDialog controller(DailogControlable controlable) {
        if (controlable == null) {
            return this;
        }
        return view(controlable.createView(this.mContext,this.mRootView), controlable.bindView(this));
    }


    public PopupDialog view(View view) {
        return view(view, null);
    }


    public PopupDialog view(View view, OnBindViewListener onBindViewListener) {
        this.addView(view);
        setBindViewListener(onBindViewListener);
        return this;
    }

    public PopupDialog width(int w) {
        this.setWidth(w);
        return this;
    }

    public PopupDialog height(int h) {
        this.setHeight(h);
        return this;
    }

    public PopupDialog animationIn(@AnimRes int anim) {
        //this.setAnimationStyle(anim);
        this.inAnimation = AnimationUtils.loadAnimation(this.mContext,anim);
        return this;
    }

    public PopupDialog animationOut(@AnimRes int anim) {
        //this.setAnimationStyle(anim);
        this.outAnimation = AnimationUtils.loadAnimation(this.mContext,anim);
        return this;
    }

    public PopupDialog backgroundDrawable(Drawable drawable) {
        this.setBackgroundDrawable(drawable);
        return this;
    }

    public PopupDialog outsideTouchable(boolean touchable) {
        this.setOutsideTouchable(touchable);
        return this;
    }

    public PopupDialog focusable(boolean focus) {
        this.setFocusable(focus);
        return this;
    }


    public void show() {
        this.showAtLocation(mRootView, ViewGroup.LayoutParams.MATCH_PARENT, 0, 0);
        this.contextView.startAnimation(inAnimation);
    }


    @Override
    public void dismiss() {

        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PopupDialog.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.contextView.startAnimation(outAnimation);
    }

    /**
     * 点击事件（默认关闭弹窗）
     *
     * @param id       组件id
     * @param listener 监听
     * @return this
     */
    public PopupDialog click(@IdRes int id, View.OnClickListener listener) {
        return click(id, listener, true);
    }

    /**
     * 点击事件
     *
     * @param id       组件id
     * @param listener 监听
     * @param dismiss  是否关闭弹窗
     * @return this
     */
    public PopupDialog click(@IdRes int id, final View.OnClickListener listener, final boolean dismiss) {

        find(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
                if (dismiss) {
                    dismiss();
                }
            }

        });

        return this;
    }

    /**
     * 销毁popupdialog的一切
     */
    public void destroy() {
        removeCurrentView();
        mRootView.removeAllViews();
        contextView.destroyDrawingCache();
        contextView = null;
        mRootView = null;
        mContext = null;
    }

    private void setBindViewListener(OnBindViewListener onBindViewListener) {
        if (onBindViewListener != null) {
            onBindViewListener.onCreated(contextView);
        }
    }

    public interface OnBindViewListener {
        void onCreated(View layout);
    }


    public interface DailogControlable {
        View createView(Context cotext,ViewGroup parent);

        PopupDialog.OnBindViewListener bindView(PopupDialog dialog);
    }
}

