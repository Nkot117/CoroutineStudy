## Coroutine以外の非同期処理
- なぜ非同期処理が必要なのか
    - ユーザーが操作をしている間に、データの取得や処理を行いたい
    - 処理に時間がかかると、例えば画面(UI)がフリーズしてしまう等の問題が発生する
- スレッド処理
    - 初期の非同期処理はスレッド処理であった
    - スレッド処理は、プログラムの実行を分割して、複数の処理を同時に実行することができる
      - UIを更新しつつデータ処理が可能
   ```kotlin.kt
    // スレッド処理の例
    val thread = Thread {
        // ここに非同期で実行したい処理を記述
    }
    thread.start()
    ```
  - コールバック
    - スレッド処理の問題点
      - スレッドが増えていった時に管理が難しくなる
      - 待ち合わせ処理、エラーハンドリングの対応を入れると複雑なコードになる
        - コールバックの登場
          - 終了時の処理をコールバック関数として渡し、終了処理を定義する
    - ただし、複雑な仕様を実装するとコールバック地獄となり、コードの可読性が下がる

      ```kotlin.kt
      interface OnDataListener<T> {
        fun onSuccess(data: T)
        fun onFailure(e: Throwable)
        }
      fun fetchData(listener: OnDataListener<Data>) { /* ... */ }
      
      fun main() {
          fetchData(object : OnDataListener<Data> {
              override fun onSuccess(data: Data) {
                  // データ取得完了時の処理
                  // ...
              }
    
              override fun onFailure(e: Throwable) {
                  // エラー時の処理
                  // ...
              }
          })
      }
    
      ``` 
  - Rx
    - コールバック地獄を解消するために登場
    - 上から処理が順番に実行される
    - コールバック地獄のように、深いインデントを避けることができ、コードの可読性が高い
    - しかし、多くのオペレータを把握する必要があり、学習コストが高い
    ```kotlin.kt
    fun main() {
    fetchData() // ①サーバからデータを取得
        .concatMap { transform(it) } // ②データを加工
        .concatMapCompletable { save(it) } // ③データを保存
        .doOnError { /* エラー時の処理 */ }
    }
     ```