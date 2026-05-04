package com.bulain.mybatis.lock;

/**
 * 锁回调接口
 *
 * @param <T> 返回值类型
 */
@FunctionalInterface
public interface LockCallback<T> {

    /**
     * 在锁保护下执行的操作
     *
     * @return 执行结果
     * @throws Exception 执行过程中抛出的异常
     */
    T doInLock() throws Exception;
}
