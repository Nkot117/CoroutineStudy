## Flow
### Flowとは
- Flowは非同期のデータストリームを表現するためのAPI
- 非同期処理の中に、複数回データを返すことが可能
  - flowではsuspend関数が使用できる
## FlowはCold Stream
- FlowはCold Streamのため、以下のような動きをする
  - collect関数がよばれるときにデータの生成が開始される
  - 複数回collect関数が呼ばれると、その数分同じデータを流す
## Flowのキャンセル
- Jobを使ってキャンセルすることが可能
- CoroutineScope.launchで返されるJobを使ってキャンセルする
  - Job.cancel()でキャンセル可能
## Flowのエラーハンドリング
- try-catchでエラーハンドリングが可能
- flowのcatchオペレータを使用してエラーハンドリングすることも可能
## Flowの合成
- combine、zip関数を使用して、複数のFlowを合成することが可能
  - combine : いずれかのFlowが更新されるたびに、最新のデータを合成する
  - zip : 2つのFlowが更新されるまでまち、２つの更新が完了してから、最新のデータを合成する
- 複数のFlowの待ち合わせを行うことが可能
## LaunchInを使用したFlowの実行
- CoroutineScopeとcollectを使用すると、階層が深くなる
- launchInを使用することで、階層を浅くすることが可能
  - launchInはCoroutineScopeを拡張した関数
  - launchInを使用すると、CoroutineScopeを指定することが可能
## Hot Streamに変換する
- FlowをShareInを使用して、Hot Streamに変換することが可能
  - HotStreamにすることで、collect関数が呼ばれるたびに、Flow内の処理が何度も呼び出されることを防ぐことができる
## StateFlowに変換すう
- FlowをStateInを使用して、StateFlowに変換することが可能
  - StateFlowは値を保持することが可能
  - StateFlowは値の変更を監視することが可能
  - StateFlowは値の変更を通知することが可能