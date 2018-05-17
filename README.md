# PART

- 사용자의 반응(공감, 공감취소)수치 업데이트

Redis의 List구조에 저장되어 있는 사용자 반응에 대한 댓글 id를 읽어 해당 반응을 MySQL에 반영

         * Spring Batch 사용

1. Reader는 사용자의 반응에 대한 데이터(댓글 id)가 담겨있는(Redis List 자료구조) Redis에 접근하여 데이터를 하나씩 읽어 JAVA List에 저장시킵니다.

2. JAVA List에 3000개의 데이터가 쌓이거나 Redis(List자료구조)에 데이터가 없을 시 해당 데이터를 List로 Writer에게 넘겨줍니다.

3.  Writer는 해당 리스트를 받아 Mapper를 호출하여 Mysql에 각 댓글 반응 수치를 UPDATE합니다.

※ Batch server를 따로 두어 스케쥴러를 통해 주기적으로 데이터를 수집 및 반영 하는  구상을 갖고 있지만 이번 프로젝트에서는 웹에 띄워 api를 호출하여 기능의 작동 여부를 보여줄 계획입니다.

# Why Spring Batch?

공부해 보고 싶었던 spring batch를 공부하여 적용시켜 볼 수 있는 프로젝트였습니다.
framework를 사용함으로써 안정적이며 객체 지향의 설계로 인해 재사용성, 확장성, 유지보수성을 높힐 수 있습니다. 공부하면서 STEP을 구성하는 Reader, Processor, Writer가 배치의 목적에 맞게 책임 분리가 확실히 되어 있는 점에서객체지향의 설계에 배움을 얻었습니다. (시스템이 확장함에 따라 큰 이점을 볼 수 있습니다. 새로운 기능을 추가 하였을 때 새로운 컴포넌트만 등록하여 사용할 수 있습니다. )

# 설계
[
![image](https://user-images.githubusercontent.com/33171233/40120372-fcd99bfa-5959-11e8-91bb-4d499dab64e8.png)
](url)
