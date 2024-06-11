package org.example.memo.`3_CoroutineContext`

import kotlinx.coroutines.*

fun main() {
    // IOスレッドを指定したCoroutineContext
    println("IOスレッドを指定したCoroutineContext")
    val context = Dispatchers.IO
    val scope = CoroutineScope(context)
    scope.launch {
        println("Dispatchers.IO 1")
        delay(1000L)
        println("Dispatchers.IO 2")
    }
    Thread.sleep(2000L)
    println("-------------------------")

    // Jobを指定したCoroutineContext
    println("Jobを指定したCoroutineContext")
    val jobContext = Job()
    val jobScope = CoroutineScope(jobContext)
    jobScope.launch {
        println("Job 1")
        delay(1000L)
        println("Job 2")
    }
    Thread.sleep(2000L)
    println("-------------------------")

    // jobを使う場合、jobを使用しているCoroutineScopeをキャンセルすることができる
    println("jobを使う場合、jobを使用しているCoroutineScopeをキャンセルすることができる")
    val cancelJob = Job()
    val cancelScope = CoroutineScope(cancelJob)
    cancelScope.launch {
        println("Cancel 1")
        delay(1000L)
        println("Cancel 2")
    }
    cancelJob.cancel()
    Thread.sleep(2000L)
    println("-------------------------")

    // DispatcherとJobを合成したCoroutineContext
    println("DispatcherとJobを合成したCoroutineContext")
    val dispatcherJobContext = Dispatchers.IO + Job()
    val dispatcherJobScope = CoroutineScope(dispatcherJobContext)
    dispatcherJobScope.launch {
        println("Dispatchers.IO + Job 1")
        delay(1000L)
        launch {
            delay(1000L)
            println("Dispatchers.IO + Job 1-1")
        }
        println("Dispatchers.IO + Job 2")
    }
    Thread.sleep(3000L)
    println("-------------------------")

    // Dispatcherの切り替え
    println("Dispatcherの切り替え")
    val dispatcherSwitchScope = CoroutineScope(Dispatchers.IO)
    dispatcherSwitchScope.launch {
        println("Dispatchers.IO 1")
        withContext(Dispatchers.Default) {
            println("Dispatchers.Default 1")
        }
        println("Dispatchers.IO 2")
    }

    Thread.sleep(2000L)
    println("-------------------------")

    // Asyncでも同じことができる
    println("Asyncでも同じことができる")
    val asyncScope = CoroutineScope(Dispatchers.IO)
    val deferred1 = asyncScope.async {
        println("Dispatchers.IO 1")
        withContext(Dispatchers.Default) {
            println("Dispatchers.Default 1")
        }
        println("Dispatchers.IO 2")
        "Hello"
    }

    val deferred2 = asyncScope.async {
        println("Dispatchers.IO 3")
        withContext(Dispatchers.Default) {
            println("Dispatchers.Default 2")
        }
        println("Dispatchers.IO 4")
        "World"
    }

    asyncScope.launch {
        println(deferred1.await() + deferred2.await())
    }

    Thread.sleep(2000L)
    println("-------------------------")

    // CoroutineScopeに名前をつける
    println("CoroutineScopeに名前をつける")
    val namedCoroutineContext = Dispatchers.Default + CoroutineName("Test")
    val namedScope = CoroutineScope(namedCoroutineContext)
    namedScope.launch {
        println("Named 1")
        delay(1000L)
        println("Named 2")
    }

    Thread.sleep(2000L)
    println("-------------------------")
}