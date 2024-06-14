package org.example.memo.`6_flow`

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() {
    println("Flow")
    val flow1 = flow {
        for (i in 1..3) {
            emit(i)
            delay(1000L)
            emit(i)
        }
    }.map {
        // emitされた値を2倍にして返す
        it * 2
    }

    val coroutineContext = Dispatchers.Default + Job()
    val coroutineScope = CoroutineScope(coroutineContext)
    coroutineScope.launch {
        flow1.collect {
            // flowのemitが呼ばれるたびに実行される
            println(it)
        }
    }

    Thread.sleep(4000L)
    println("--------------------")

    // Cold Stream
    println("Cold Stream")

    val flow2 = listOf(1, 2, 3).asFlow()
    coroutineScope.launch {
        // collectが呼ばれるたびにflowが再度実行される
        // 同じデータが複数箇所で流れる
        flow2.collect {
            println(it)
        }

        flow2.collect {
            println(it)
        }
    }

    Thread.sleep(1000L)
    println("--------------------")

    // Flowのキャンセル
    println("Flowのキャンセル")
    val cancelFlow = flow {
        var i = 0
        while (true) {
            emit(i++)
            delay(1000L)
        }
    }

    // CoroutineScopeをキャンセルすれば、中のFlowもキャンセルされる
    val job = coroutineScope.launch {
        cancelFlow.collect {
            println(it)
        }
    }

    Thread.sleep(3000L)
    job.cancel()
    println("--------------------")

    // try-catchのFlowのエラーハンドリング
    println("try-catchのFlowのエラーハンドリング")
    val errorFlow = flow {
        emit(1)
        throw RuntimeException("Error")
    }

    // Coroutineと同じように、try-catchでエラーハンドリングができる
    coroutineScope.launch {
        try {
            errorFlow.collect {
                println(it)
            }
        } catch (e: Exception) {
            println("try-catchでエラーをハンドリング")
        }
    }

    Thread.sleep(1000L)
    println("--------------------")

    // catchオペレータのFlowエラーハンドリング
    println("catchオペレータのFlowのエラーハンドリング")
    coroutineScope.launch {
        errorFlow.catch { e ->
            println("catchオペレータでエラーをハンドリング")
        }.collect {
            println(it)
        }
    }

    Thread.sleep(1000L)
    println("--------------------")

    // Flowの合成
    println("CombineのFlowの合成")
    val flow3 = flowOf(1, 2, 3)
    val flow4 = flowOf(4, 5, 6)

    // combineで2つのFlowを合成する
    val combined = combine(flow3, flow4) { a, b ->
        "$a : $b"
    }

    coroutineScope.launch {
        combined.collect {
            println(it)
        }
    }

    Thread.sleep(3000L)
    println("--------------------")

    // Flowの合成
    println("ZipのFlowの合成")
    // combineで2つのFlowを合成する
    val zip = flow3.zip(flow4) { a, b ->
        "$a : $b"
    }

    coroutineScope.launch {
        zip.collect {
            println(it)
        }
    }

    Thread.sleep(3000L)
    println("--------------------")

    // LaunchInを使ったFlowの実行
    println("LaunchInを使ったFlowの実行")
    val flow5 = flowOf(1, 2, 3)
    flow5.onEach {
        // 階層が浅くなる
        println(it)
    }.launchIn(coroutineScope)

    Thread.sleep(1000L)
    println("--------------------")

    // ShareInを使ったFlowの実行
    println("ShareInを使ったFlowの実行")
    val flow = flow {
        // ここは一度しか動かない
        delay(1000L)
        println("emit")
        emit(1)
        emit(2)
        emit(3)
    }.map {
        println("map: $it")
        it
        // shareInで複数のcollectを実行しても、emitは一度しか呼ばれない
    }.shareIn(coroutineScope, SharingStarted.Eagerly)

    coroutineScope.launch {
        flow.collect {
            println("Collecting $it")
        }
    }

    coroutineScope.launch {
        flow.collect {
            println("Collecting $it")
        }
    }

    Thread.sleep(5000L)
    println("--------------------")

    // FlowをStateInに変換
    println("FlowをStateInに変換")
    val stateInFlow = flow {
        emit(1)
        delay(1000L)
        emit(2)
        delay(1000L)
        emit(2)
        delay(1000L)
        emit(3)
        emit(4)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, 0)


    stateInFlow.onEach {
        println(it)
    }.launchIn(coroutineScope)

    Thread.sleep(5000L)
    println("StateInの現在の値: ${stateInFlow.value}")
    println("--------------------")

}