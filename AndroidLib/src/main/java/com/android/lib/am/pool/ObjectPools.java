package com.android.lib.am.pool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class ObjectPools {

    private interface Poolable<T> {
        /**
         * 从对象池中取一个对象
         */
        @Nullable
        T acquire();

        /**
         * 将一个对象释放到对象池中
         */
        boolean release(@NonNull T var);
    }

    public static class SimplePoolImpl<T> implements ObjectPools.Poolable<T> {

        private final Object[] mArrayPool;
        private int mPoolSize;

        public SimplePoolImpl(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool sie must be >0");
            } else {
                this.mArrayPool = new Object[maxPoolSize];
            }
        }

        @Nullable
        @Override
        public T acquire() {
            if (this.mPoolSize <= 0) {
                return null;
            }
            int lastPoolIndex = this.mPoolSize - 1;
            T instance = (T) this.mArrayPool[lastPoolIndex];
            this.mArrayPool[lastPoolIndex] = null;
            --this.mPoolSize;
            return instance;
        }

        @Override
        public boolean release(@NonNull T var) {
            if (this.isInPool(var)) {
                throw new IllegalArgumentException("Already in the pool!");
            } else if (this.mPoolSize < this.mArrayPool.length) {
                this.mArrayPool[this.mPoolSize] = var;
                ++this.mPoolSize;
                return true;
            }
            return false;
        }

        private boolean isInPool(@NonNull T var) {
            for (int i = 0; i < this.mPoolSize; ++i) {
                if (this.mArrayPool[i] == var) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class SynchronizedPool<T> extends ObjectPools.SimplePoolImpl<T> {

        private final Object mLock = new Object();

        public SynchronizedPool(int maxPoolSize) {
            super(maxPoolSize);
        }

        @Nullable
        @Override
        public T acquire() {
            synchronized (this.mLock) {
                return super.acquire();
            }
        }

        @Override
        public boolean release(@NonNull T var) {
            synchronized (this.mLock) {
                return super.release(var);
            }
        }
    }
}
