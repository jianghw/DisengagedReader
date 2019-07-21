/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.lifecycle

internal class FullLifecycleObserverAdapter(private val mObserver: FullLifecycleObserver) : GenericLifecycleObserver {

    @Override
    fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            ON_CREATE -> mObserver.onCreate(source)
            ON_START -> mObserver.onStart(source)
            ON_RESUME -> mObserver.onResume(source)
            ON_PAUSE -> mObserver.onPause(source)
            ON_STOP -> mObserver.onStop(source)
            ON_DESTROY -> mObserver.onDestroy(source)
            ON_ANY -> throw IllegalArgumentException("ON_ANY must not been send by anybody")
        }
    }
}
