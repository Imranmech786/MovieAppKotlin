package com.imran.myapplication.utils

class StateData<T> {

    var status: DataStatus

    var data: T? = null

    var error: Throwable? = null

    init {
        this.status = DataStatus.CREATED
        this.data = null
        this.error = null
    }

    fun loading(): StateData<T> {
        this.status = DataStatus.LOADING
        this.data = null
        this.error = null
        return this
    }

    fun success(data: T): StateData<T> {
        this.status = DataStatus.SUCCESS
        this.data = data
        this.error = null
        return this
    }

    fun error(error: Throwable): StateData<T> {
        this.status = DataStatus.ERROR
        this.data = null
        this.error = error
        return this
    }

    fun complete(): StateData<T> {
        this.status = DataStatus.COMPLETE
        return this
    }

    enum class DataStatus {
        CREATED,
        SUCCESS,
        ERROR,
        LOADING,
        COMPLETE
    }
}
