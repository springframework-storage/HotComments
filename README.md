# 사용자의 공감 요청 처리 기능

## Redis 데이터 타입 : Set
```
공감의 경우
Key : "Pst" + commentId
Member : userId
```

## Redis 데이터 타입으로 Set을 선택한 배경
* 공감/비공감이 존재할 경우에만 Redis에 데이터가 저장됩니다.
   > 각 댓글에 공감/비공감이 존재하지 않으면  Redis에 ("Pst or Ngt" + commentId)를 Key로 하는 데이터가 쌓이지 않습니다.<br/>
   > (공감/비공감 데이터가 없으면 어차피 각 Count = 0 이기 때문에 불 필요한 데이터를 넣지 않아도 된다고 생각했습니다.)

* Set 데이터 구조
   * Set은 내부적으로 2가지 데이터 구조를 사용합니다.
      * 데이터가 정수이고 member 수가 512개 이하일 때는 INTSET에 저장
      * 문자이거나 member 수가 512개보다 클 때는 Hash Table에 저장
      * 내부 데이터 구조를 정하는 파라미터는 redis.conf의 set-max-intset-entried로 수정 가능
         > 기본값은 512
   * 1개의 key당 member의 중복을 허용하지 않습니다.
   * INTSET
      * Set의 member가 정수일 때 메모리를 절약하기 위한 구조입니다.<br/> 
        (Hash Table에 비해 메모리를 적게 사용합니다.)<br/>
         ![메모리 사용량 비교](http://redisgate.kr/images/internal/intset-memory.png)
