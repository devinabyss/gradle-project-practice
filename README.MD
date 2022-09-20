# 개요
## 기능 구현
1. 블로그 검색
    - 카카오, 네이버 의 검색 API 를 활용하여 블로그 검색 기능 제공합니다.
      - 카카오와 네이버의 페이징 최대 허용치 조건이 달라 카카오를 기준으로 제한을 두었습니다.
    - 기본적으로 카카오 API 를 사용하되, 카카오 API 연동이 Break 될 경우 네이버 API 로 대체되도록 합니다.
      - 연동 실패 시 Retry 횟수는 총 3번이며, BadRequest 와 Circuit Break 상태로 넘어갔을 경우엔 Retry 하지 않습니다.
      - Circuit Break 역시, BadRequest 는 Break 조건에 카운팅 되지 않도록 합니다.
      - BadRequest 는 최대한 내부 비즈니스 로직적으로 발생되지 않도록 막아야겠지만, 유저의 검색 조건에 따라 무의식적인 BadRequest 의 발생 가능성을 열어두었습니다.  
    - 제공 API 에는 옵션을 제공하지 않으나, 내부 서비스 로직 상으로는 어떤 API 를 우선 사용할 것인지 명시할 수 있도록 되어 있습니다.
    - 카카오, 네이버로의 연동은 실시간으로 반드시 질의가 이뤄져야 할 데이터로 판단하지 않아, 10초 가량의 캐싱이 되도록 로컬 캐싱 라이브러리 (EhCache) 를 사용했습니다.
2. 인기 검색어 목록
   - 검색 카운팅의 조건이 검색이 완료된, 이라는 정의가 별도로 없어 질의 시도를 카운팅의 기준으로 삼았습니다.
   - 검색 요청이 서비스 레이어로 넘어오면 즉시 질의 히스토리가 Publisher - Listener 를 통해 기록됩니다.
     - Publish 가 실패할 경우 실패한 질의 기록을 남기며, 이렇게 남은 기록은 별도의 배치 프로세스를 통해 재발행는 것을 의도하였으나, 재발행 로직까지 구현되어 있진 않습니다.
     - Listener 의 Event Consume 실패의 케이스도 Consume 실패의 기록을 별도로 가지면 더 좋을 것으로 보이나, 구현되어 있진 않습니다.
   - 검색어 카운팅 조회는 JPA - QueryDSL 을 통해 질의 히스토리를 키워드 기준으로 집합 질의 합니다.
   - 질의 시간에 따라 차이가 날 수는 있으나, 최종적으로 질의 수에 근접한 결과가 나도록 하는 것이 구현 의도 방향 입니다.
     - 별도 배치 프로세싱을 통해 집계 데이터를 내고, 집계 데이터 + 이력 데이터 로 조회하는 추가 보완 방향을 고려할 수 있을 것 같습니다.
     - 실시간 카운터의 적용을 고려해볼 수도 있겠지만, 최소 - 최대 기간의 적용 기준을 잡기 애매하여 구현 고려 대상으로 삼지 않았습니다.

## 의존성
- `org.apache.commons.commons-lang3` : String 핸들링
- `org.apache.commons.commons-collections4` : Collection 핸들링
- `org.springframework.boot.spring-boot-starter-web` : 스프링 웹 프로젝트 
- `org.springframework.boot.spring-boot-starter-data-jpa` : 스프링 - JPA 통합
- `org.springframework.boot.spring-boot-starter-validation` : 스프링 validation 
- `org.springframework.boot.spring-boot-configuration-processor` : 스프링 부트 설정 처리
- `org.springframework.boot.spring-boot-starter-json` : 스프링 JSON 핸들링 통합
- `org.springframework.boot.spring-boot-starter-cache` : 스프링 캐시 통합
- `org.springframework.cloud.spring-cloud-starter-openfeign` : Feign Client 사용
- `org.springframework.cloud.spring-cloud-starter-circuitbreaker-resilience4j` : Retry, Circuit Breaker 기능 적용 
- `com.querydsl.querydsl-jpa` : JPA - QueryDSL
- `com.querydsl.querydsl-apt` : JAP - QueryDSL
- `io.github.openfeign.feign-okhttp` : Feign Client 의 실 통신 구현체
- `org.ehcache.ehcache` : 캐싱 구현체
- `javax.cache.cache-api` : 캐싱 구현체 연결
- `com.h2database.h2` : 메모리 DB

