특징

1. navigation 3사용(뒤로 가기 예측 가능)
2. compositionLocal을 사용한 Dialog로 Composable 함수 밖에서 Dialog 표시 가능
3. NetworkFetcher를 사용한 네트워크 콜 간편성
    * 간편한 refresh
    * cache 사용으로 refresh 시 데이터 유실 없음
    * DetailScreen에서 전달 받은 케시로 화면 구현
4. MutateFetcher 사용으로 간편한 데이터 변경
    * Button등 Composable 함수 밖에서 데이터 변경 요청
    * 변경 요청한 데이터 상태 추적
    * 필요시 사이드 이펙트 전달 가능(예: 스낵바, 다이얼로그)
5. navigation 3과 통합한 Hilt 사용
6. CleanArchitecture 사용
7. UseCase 사용