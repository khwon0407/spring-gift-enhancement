# spring-gift-enhancement

## Step 1. Jpa 적용
1. Product에 대해 Jpa 적용
2. Member에 대해 Jpa 적용
3. Wishlist에 대해 Jpa 적용
4. Testcode 작성

### JPA를 추가하면서...
- 기존의 JDBC 코드는 삭제
- schema.sql 삭제
- properties 설정으로 테이블이 먼저 생성되고 초기 데이터가 삽입될 수 있도록 변경

## Step 2. 페이지네이션
1. Product 조회에 대해 페이지네이션 적용
2. Wishlist에 대해 페이지네이션 적용
3. Step 1 피드백 반영

### 페이지네이션을 추가하면서...
- param으로는 총 3가지의 인자를 받음
  - page : 몇 페이지의 데이터를 볼 것인가?
  - size : 한 번에 몇개씩 볼 것인가?
  - criteria : 어떤 필드를 기준으로 정렬하여 볼 것인가?
- criteria의 경우 사용할 수 있는 필드는
  - id : wishlist에 담은 순서대로 출력
  - productId : 상품 아이디 순서대로 출력
  - productCnt : 상품 개수대로 출력

##Step 3. 상품 옵션
1. option entity 추가 / id, product, name, quantity로 구성
2. product는 ManyToOne으로 연결
3. name은 unique 설정
4. product에 options 필드 설정, OneToMany로 설정
5. 옵션이 반드시 하나 이상 존재하도록 설정
6. add request에 옵션을 반드시 받도록 수정
7. 옵션 추가 등록 구현, 수정 / 삭제도 필요하다면 구현
8. 옵션에서 수량 감소 구현
9. 테스트 코드 작성