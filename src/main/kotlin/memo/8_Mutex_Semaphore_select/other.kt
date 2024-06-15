package org.example.memo.`8_Mutex_Semaphore_select`

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit


var counter = 0
private val mutex = Mutex()

suspend fun increment() {
    mutex.withLock {
        // ここは一つずつしか実行されない
        val current = counter
        delay(2000)
        counter = current + 1
    }
}

val semaphore = Semaphore(2)
suspend fun semaphoreProcess(value: Int) {
    semaphore.withPermit {
        // ここは2つずつしか実行されない
        // start、start
        // end、end
        // のように実行される
        println("Start $value")
        delay(2000)
        println("End $value")
    }
}

fun main() {
    // Mutexを使った並行処理
    println("Mutexを使った並行処理")
    val coroutineContext = Dispatchers.Default + Job()
    val coroutineScope = CoroutineScope(coroutineContext)

    coroutineScope.launch {
        increment()
    }

    coroutineScope.launch {
        increment()
    }

    Thread.sleep(5000)
    println(counter)
    println("--------------------")

    // Mutexのキャンセル
    println("Mutexのキャンセル")
    val job = coroutineScope.launch {
        // これはキャンセルされる
        increment()
    }

    coroutineScope.launch {
        increment()
    }

    job.cancel()

    Thread.sleep(5000)
    println(counter)
    println("--------------------")

    // Semaphoreを使った並行処理
    println("Semaphoreを使った並行処理")
    val jobs = List(5) {
        coroutineScope.launch {
            semaphoreProcess(it)
        }
    }

    runBlocking {
        jobs.forEach { it.join() }
    }
    println("--------------------")

    // select
    println("select")
    coroutineScope.launch {
       val deferred1 = async {
           delay(2000)
           100
       }
        val deferred2 = async {
            delay(1000)
            200
        }

        val result = select<Int> {
            // 一番早く終わったものが選ばれる
            deferred1.onAwait {
                it
            }
            deferred2.onAwait {
                it
            }
        }

        println(result)
    }

    Thread.sleep(5000)
    println("--------------------")
}