# API 명세
- 기본 설정 포트 : 11080

## 블로그 검색
- URI : `/blog/search`
- Method : `GET`
- Response Body : `application/json`

### Request Parameters
| 키 | 필수 여부 | 설명 | 예시 |
| -- | -- | -- |  -- |
| `keyword` | O | 블로그 포스트 검색어 | 김종국, 유재석 ... |
| `page` | X | 검색 결과 페이징 위치. 기본값 1 | 1 ~ 50 |
| `pageSize` | X | 검색 결과 반영 페이징 크기. 기본값 10 | 1 ~ 50 |


### Response Information
#### Main
| 키 | 데이터 타입 | 설명 | 
| -- | -- | -- |
| `totalCount` |  Number |  검색된 총 블로그 포스트 수 |
| `pageableCount` | Number |  동일 페이징 조건에서 제공되는 총 페이징 포스트 수 <br/> 카카오 소스의 값은 그대로 중계, 네이버 소스의 값은 페이징 조건에 맞춰 재구성됨 |
| `continuable` | Boolean |  다음 페이지가 존재 하는지 여부 |
| `searchSource` | String |  검색 결과의 원 시스템 <br/> `KAKAO` = 카카오 , `NAVER` = 네이버 |
| `subjects` | Object Array | 검색된 블로그 포스트 정보 리스트. 하단 `블로그 정보` 참조 |

#### 블로그 정보
| 키 | 데이터 타입 | 설명 |
| -- | -- | -- |
| `blogName` | String | 블로그 명 |
| `subjectTitle` | String | 블로그 포스트 명 |
| `subjectUrl` | String | 블로그 포스트 주소 |
| `summary` | String | 블로그 포스트 요약 내용 | 
| `thumbnailUrl` | String | 블로그 썸네일 이미지 주소. 네이버 소스는 빈값으로 제공 | 
| `postDatetime` | String | 포스트 게시일시. (`yyyy-MM-dd'T'HH:mm:ss`)|

