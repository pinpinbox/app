import android.util.Log;

import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;

import org.junit.Test;

import java.util.logging.Handler;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by vmage on 2018/4/30.
 */

public class UnitTestSample {

    /**
     * @Test 被注解的方法是一个测试方法。
     * @BeforeClass 被注解的（静态）
     * 方法将在当前类中的所有 @Test 方法前执行一次。
     * @Before 被注解的方法将在当前类中的每个 @Test 方法前执行。
     * @After 被注解的方法将在当前类中的每个 @Test 方法后执行。
     * @AfterClass 被注解的（静态）
     * 方法将在当前类中的所有 @Test 方法后执行一次。
     * @Ignore 被注解的方法不会执行（将被跳过），但会报告为已执行。
     */


    private String TAG = UnitTestSample.class.getSimpleName();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Test
    public void doSomething() {

//        test1();

        test2();

    }


    private void test1() {


        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onComplete();
//                e.onError(new NullPointerException());
            }
        });


        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(String s) {

                System.out.println("onNext => " + s);

            }

            @Override
            public void onError(Throwable e) {

                System.out.println("onError");

            }

            @Override
            public void onComplete() {

                System.out.println("onComplete");

            }
        });


        disposables.dispose();


    }


    private void test2() {


        final ItemUser itemUser = new ItemUser();

        Observable.create(new ObservableOnSubscribe<ItemUser>() {
            @Override
            public void subscribe(final ObservableEmitter<ItemUser> emitter) throws Exception {

                itemUser.setName("abcde");

                emitter.onNext(itemUser);

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(ItemUser itemUser) {

                        System.out.println("onNext => " + itemUser.getName());

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                })
        ;



    }


}
