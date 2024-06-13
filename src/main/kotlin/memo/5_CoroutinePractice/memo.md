## Coroutine
### Coroutineの実行時間を制限する
- `withTimeout`
  - 指定した時間内で処理が終わらない場合、例外をスローする
  - `withTimeoutOrNull`は例外をスローせずにnullを返す

### suspend関数の並列実行する
- suspend関数内にはCoroutineScopeがないため、`coroutineScope`を使用する
  - `coroutineScope`はsuspend関数内でのみ使用可能
  - `coroutineScope`でasync、awaitを使用することで複数のCoroutineの待ち合わせ処理か可能
