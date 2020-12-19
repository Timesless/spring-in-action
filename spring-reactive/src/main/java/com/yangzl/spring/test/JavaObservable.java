package com.yangzl.spring.test;

import java.util.Observable;

/**
 * @author yangzl
 * @date 2020/12/18 22:12
 * @desc Java8及之前 实现响应式编程
 * 
 *      观察者模式
 *          Observer
 *          Observable
 */

public class JavaObservable extends Observable {

    public static void main(String[] args) {
        final JavaObservable observer = new JavaObservable();
        // Observer 是函数式接口
        observer.addObserver((obs, arg) -> {
            System.out.println("被监测对象发生变化");
            System.out.println("1 触发改变");
        });
        observer.addObserver((obs, arg) -> {
            System.out.println("被监测对象发生变化");
            System.out.println("2 触发改变");
        });
        
        // 手动调用改变
        observer.setChanged();
        // 通知所有观察者
        observer.notifyObservers();
    }

}
