## Coroutine
- Coroutineとは
    - 非同期処理を同期処理のように記述できる
    - コールバック地獄を回避できる
    - スレッドの管理を自動で行ってくれる
    - 他の非同期処理ライブラリとの組み合わせも可能
- Coroutineの基本
  - 非同期で行いたい処理は、`suspend`修飾子をつける
    - まるで同期処理のようにコードを書ける
  - エラーハンドリングもtry-catchで行える
  - 軽量スレッド
    - スレッドを再利用し、他のCoroutineを同じスレッドで動かすことができる
    - 並行に動作、メモリを効率的に使用
  - 使い方
    - CoroutineScope
      - Coroutineを起動するには、CoroutineScopeが必要
      - CoroutineScopeは、Coroutineを管理するためのクラス
      - 作成方法はいくつかある
        - GlobalScope
          - アプリケーション全体で使えるCoroutineScope
          - アプリケーション全体で使えるため、使いすぎるとメモリリークの原因になる
          - 基本的には使用は避けるべき
        - MainScope
          - UIスレッドで使えるCoroutineScope
          - Androidの場合、UIスレッドでの処理を行う際に使用
          - ActivityやFragmentのライフサイクルに関連付けて使われる
        - ViewModelScope
          - ViewModelで使えるCoroutineScope
          - ViewModelのライフサイクルに関連付けて使われる
          - ViewModelが破棄されると、関連付けられたCoroutineもキャンセルされる
        - lifecycleScope
          - LifecycleOwnerのライフサイクルに自動的に関連付けられ、LifecycleOwnerが破棄されるとコルーチンがキャンセルされる
        - SupervisorScope
          - 子Coroutineが失敗しても、親Coroutineに影響を与えない
        - runBlocking
          - 起動されたlaunchの処理が完了するまで処理をBlockし、他の処理が止まる
          - 基本的には使用は避けるべき
    - CoroutineScope内で``launch``を使うことでCoroutineを起動できる
    - 複数回のlaunchを使うと、それらの処理は並行で動く
    - CoroutineScopeのキャンセル
      - CoroutineScopeのキャンセルキャンセルすると、そのCorourineScopeで起動していたCoroutineはすべてキャンセルされる
      - キャンセルの伝播
        - Coroutine①の中でCoroutine②が呼ばれた場合、Coroutine①がキャンセルされると、子Coroutine②もキャンセルされる
- Job
  - Coroutineを起動すると、返却値としてJobを取得することができる
  - JobはCoroutineのライフサイクルをハンドリングできる
    - Jobを使用し、Coroutineの状況の監視・キャンセルが可能
    - また、joinを使うと、Jobが完了するまで待機させることもできる
- async
  - 複数のCoroutineの実行結果を受け取り、各々の値を使用できる
  - Jobだと、複数のCoroutineの実行結果を待ち合わせした時に、Coroutineの実行結果を取得・使用できない
- suspend functionで関数化
  - 非同期で行いたい処理を関数にまとめることができる

    