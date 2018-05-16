## Comment 리스트 조회

- endpoint: GET /comments/cursor={cursor}&pageSize={pageSize}&orderType={orderType}

### Request

- Query parameter

| Key    | Value | Type | Info | required |
| :----- | :---- | :--- | :--- | :------- |
| pageNo | 0 | int | 현재 페이지 | true |
| cursor | 0 | int | 현재 포인터 | true |
| pageSize   | 10  | int  | 한 페이지의 사이즈 | false (default : 10) |
| orderType | "DESC" | enum | 정렬 방법 ("ASC", "DESC") | false (default : "DESC") |

- Header parameter

| Key    | Value | Type | Info |
| :----- | :---- | :--- | :--- |
| postId | 1 | int | 게시글 아이디 값 |
| userId | 1 | int | 사용자 아이디 값 |

### Response

#### Success
- status code : 200

| Key    | Value | Type | Info |
| :----- | :---- | :--- | :--- |
| total_size | 10 | int | 데이터 리스트의 갯수(default: 10 -> 수정 불가) |
| data_list | 데이터 리스트 | array | 데이터 리스트 |

```Json
result: {
    "total_size": 10,
    "data_list": [
        {
            "post_id": 1,
            "user_id": 1,
            "content": "wonderful naver hackday",
            "like_num": 10,
            "dislike_num": 1,
            "total_react_count": 11
        },
        {
            "post_id": 1,
            "user_id": 2,
            "content": "i like you",
            "like_num": 7,
            "dislike_num": 1,
            "total_react_count": 8
        },
        {
            "post_id": 1,
            "user_id": 3,
            "content": "i love you",
            "like_num": 6,
            "dislike_num": 1,
            "total_react_count": 7
        }
    ]
}
```

#### Fail

##### 잘못된 파라미터

- cursor값이 null인경우
- cursor값이 pageSize * 5 보다 큰 경우
    - pageSize의 5배 만큼만 DB에서 가져온 후 Redis에 캐싱함.(캐싱 히트율을 높이기 위함)
- orderType에 ASC, DESC가 아닌 다른 값이 들어간 경우
- pageSize < 0인 경우

| Key    | Value | Type | Info |
| :----- | :---- | :--- | :--- |
| return_code | 404 | int | 응답 코드 |
| return_message | 잘못된 파라미터 요청 -> size 조건 확인 | string | 응답 메시지 |
