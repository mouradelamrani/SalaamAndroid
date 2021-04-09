package com.mouradelamrani.salaam.extensions

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.mouradelamrani.salaam.utils.livedata.Event
import com.mouradelamrani.salaam.utils.livedata.EventObserver


/** Uses `Transformations.map` on a LiveData */
fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}

/** Uses `Transformations.switchMap` on a LiveData */
fun <X, Y> LiveData<X>.switchMap(body: (X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this, body)
}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNew(newValue: T) {
    if (this.value != newValue) postValue(newValue)
}

/**
 * Filters items emitted by LiveData
 * Implementation based on `Transformations.map`
 */
@MainThread
fun <X> LiveData<X>.filter(predicate: (X) -> Boolean): LiveData<X> {
    val result = MediatorLiveData<X>()
    result.addSource(this) { x -> if (predicate(x)) result.value = x }
    return result
}

/**
 * Calls as `LiveData.mapNotNull` with assertion operator (!!)
 */
@MainThread
fun <X> LiveData<X?>.filterNotNull(): LiveData<X> = this.mapNotNull { it!! }

/**
 * Same as `Transformations.map` but filter null values
 */
@MainThread
fun <X, Y> LiveData<X?>.mapNotNull(mapFunction: (X) -> Y): LiveData<Y> {
    val result = MediatorLiveData<Y>()
    result.addSource(this) { x ->
        if (x != null) result.value = mapFunction(x)
    }
    return result
}

/**
 * Emits only first value from source LiveData
 */
@MainThread
fun <X> LiveData<X>.first(): LiveData<X> {
    val mediator = MediatorLiveData<X>()
    mediator.addSource(this) { x ->
        mediator.removeSource(this)
        mediator.value = x
    }
    return mediator
}

/**
 * Emits only first not null value from source LiveData
 */
@MainThread
fun <X> LiveData<X?>.firstNotNull(): LiveData<X> {
    val mediator = MediatorLiveData<X>()
    mediator.addSource(this) { x ->
        if (x != null) {
            mediator.removeSource(this)
            mediator.value = x
        }
    }
    return mediator
}

/**
 * Emits pair of two latest values of source LiveData.
 */
fun <X> LiveData<X>.withPrevious(): LiveData<Pair<X?, X>> {
    val mediator = MediatorLiveData<Pair<X?, X>>()
    mediator.addSource(this, object : Observer<X> {
        var prev: X? = null
        override fun onChanged(x: X) {
            mediator.value = Pair(prev, x)
            prev = x
        }
    })
    return mediator
}


fun <T> MutableLiveData<Event<T>>.postEvent(t: T) = this.postValue(Event(t))

fun <T> MutableLiveData<Event<T>>.sendEvent(t: T) {
    this.value = Event(t)
}

fun MutableLiveData<Event<Unit>>.sendEvent() {
    this.value = Event(Unit)
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, onChanged: (T) -> Unit) {
    this.observe(owner, Observer(onChanged))
}

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, onEvent: (T) -> Unit) {
    this.observe(owner, EventObserver(onEvent))
}

fun <T> LiveData<T>.getDistinct(): LiveData<T> = Transformations.distinctUntilChanged(this)

//fun <T> Flowable<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this)
//
//fun <T> Observable<T>.toLiveData(): LiveData<T> = this.toFlowable(BackpressureStrategy.LATEST).toLiveData()

fun <T> LiveData<T>.mergeWith(other: LiveData<T>): LiveData<T> {
    val mediator = MediatorLiveData<T>()
    mediator.addSource(this) { mediator.value = it }
    mediator.addSource(other) { mediator.value = it }
    return mediator
}

fun <X, Y> LiveData<X>.withLatestFrom(other: LiveData<Y>): LiveData<Pair<X, Y>> =
    object : MediatorLiveData<Pair<X, Y>>() {
        private var x: X? = null
        private var y: Y? = null

        init {
            addSource(this@withLatestFrom) { _x ->
                x = _x
                notifySourceChanged()
            }
            addSource(other) { _y ->
                y = _y
                notifySourceChanged()
            }
        }

        private fun notifySourceChanged() {
            val _x = x ?: return
            val _y = y ?: return
            this.value = Pair(_x, _y)
        }
    }