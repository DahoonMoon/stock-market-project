# StockMarketProject

## 1. 개요
### 1) 과제
* 실시간 순위 정보를 고객에게 제공하는 RESTful API 서비스(인기순, 상승순, 하락순, 거래량순)
* Application이 로딩될 때 SampleData.csv 파일을 참고하여 기본 데이터가 생성 및 DB에 적재됨

### 2) 사용기술
* Spring Boot 2.7.12
* Java 11
* RESTful API
* Spring Data Jpa
* QueryDSL
* H2 Database(Embedded)
* OpenAPI Specification3(Swagger)

### 3) 설치방법
```bash
$ ./gradlew build
$ java -jar build/libs/stock-0.0.1-SNAPSHOT.jar
```

### 4) 정보
* Swagger : http://localhost:8080/api/v1/apis
* H2 Database Console : http://localhost:8080/api/v1/h2-console
  * driver-class-name: org.h2.Driver
  * url: jdbc:h2:mem:testdb
  * username: sa
  * password:

## 2. 설계 및 문제해결 전략

### 1. 개념 정의

* 인기순
  * 개별 주식 종목의 상세 페이지의 조회수를 기반으로 순위를 매기는 기능입니다. 
  * 종목별 조회 이력을 적재하고, API 호출 시점부터 12시간 내의 조회수 합계 순위를 매겼습니다.

* 상승순/하락순
  * 개별 주식의 지난 거래일 종가 대비 현재 종가를 나누어 가격 변동률을 계산하여 순위를 매겼습니다. 
  * API 호출 시점이 거래시간 내 인지 아닌지에 따라, 비교 대상인 가격과 현재 가격을 가져오는 시점이 달라집니다.

    - 개장일, 장 시작 전 : 이전 거래일 16시 종가
    - 개장일, 장 중 : 현재 시각의 종가
    - 개장일, 장 마감 후 :당일 16시 종가
    - 휴장일 : 이전 거래일 16시 종가

* 거래량순
  * 시간별 주식 종목별 가격 데이터가 적재될 때, 그 시간 구간의 거래량 데이터도 함께 적재됩니다. 
  * API 호출 시점에 따라 일간 거래량 합계 순위를 매겼습니다. API 호출 시점에 따라 거래량 집계 시점이 달라집니다.
  
    - 개장일, 장 시작 전 : 이전 거래일 거래량 합계
    - 개장일, 장 중 : 당일 거래량 합계
    - 개장일, 장 마감 후 :당일 거래량 합계
    - 휴장일 : 이전 거래일 거래량 합계

* 데이터 리셋
  * 초기 생성한 데이터를 모두 삭제하고, 동일한 로직으로 생성합니다.


### 2. DB 스키마

* stock : 주식 종목 정보 테이블
* stock_view : 주식 종목 조회 로그 테이블
* stock_price : 시간대별 주식 종목별 가격, 거래량 테이블
* member : 회원 정보 테이블
* ERD<br>

### 3. 데이터 생성

* 주식 종목 테이블(stock) : 첨부된 CSV파일을 읽어 생성하였습니다.
* 회원 테이블(member) : Datafaker 사용하여 임의의 회원을 생성하였습니다.
* 주식 가격 테이블(stock_price) : 종목별 초기 가격을 CSV 파일을 읽어 생성하고, 다음 가격은 +-10 호가씩 랜덤으로 변동을 주었습니다.
* 주식 조회수 테이블(stock_view) : 시간대별 랜덤 회원이 랜덤 종목을 조회하는 데이터를 생성하였습니다.

### 4. API 명세

### `POST /reset`

데이터 리셋 API

#### Request

없음

#### Response

```json
{
  "status": 200,
  "message": "OK",
  "data": "데이터 리셋 완료"
}
```


### `GET /stocks`

실시간 순위 제공 API

#### Request

| Parameter  | Type   | Description                             | Default   | Required |
| ---------- | ------ |-----------------------------------------| --------- | -------- |
| `sortField`| String | 정렬 기준(`CHANGE`, `VOLUME`, `POPULAR`)    | None      | Yes      |
| `sortOrder`| String | 정렬 순서 (`ASCENDING`, `DESCENDING`)       | `DESCENDING` | No       |
| `page`     | Number | 페이지 번호                                  | `1`       | No       |
| `size`     | Number | 페이지당 종목 개수                              | `100`     | No       |

#### Response

```json
{
  "status": 200,
  "message": "OK",
  "data": [
    {
      "order": 1,
      "stock_id": 1,
      "stock_code": "005930",
      "stock_name": "삼성전자",
      "price": 100000,
      "volume": 0.3000,
      "totalView": 100,
      "totalVolume": 10000
    },
    ...
  ]
}
```
