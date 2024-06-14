package org.example.memo.StateFlow_SharedFlow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun main() {
    // SharedFlow
    println("SharedFlow")
    val coroutineContext = Dispatchers.Default + Job()
    val coroutineScope = CoroutineScope(coroutineContext)
    val mutableShared = MutableSharedFlow<Int>()

    mutableShared.onEach {
        println("１つ目：$it")
    }.launchIn(coroutineScope)

    mutableShared.onEach {
        println("２つ目：$it")
    }.launchIn(coroutineScope)

    coroutineScope.launch {
        mutableShared.emit(1)
        delay(1000L)
        mutableShared.emit(2)
        delay(1000L)
        mutableShared.emit(3)
    }

    Thread.sleep(4000L)
    println("--------------------")

    // Replay SharedFlow
    println("Replay SharedFlow")
    val mutableReplayShared = MutableSharedFlow<Int>(replay = 2)

    coroutineScope.launch {
        mutableReplayShared.emit(1)
        mutableReplayShared.emit(2)
        mutableReplayShared.emit(3)

        // collectが呼ばれる前にemitされた直近2つの値を拾って流れる
        launch {
            mutableReplayShared.collect {
                println("collect1: $it")
            }
        }
    }

    Thread.sleep(4000L)
    println("--------------------")

    // StateFlow
    println("StateFlow")
    val mutableState = MutableStateFlow(0)

    mutableState.onEach {
        println(it)
    }.launchIn(coroutineScope)

    mutableState.value = 1
    coroutineScope.launch {
        delay(1000L)
        mutableState.value = 2
        delay(1000L)
        // 同じ値はskip
        mutableState.value = 2
        delay(1000L)
        // 連続でemitされた場合は最後の値だけ流れる
        mutableState.value = 3
        mutableState.value = 4
    }

    Thread.sleep(4000L)

    println("現在の値" + mutableState.value)
}