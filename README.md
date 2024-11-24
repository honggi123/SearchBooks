# Skills
Mvvm, Coroutine, Flow, Compose, Jetpack(ViewModel, Paging3), Retrofit, Hilt

## 아키텍처
<img width="612" alt="스크린샷 2024-11-22 오후 3 06 53" src="https://github.com/user-attachments/assets/8656f35e-fc20-48fd-a26e-7fa3ec6a7ebc">

#### View → ViewModel → Model 
1. View에서 이벤트가 발생하면, ViewModel이 해당 이벤트 처리. 
2. 필요에 따라 Data Layer (Model) 에 데이터 요청.
3. Data Layer에서 반환된 결과는 ViewModel에서 UI 상태(UI State)로 변환되어 View에 제공.
 
#### Mvvm 패턴
* UI로 부터 비즈니스, UI 로직 분리
* UI와 비즈니스, UI 로직을 독립적으로 개발 가능, UI 변경에 모델과 뷰모델 변경 최소화
* Jetpack ViewModel 활용, 뷰의 라이프사이클과 독립적으로 데이터 유지 (ex. 화면 회전)

## 패키지 구조 
```bash
├── feature
│   ├── component
│   ├── search
│   └── bookdetail
├── data
│   ├── repository
│   ├── network
│   └── model
└── di
```

## 주요 화면 및 기능
### 1. 도서 검색 화면 
`feature - search`
#### 1-1. 시스템에 의한 앱 프로세스 종료 대응 
  * savedStateHandle을 통한 검색 쿼리 저장
  * 종료 후 앱 재시작 시, 이전 검색 쿼리 복원
#### 1-2. Google Books API 호출 최적화
  * [Google Books API 성능 문서](https://developers.google.com/books/docs/v1/performance?hl=ko) 참고
  * 필요한 필드만 요청하도록 fields 파라미터 지정
  * 해당 코드 data/network/service 참고
#### 1-3. 화면 회전 시 검색어 및 검색 결과 유지 
  * 화면 회전 시 뷰의 생명주기로 인한 검색어 및 검색 결과 데이터 사라지는 이슈
  * 입력한 검색어 및 검색결과 유지를 위해, Jeptack ViewModel + StateFlow 를 통해 최신 상태 유지.
#### 1-4. 검색어 입력시 debounce 처리
  * 사용자가 검색어를 빠르게 입력할 때 불필요한 API 호출 방지
  * SearchViewModel 내부 Flow의 debounce 연산자 적용.
#### 1-5. 검색 상태에 따른 화면 분기
  * SearchViewModel에서 검색 상태에 따라 uiState를 뷰에 전달
  * uiState 기반으로 ui 표시
    
    * 검색을 아직 수행하지 않은 경우
    * 검색어가 있는 경우
    * 검색 결과가 없는 경우
#### 1-6. Jetpack Paging3의 cachedIn()을 통해 캐싱 
  * 검색 결과는 cachedIn 연산자를 통해 ViewModel 범위에서 캐싱
  * 캐싱을 통해 같은 호출에 대해 재 호출 방지
#### 1-7. 검색어 모두 지우기 기능 

### 2. 디테일 화면 
`feature/detail`
#### 2-1. 네트워크 오류 또는 미연결 오류처리
  * API 호출을 비동기적으로 처리하는 Flow에서 발생할 수 있는 예외를 catch 
  * 예외 발생 시 뷰모델에서 Custom Exception (NetworkException, ApiException, UnknownException) 분기
  * Custom Exception을 기반으로 ui 업데이트

### 공통
#### 1. 이미지 로딩 ui 컴포넌트 
`ImageLoader.kt - feature/component`
  * AsyncImagePainter의 상태를 관찰
  * rememberAsyncImagePainter을 통해 리컴포지션 시 이미지 재 로딩 방지
    
    * 로딩 중 프로그레스바 표시.
    * 로딩 실패 시 대체 이미지 표시.
    * 로딩 성공 시 이미지를 화면에 출력.
