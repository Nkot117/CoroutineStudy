## StateFlow・SharedFlow
### SharedFlow
- Flowの場合、Flowの外側から値を送信することができない
- SharedFlowはFlowの外側から値を送信することができる
- 外側からFlowにデータを送信する特性を活かし、イベント通知に利用することができる
```kotlin
class EventBus {
    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun produceEvent(event: Event) {
        _events.emit(event)
    }
}
```
- SharedFlowはFlowを継承しているため、Flowの特徴もそのまま引き継がれる
  - Cold Streamとして動作する 
  - map, filter, transformなどの演算子を利用することができる

### FlowをSharedFlowに変換する
- ShareInを利用することで、FlowをSharedFlowに変換することができる
- ShareInをHot Streamとして動作する

### Replay
- SharedFlowはReplayを利用することで、過去の値を再生することができる

### StateFlow
- 状態管理によく使われるFlow
- 以下のような特徴がある
  - 初期値が必須
  - collect時に最新の値を受け取る
  - 値の設定にCoroutineScopeが不要
  - 同じ値は流さない
  - 値が連続で流れてきた場合、最後の値のみ流す
- 今現在の状態も取得することが可能
