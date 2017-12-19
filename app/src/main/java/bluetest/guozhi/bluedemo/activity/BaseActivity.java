package bluetest.guozhi.bluedemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import bluetest.guozhi.bluedemo.presenter.BasePresenter;
import butterknife.ButterKnife;

/**
 * Created by wangcheng on 2016-12-7.
 */

public abstract class BaseActivity<T extends BasePresenter> extends Activity   {
    T presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setPresenter();
    }

    public abstract void initView();


    public abstract int getLayout();

    public abstract void setPresenter();


    @Override
    protected void onDestroy() {
        if(presenter!=null){
            presenter.unSubscription();
        }
        super.onDestroy();
    }
}
