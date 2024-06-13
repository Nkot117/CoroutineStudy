package org.example.memo.`5_CoroutinePractice`

import kotlinx.coroutines.*

fun main() {
    // Coroutineの実行時間を制限する
    // 1秒で終わる
    println("withTimeout")
    val job = Dispatchers.Default + SupervisorJob()
    val timeOutCoroutine = CoroutineScope(job)
    timeOutCoroutine.launch {
        // 制限時間を1秒に指定
        withTimeout(1000) {
            try {
                timeOutProcess()
            } catch (e: TimeoutCancellationException) {
                // TimeoutCancellationExceptionが発生した場合の処理
                println("Timeout")
            }
        }
    }

    Thread.sleep(2000)
    println("--------------------")

    timeOutCoroutine.launch {
        // 制限時間を1秒に指定
        // withTimeoutOrNullはTimeoutCancellationExceptionが発生しない
        // その代わりにタイムアウトした時の動作を指定できる
        withTimeoutOrNull(1000) {
            timeOutProcess()
        } ?: println("TimeoutOrNull")
    }

    Thread.sleep(2000)
    println("--------------------")

    // リトライ処理
    println("retryOrNull")
    val retryCoroutine = CoroutineScope(job)
    var count = 0
    retryCoroutine.launch {
        retryOrNull(3, 1000) {
            count++
            if (count < 3) {
                throw Exception("Failed")
            }
            println("Success")
        } ?: println("Failed")
    }

    Thread.sleep(10000)
    println("--------------------")

    // 並列処理
    println("bothProcess")
    val bothCoroutine = CoroutineScope(job)
    bothCoroutine.launch {
        val (result1, result2) = bothProcess()
        println("$result1 $result2")
    }

    Thread.sleep(3000)
    println("--------------------")

    // キャンセル処理
    println("cancel")
    val cancelCoroutine = CoroutineScope(job)
    val cancelJob = cancelCoroutine.launch {
        try {
            repeat(100) {
                println("Hello")
                // キャンセルを検知する
                ensureActive()
            }
        } catch (e: CancellationException) {
            println("Cancel")
        }
    }

    cancelJob.cancel()
    Thread.sleep(3000)
    println("--------------------")
}

suspend fun timeOutProcess() {
    println("Start")
    delay(3000)
    println("End")
}

suspend fun <T> retryOrNull(
    retries: Int,
    intervalMills: Long,
    block: suspend () -> T
): T? {
    repeat(retries) {
        try {
            return block()
        } catch (e: CancellationException) {
            // キャンセル時は再スローする
            throw e
        } catch (e: Throwable) {
            println(e.message)
            delay(intervalMills)
        }
    }
    return null
}

suspend fun bothProcess(): Pair<String, String> {
    return coroutineScope {
        val deferred1 = async {
            delay(1000)
            "Hello"
        }
        val deferred2 = async {
            delay(1000)
            "World"
        }
        deferred1.await() to deferred2.await()
    }
}