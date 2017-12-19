package bluetest.guozhi.bluedemo.utils;


import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wangcheng on 2016-12-7.
 */

public class RxUtil {

    public static <T> Observable.Transformer<T, T> rxSchedulerHelper() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 可自定义线程
     */
    public static <T> Observable.Transformer<T, T> rxSchedulerHelper(final Scheduler scheduler) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(scheduler)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * create a sync observable
     *
     * @param callable
     * @param <R>
     * @return
     */
    public static <R> Observable<R> wrap(Callable<R> callable) {
        return fromCallable(callable).subscribeOn(Schedulers.io());
    }

    public static <T> Observable<T> fromCallable(final Callable<T> callable) {
        return Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                T result;
                try {
                    result = callable.call();
                } catch (Exception e) {
                    return Observable.error(e);
                }
                return Observable.just(result);
            }
        });
    }

    public static Observable<Long> startTimer(int time, TimeUnit unit) {
        return Observable.timer(time, unit);
    }

    public static Observable<Integer> countdown(int time) {
        return Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return  increaseTime.intValue();
                    }
                })
                .take(time);

    }
}
