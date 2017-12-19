package bluetest.guozhi.bluedemo.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by wangcheng on 2016-12-7.
 */

public class BasePresenter {

    protected CompositeSubscription mCompositeSubscription;

    protected void addSubscription(Subscription subscription){
        if (mCompositeSubscription == null){
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }
    public void unSubscription(){
        if (mCompositeSubscription != null){
            mCompositeSubscription.unsubscribe();
        }
    }
}
