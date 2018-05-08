import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by vmage on 2018/4/30.
 */

public class UnitTestSample {


    private String TAG = UnitTestSample.class.getSimpleName();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Test
    public void doSomething() {

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

                System.out.println("onNext");

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


    }


}
