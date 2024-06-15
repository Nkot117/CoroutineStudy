### Mutex
- 排他制御のためのツール
- Mutexを使用することで、複数のスレッドが同時に同じリソースにアクセスすることを防ぐことができる。
- Mutexはキャンセル可能。
  - Mutexを呼び出したCoroutineがキャンセルされると、Mutexもキャンセルされる。
### Semaphore
- Mutexと同じく、排他制御のためのツール
- Mutexと異なり、Semaphoreは複数のスレッドが同時に同じリソースにアクセスすることを許可する。

### select
- 複数のCoroutineから、最初に完了したCoroutineを選択するためのツール