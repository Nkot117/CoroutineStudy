package org.example

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

//TIP コードを<b>実行</b>するには、<shortcut actionId="Run"/> を押すか
// ガターの <icon src="AllIcons.Actions.Execute"/> アイコンをクリックします。
fun main() {
    // rubBlocking　を使用したCoroutineの実行
    runBlocking {
        launch {
            println("runBlocking 1")
            delay(1000L)
            println("runBlocking 2")
        }

        launch {
            println("runBlocking 3")
        }
    }

    // CoroutineScopeは、CoroutineContextを指定するすることで作成できる
    // CoroutineContextとは、Coroutineの実行環境を指定するためのもの
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        println("EmptyCoroutineContext 1")
        delay(1000L)
        println("EmptyCoroutineContext 2")
    }
    scope.launch {
        println("EmptyCoroutineContext 3")
    }
    // EmptyCoroutineContextの場合、launchの完了を待ってくれない
    // そのため、Thread.sleepを使用して待つ
    Thread.sleep(2000L)

    val cancelScope = CoroutineScope(EmptyCoroutineContext)
    cancelScope.launch {
        println("cancelScope 1")
        delay(1000L)
        // 後続でcancelされるため、この処理は実行されない
        println("cancelScope 2")
    }
    cancelScope.launch {
        println("cancelScope 3")
    }
    cancelScope.cancel()
    // EmptyCoroutineContextの場合、launchの完了を待ってくれない
    // そのため、Thread.sleepを使用して待つ
    Thread.sleep(2000L)

    // Coroutineのキャンセルは、子Coroutine、孫Coroutineに伝播する
    val cancelScope2 = CoroutineScope(EmptyCoroutineContext)
    cancelScope2.launch {
        println("cancelScope2 1")
        delay(1000L)
        launch {
            // 親Coroutineがキャンセルされると、子Coroutineもキャンセルされる
            println("cancelScope2 2")
            delay(1000L)
            println("cancelScope2 3")
        }
    }

    cancelScope2.cancel()

    // Jobを使用して、Coroutineをハンドリンできる
    runBlocking {
        val job1 = launch {
            delay(100L)
            println("completed: job1")
        }
        val job2 = launch {
            delay(100L)
            println("completed: job2")
        }
        job1.join()
        job2.join()
        println("completed: all")
    }

    val jonScope = CoroutineScope(EmptyCoroutineContext)
    val job3 = jonScope.launch {
        delay(400L)
        println("completed: job3")
    }
    val job4 = jonScope.launch {
        delay(500L)
        println("completed: job4")
    }
    jonScope.launch {
        job3.join()
        job4.join()
        println("completed: all")
    }
    Thread.sleep(1000L)

    // asyncを使用して、非同期処理を行う
    runBlocking {
        val deferred1 = async {
            delay(100L)
            "completed: deferred1"
        }
        val deferred2 = async {
            delay(100L)
            "completed: deferred2"
        }
        println(deferred1.await() + " " + deferred2.await())
        println("completed: all")
    }

    val suspendFunScope = CoroutineScope(EmptyCoroutineContext)
    suspendFunScope.launch {
        println("Start")
        delayTask()
        println("End")
    }

    Thread.sleep(2000L)
}

private suspend fun delayTask() {
    delay(1000L)
    println("Delayed Task")
}