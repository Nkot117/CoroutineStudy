package org.example.memo.`4_エラーハンドリング`

import kotlinx.coroutines.*

fun main() {
    // try-catch
    println("try-catch")
    val context = Job()
    val coroutineScope = CoroutineScope(context)
    coroutineScope.launch {
        // Coroutineの内部で例外が発生した場合、try-catchでキャッチすることができる
        try {
            println("start")
            throw Exception("error")
        } catch (e: Exception) {
            println("catch")
            println(e)
        }
    }

    Thread.sleep(1000)
    println("--------------------")

    // CoroutineExceptionHandler
    println("CoroutineExceptionHandler")
    val exceptionHandler = CoroutineExceptionHandler { _, e ->
        println("$e が発生しました")
    }

    val job = Job() + exceptionHandler
    val exceptionHandlerCoroutineScope = CoroutineScope(job)
    exceptionHandlerCoroutineScope.launch {
        println("start")
        delay(1000)
        throw Exception("error")
    }

    exceptionHandlerCoroutineScope.launch {
        println("start2")
        delay(2000)
        println("end2")
    }

    Thread.sleep(3000)

    // SupervisorJob
    println("SupervisorJob")
    val supervisorJob = SupervisorJob()
    val supervisorCoroutineScope = CoroutineScope(supervisorJob)
    supervisorCoroutineScope.launch {
        // このCoroutineが例外を発生させても、他のCoroutineに影響を与えない
        try {
            println("start")
            throw Exception("error")
        } catch (e: Exception) {
            println("catch")
            println(e)
        }
    }

    supervisorCoroutineScope.launch {
        println("start2")
        delay(2000)
        println("end2")
    }

    Thread.sleep(3000)
    println("--------------------")
}