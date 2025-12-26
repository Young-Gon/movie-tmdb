# 🎬 MovieTMDB (Jetpack Compose Movie App)

**MovieTMDB**는 TMDB API를 활용하여 최신 영화 및 TV 프로그램 정보를 제공하는 안드로이드 애플리케이션입니다.
이 프로젝트는 **Modern Android Architecture**를 준수하며, **Jetpack Compose**를 활용한 선언형 UI, **Clean Architecture** 기반의 모듈화, 그리고 **퍼포먼스 최적화**에 중점을 둔 포트폴리오용 프로젝트입니다.

---

## 🛠 Tech Stack

| Category | Technology |
| --- | --- |
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Material3) |
| **Architecture** | Clean Architecture (Multi-module), MVVM |
| **DI** | Hilt |
| **Async** | Coroutines, Flow |
| **Network** | Retrofit2, OkHttp3, Kotlin Serialization |
| **Image Loading** | Coil3 |
| **Navigation** | Androidx Navigation3 (Compose) |

---

## 🏗 Project Structure & Architecture

이 프로젝트는 **Clean Architecture** 원칙에 따라 관심사를 분리하고 유지보수성을 높이기 위해 멀티 모듈 구조로 설계되었습니다.

```text
📂 movietmdb
├── 📂 app            # [Presentation Layer] UI 및 ViewModel, DI 설정
│   ├── 📂 ui         # Screen, Component, Theme
│   ├── 📂 navi       # Navigation Route 정의
├── 📂 domain         # [Domain Layer] 비즈니스 로직 (순수 Kotlin)
│   ├── 📂 model      # 데이터 모델 (Interface, Data class)
│   ├── 📂 repository # Repository Interface
│   └── 📂 usecase    # 비즈니스 로직 단위 (UseCase)
├── 📂 data           # [Data Layer] 데이터 소스 및 구현체
│   ├── 📂 module     # Hilt Dependency Injection 모듈
│   ├── 📂 service    # Retrofit Service
│   ├── 📂 repository # Repository Implementation
│   └── 📂 model      # API Response DTO
└── 📂 NetworkFetcher # [Core/Utility] 네트워크 상태 관리 및 공통 유틸리티
```

---

## ✨ Key Features (주요 기능)

### 1. 홈 화면 (Home Screen)
- **탭 기반 탐색:** `Movies`, `TV Shows`, `Search` 3개의 메인 탭을 제공합니다.
- **부드러운 전환:** `HorizontalPager`를 사용하여 탭 간 스와이프 및 애니메이션 전환을 지원합니다.
- **섹션별 데이터:**
    - **Now Playing / Airing Today:** 현재 상영/방영 중인 컨텐츠 리스트
    - **Trending:** 가로 스크롤 가능한 트렌딩 배너 (`HorizontalPager`)
    - **Upcoming / Top Rated:** 가로 스크롤 리스트 (`LazyRow`)

### 2. 상세 화면 (Detail Screen)
- **몰입형 UI:** 배경에 백드롭 이미지를 적용하여 시각적 완성도를 높였습니다. (성능을 위해 실시간 Blur 대신 투명도 최적화 적용)
- **정보 제공:** 포스터, 제목, 평점, 장르, 제작사, 시놉시스 등 상세 정보를 표시합니다.
- **관련 영상:** 예고편 등의 관련 비디오 리스트를 제공합니다.

### 3. 검색 (Search)
- 키워드를 통해 영화 및 TV 프로그램을 검색할 수 있는 기능을 제공합니다.

---

## 🚀 Technical Highlights (기술적 특징 및 최적화)

이 프로젝트는 단순한 기능 구현을 넘어, 실제 앱에서 발생할 수 있는 **성능 문제 해결**과 **사용자 경험(UX) 개선**에 집중했습니다.

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