#### Response Sample
```json
{
    "totalCount": 192456,
    "continuable": true,
    "pageableCount": 800,
    "searchSource": "KAKAO",
    "subjects": [
        {
            "blogname": "미국에서 블라블라",
            "subjectTitle": "가수 <b>김종국</b>보다 더 좋은 운동인 <b>김종국</b>에 대하여",
            "subjectUrl": "http://alrnr-passenger-number-0.tistory.com/129",
            "summary": "운동하는 사람이면 다 아는 채널 <b>김종국</b> 씨의 짐 종국. 저 역시 짐 종국 채널을 보다 보니 운동인 <b>김종국</b>에 대해 궁금해지는 것들이 생겨서 적어보는 포스팅입니다. 1. <b>김종국</b> 키 -177.4cm/5&#39;10&#34; 2. <b>김종국</b> 몸무게 81kg 3. <b>김종국</b> 체지방율! 8.5% 일반인이 8.5% 라니 아니 세상에나. 평소에 식단/운동을 얼마나 힘들게...",
            "thumbnailUrl": "https://search2.kakaocdn.net/argon/130x130_85_c/9cR8O5xU1uv",
            "postDatetime": "2022-09-14T06:10:09"
        },
        {
            "blogname": "새로운이로운",
            "subjectTitle": "소야 나이 프로필 <b>김종국</b> 조카",
            "subjectUrl": "http://newsforprofits.tistory.com/163",
            "summary": "소야 나이 프로필 <b>김종국</b> 조카 소야 <b>김종국</b> 조카로 알려진 소야 가수입니다. 가수 소야는 종종 미우새 등 <b>김종국</b>이 출연하는 프로그램에 출연하는데요. 최근에도 미우새에 출연해 남창희 개그맨과 러브라인을 보였습니다. <b>김종국</b> 유튜브 채널에도 종종 나와요. 오늘은 소야 가수 프로필 정보 소개할게요. 소야 본명은...",
            "thumbnailUrl": "https://search1.kakaocdn.net/argon/130x130_85_c/BH699bQpxrv",
            "postDatetime": "2022-06-26T18:46:57"
        },
        {
            "blogname": "MakeitRight",
            "subjectTitle": "송지효 <b>김종국</b> 나이 차",
            "subjectUrl": "http://makeweight.tistory.com/6",
            "summary": "런닝맨에서는 집종국 레이스로 꾸며진 가운데 송지효가 안주인 포스를 풍겨 눈길을 끌었습니다. 런닝맨 방송에서 오프닝부터 <b>김종국</b> 집 지하 주차장에서 시작되었습니다. 이에 송지효는 멤버들에게 &#34;저 여기 신혼집이다. 초대했으니까 마음껏 누리시라&#34;며 자연스럽게 안내를 시작해 웃음을 자아냈습니다. 목차 송지효...",
            "thumbnailUrl": "https://search2.kakaocdn.net/argon/130x130_85_c/80JXi9wrTzI",
            "postDatetime": "2022-07-04T16:22:31"
        },
        {
            "blogname": "AI search",
            "subjectTitle": "<b>김종국</b> 헬스 운동 다이어트 어록 명언 팁",
            "subjectUrl": "http://kaypen.tistory.com/383",
            "summary": "<b>김종국</b> 헬스 운동 다이어트 어록 명언 팁 <b>김종국</b> 헬스 운동 다이어트 어록 명언 팁 1 운동할 때 힘이 든 것은 몸이 아니라 마음이다. <b>김종국</b> 헬스 운동 다이어트 어록 명언 팁 2 헬스클럽은 클럽보다 더 즐거운 곳이다. <b>김종국</b> 헬스 운동 다이어트 어록 명언 팁 3 운동만 하고 음식 먹는 걸 제대로 안 챙겨 먹으면 그건...",
            "thumbnailUrl": "https://search1.kakaocdn.net/argon/130x130_85_c/D6q7G0sm25v",
            "postDatetime": "2022-09-15T15:45:29"
        },
        {
            "blogname": "tokkisaru",
            "subjectTitle": "프랭크버거 전속 모델이 <b>김종국</b> 이었네요",
            "subjectUrl": "http://tokkisaru.tistory.com/287",
            "summary": "지난 7월 10일 SBS 미운 우리 새끼 프로그램에서 <b>김종국</b>의 햄버거 광고가 논란이었다고 해서 함 찾아보았다. 그런데 알고 보니 햄버거 프랜차이즈 프랭크 버거의 전속 모델이 가수 <b>김종국</b>이었다. 헬창으로 알려진 <b>김종국</b>이 프랭크 버거의 전속 모델인 줄은 생각지도 못했는데, 사실이어서 한번 지난 내용을 체크해...",
            "thumbnailUrl": "https://search4.kakaocdn.net/argon/130x130_85_c/73mxnCLylEA",
            "postDatetime": "2022-07-12T22:50:08"
        }
    ]
}
```


## 블로그 검색어 순위
- URI : `/blog/favoriteKeywords`
- Method : `GET`
- Response Body : `application/json`

### Request Parameters
| 키 | 필수여부 | 설명 | 예시 |
| -- | -- | -- | -- |
| `from` | X | 순위 집계 시작일. `yyyyMMdd` <br/> 기본값 : 현재 - 7일 | 20220101 |
| `to` | X | 순위 집계 종료일 . `yyyyMMdd` <br/> 기본값 : 현재 | 20221001 |


### Response Information
#### Main
| 키 | 데이터 타입 | 설명 |
| -- | -- | -- |
| 없음 |  Object Array | 하단 `검색어 정보` 참조 |

#### 검색어 정보
| 키 | 데이터 타입 | 설명 |
| -- | -- | -- |
| `keyword` | String |  검색된 키워드 |
| `count` | Number |  검색된 횟수 |

#### Response Sample
```json
[
    {
        "keyword": "김종국",
        "count": 6
    },
    {
        "keyword": "송지효",
        "count": 6
    },
    {
        "keyword": "런닝맨",
        "count": 5
    },
    {
        "keyword": "유재석",
        "count": 3
    },
    {
        "keyword": "전소민",
        "count": 3
    },
    {
        "keyword": "양세찬",
        "count": 2
    },
    {
        "keyword": "지석진",
        "count": 1
    },
    {
        "keyword": "하",
        "count": 1
    },
    {
        "keyword": "하하",
        "count": 1
    }
]
```