# jpa
- jpa란 java persistence api의 약자로 자바의 orm (object relational mapping) 표준 스펙을 정의
- jpa의 스펙은 자바의 객체와 데이터베이스를 어떻게 매핑하고 동작해야 하는지를 정의하고 있음

### hibernate
- orm framework 중 하나, jpa 프로바이더라고 부름
- jpa의 실제 구현체 중 하나이며, 현재 jpa 구현체 중 가장 많이 사용됨

### 영속성 컨텍스트 (persistence context)
- 영속성 컨텍스트는 jpa가 관리하는 엔티티 객체의 집합
- 엔티티 객체가 영속 컨텍스트에 들어오게 되면 jpa는 엔티티 객체의 매핑 정보를 가지고 db에 반영함
- 엔티티 객체가 영속 컨텍스트에 들어오게 되어 관리 대상이 되면 그 객체를 영속 객체라고 부름
---
- 영속성 컨텍스트는 세션 단위로 생명주기를 갖고 있음 (세션이 생기면서 만들어지고, 세션이 종료되면 없어짐)
- 영속성 컨텍스트에 접근하기 위해서 EntityManager를 사용함
- EntityManager는 하나의 세션으로 보고 아래와 같은 방식으로 동작을 구성함
---
- commit 시점에 영속성 컨텍스트의 변경 내역을 db에 반영 (변경 쿼리 실행)
- 스프링 연동시에는 대신 처리해줘서 매핑 설정 중심으로 작업

```text
1. EntityManager 생성 (EntityManagerFactory를 통해 생성)
2. EntityManager가 가지고 있는 트랜잭션(transaction)을 시작
3. EntityManager를 통해 영속 컨텍스트에 접근하고 객체를 작업
4. 트랜잭션을 커밋(commit)하여 db에 반영
5. EntityManager 종료
```

### 엔티티 클래스
- jpa 어노테이션을 활용하여 엔티티 클래스를 정의
- 주요 어노테이션은 아래와 같음

```text
@Entity : 해당 클래스가 jpa 엔티티 클래스라고 정의, 필수
@Table : 해당 클래스가 데이터베이스의 어느 테이블에 매핑되는지 정의
@Id : db 테이블의 primary key 칼럼과 매핑
@Column : 매핑한 데이터베이스의 칼럼 이름과 필드 변수의 이름이 다를 경우 매핑하기 위해 사용
@Enumerated : enum 타입 매핑
```

```text
@Table 속성
name : 테이블 이름, 생략시 클래스와 같은 이름 (name = "hotel_info")
catalog : 카탈로그 이름, mysql db 이름 (catalog = "point", name = "point_history")
schema : 스키마 이름, 오라클 스키마 이름 (schema = "crm", name = "crm_stat"
```

```text
@Enumerated 설정 값
EnumType.STRING : enum 타입 값 이름을 저장 (문자열 타입)
EnumType.ORDINAL (기본값) : enum 타입의 값의 순서를 저장 (숫자 타입, 값 변경시 문제가 생겨 사용 x)
```

### 엔티티 클래스 제약조건 (스펙 기준)
- @Entity, @Id를 꼭 적용해야함
- 인자 없는 기본 생성자 필요
- 기본 생성자는 public or protected
- 최상위 클래스여야 함
- final 안 됨

### 접근 타입

##### 필드 접근
- 필드 값을 사용하여 매핑
- @Id 어노테이션을 필드에 붙히면 필드 접근
---

##### 프로퍼티 접근
- getter / setter 메서드를 사용해서 매핑
- @Id 어노테이션을 getter 메서드에 붙히면 프로퍼티 접근
---

- @Access 어노테이션을 사용해서 명시적으로 지정
- @Access(AccessType.PROPERTY), @Access(AccessType.FILED)
- 불필요한 setter 메서드를 만들 필요가 없는 필드 접근 선호

### 식별자 생성 방식

##### 직접 할당 (Hotel 클래스 예제)
- @Id 설정 대상에 직접 값 설정
- 사용자가 입력한 값, 규칙에 따라 생성한 값 (이메일, 주문번호)
- 저장하기 전에 생성자 할당, 보통 생성 시점에 전달
---

##### 식별 칼럼 방식 (Review 클래스 예제)
- db가 식별 칼럼에 매핑 (mysql에 autoincreament)
- @GeneratedValue(strategy = GenerationType.IDENTIFY) 설정
- insert 쿼리를 실행해야 식별자를 알 수 있음
- EntityManager의 persist() 호출 시점에 insert 쿼리 실행
- persist() 실행할 때 객체에 식별자 값 할당됨
---

##### 시퀀스 사용 방식 (ActivityLog 클래스 예제)
- jpa가 식별자 생성 처리 -> 객체 생성시에 식별값을 설정하지 않음
- @SequenceGenerator로 시퀀스 생성기 설정
- @GeneratedValue의 generator로 시퀀스 생서기 지정
- EntityManager의 persist() 호출 시점에 시퀀스 사용
- persist() 실행할 때 객체에 식별자 값 할당됨
- insert 쿼리는 실행하지 않음
---

##### 테이블 사용 방식 (AccessLog 클래스 예제)
- 테이블에 엔티티를 위한 키를 보관
- 해당 테이블을 이용해서 다음 식별자 생성
- @TableGenerator로 테이블 생성기 설정
- @GeneratedValue의 generator로 테이블 생성기 지정
- EntityManager의 persist() 호출 시점에 테이블 사용
- persist() 실행할 때 테이블을 이요해서 식별자 구하고 이를 엔티티에 할당
- insert 쿼리는 실행하지 않음

### @Embeddable
- 엔티티가 아닌 타입을 한 개 이상의 필드와 매핑할때 사용 (예 : adress, money 등 매핑)
- 엔티티의 한 속성으로 @Embeddable 적용 타입 사용 (Address, Hotel 클래스 예제)
---
- address에 null 입력시, 매핑된 값들 모두 null로 입력 (MainNullEmbedded 클래스 예제)
- 같은 @Embeddable 타입 필드를 두 개를 사용하면 팩토리 초기화 시 중복 사용에 대한 에러 발생 (MainAttrOver 클래스 예제)
- 중복 에러를 해결하려면 @AttributeOverride로 설정 재정의 (Employee, MainAttrOver 클래스 에제)
---
- @Embeddable를 사용하면 모델을 더 잘 표현할 수 있음
- 개별 속성을 모아서 이해 (addr1, addr2, zipcode) -> 타입으로 더 쉽게 이해 (address)

### @Embeddable 다른 테이블 매핑
- 방번 1. @SecondaryTable + 테이블 명 (intro, writer 클래스 예제)
- 방법 2. @SecondaryTable + @ArrtibuteOverride (address, writer 클래스 예제)
---
- @SecondaryTable
- 다른 테이블에 저장된 데이터를 @Embeddable로 매핑 가능
- 다른 테이블에 저장된 데이터가 개념적으로 value (값, 예 : address)일 때 사용
- 1-1 관계인 두 테이블을 매핑할 때 종종 출현

### 값 콜렉션 set 매핑
- 콜렉션 테이블을 이용한 값 set 매핑
- @ElementCollection과 @CollectionTable이면 끝
- @Embeddable 사용시 해당 클래스에 equals + hashcode 메소드 생성

### 값 콜렉션 list 매핑
- 콜렉션 테이블을 이용한 값 list 매핑
- set과 동일하게 @ElementCollection과 @CollectionTable 사용
- 추가로 인덱스 값 지정을 위한 @OrderColumn 사용

### 값 콜렉션 map 매핑
- 콜렉션 테이블을 이용한 값 map 매핑
- set, list와 동일하게 @ElementCollection과 @CollectionTable을 사용
- 추가로 키 값을 지정하기 위한 @MapKeyColumn 사용

### 값 콜렉션 주의사항
- 컬렉션 매핑 사용시 성능문제를 고려할 수 밖에 없음
- 설문 질문 목록을 보여줄 때 각 질문의 보기 개수를 함께 표시?
  - 페이징 처리 필요
  - 콜렉션 데이터 자체는 필요 x
- 역할 목록을 표시할 때 가진 권한을 함께 표시?
  - 페이징 처리 필요
  - 각 역할마다 권한 조회 쿼리를 실행하고 싶지 않음
- 성능 문제 -> cqrs로 해결
- 변경 기능을 위한 모델과 조회 기능을 위한 모델을 분리
  - 변경 기능 - jpa 활용
  - 조회 기능 - mybatis / jdbctemplate / jpa 중 알맞은 기술 사용
- 모든 기능을 jpa로 구현할 필요는 없음
- 특히 목록, 상세와 같은 조회 기능

### 영속 컨텍스트와 캐시
- jpa는 영속 엔티티(객체)를 영속 컨텍스트에 담아 변경 추적
- 트렌잭션 커밋 시점에 변경 반영
- 동일 식별자 -> 동일 객체
- 영속 컨텍스트에 보관된 객체 조회 (repeatable read 효과)
- 대량 변경은 굳이 jpa로 할 필요 없음 -> 직접 쿼리 실행이 나음

### 영속 객체 라이프사이클
- find 메소드나 persist 메소드를 통해 쿼리 실행을 하면 managed 상태가 되어 영속 컨텍스트를 통해 관리됨 (변경 추척)
- remove 메소드를 실행하면 managed 상태로 영속 컨텍스트에 저장된 데이터를 삭제 후 롤백
- close 메소드나 detach 메소드를 실행하면 detached 상태가 되어 이후 실행 코드는 변경 추적의 대상에서 제외

### 엔티티 연관 매핑
- 엔티티가 다른 엔티티를 필드 / 프로퍼티로 참조
- 연관 종류 : 참조키 방식 1-1, 키 공유 방식 1-1, 단방향 n-1, 콜렉션 단방향 1-n, 양방향 n-1 / 1-n, m-n
- 엔티티 간 연관 거의 사용하지 않음 (양방향과 m-n은 전무한 수준)
- 이유 : 밸류(Embeddable 타입)로 매핑, 조회는 직접 쿼리 사용이 많음
---

##### 1-1 매핑
- 참조키 방식 1-1 단방향 연관 매핑
- @OneToOne, @JoinColumn 사용하여 참조하려는 객체를 매핑
- 식별자 공유 방식 1-1 단방향 연관 매핑
- @OneToOne, @PrimaryKeyJoinColumn 사용하여 식별자(pk)를 공유하려는 객체를 매핑
---

##### n-1 매핑
- n-1 단방향 연관 매핑 설정
- @ManyToOne, @JoinColumn 사용하여 참조하려는 객체를 매핑
---

##### 1-n 매핑
- 1-n 단방향 연관 매핑
- 콜렉션 (set, list, map)을 사용한 매핑
- set 사용 시, @OneToMany, @JoinColumn을 사용하여 매핑
- list 사용 시, @OneToMany, @JoinColumn에다 추가로 순서를 설정하기 위한 @OrderColumn을 사용하여 매핑
- map 사용 시, @OneToMany, @JoinColumn에다 추가로 키 값 저정을 위한 @MapKeyColumn을 사용하여 매핑

### 영속성 전파
- 연관된 엔티티에 영속 상태를 전파
- 예, 저장할 때 연관된 엔티티도 함께 저장
- 연관 관련 매핑 어노테이션에 cascade 옵션을 추가하면 사용 가능
- @OneToMany(cascade = CascadeType.PERSIST)
- 영속성 전파 종류 : ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH
- 기본적으로 사용하면 안됨 (엔티티 연관 매핑과 같은 이유)

### 연관 고려 사항
- 연관 대신 id 값으로 참조 고려
- 객체 탐색이 쉽다고 연관 쓰기 없기
- 조회는 전용 쿼리나 구현 사용 고려 (CQRS)
- 엔티티가 아닌 밸류인지 확인 (1-1, 1-n 관계에서 특히)
- 1-n 보다는 n-1 (이 경우도 어쩔 수 없는 상황이면)\
- 양방향 x

### jpql
- jpa query language
- sql 쿼리와 유사
- 테이블 대신 엔티티 이름, 속성 사용
- jpql 기본 구조
- select 별칭 from 엔티티명 별칭 ...
```text
select r from Review r
select r from Review as r
```
---

- 쿼리 생성
- TypedQuery<T> ~ = EntityManager.createQuery(String ql, Class<T> resultClass)
```text
TypedQuery<Review> query = em.createQuery(
    "select r from Review r", // 쿼리
    Review.class); // 결과 타입
    
List<Review> reviews = query.getResultList();
```
---

- where + and, or, 괄호 등
```text
select r from Review r where r.hotelId = :hotelId // :hotelId : 비교 대상 이름 지정
select r from Review r where r.hotelId = ? // ? : 위치 기반 비교 대상 지정
select r from Reivew r where r.hotelId = :hotelId and r.mark > :minMark
select p from Player p where p.position = :pos or p.team.id = :teamId
```
---

- 파라미터
- 이름 사용 : query.setParameter("hotelId", "H-001") 
- 인덱스 기반 : query.setParameter(0, "H-001")
```text
TypedQuery<Review> query = em.createQuery(
    "select r from Review r where r.hotelId = :hotelId order by r.id. desc",
    Review.class);
    
query.setParameter("hotelId", "H-001");
```
---

- order by
```text
select r from Review r order by r.id // 기본값은 오름차순
select r from Review r order by r.id asc // 오름차순
select r from Review r order by r.id desc // 내림차순
select p from Player p order by p.position, p.name
select p from Player p order by p.team.id, p.name
```
---

- 비교 연산자
```text
= : u.name = 'JPA'
<> : o.state <> ? // !=
> >= < <= : p.salary > 2000
between : mc.expiryDate between ? and ?
in, not in : o.mark in (1, 2, 3)
like, not like : u.name like '%유%'
is null, is not null : u.name is null
```
---

- 페이징 처리
- mysql의 경우, 쿼리에서 limit를 통해 페이징 처리
```text
TypedQuery<Review> query = em.createQuery(
    "select r from Review r where r.hotelId = :hotelId order by r.id. desc",
    Review.class);
    
query.setParameter("hotelId", "H-001");
query.setFirstResult(8); // 0부터 시작, 시작 행
query.setMaxResult(4); // 최대 결과 갯수

List<Review> reviews = query.getResultList();
```
---

- 그 외
- 집합 함수
  - count, max, min, avg, sum
- group by, having
- 콜렉션 관련 비교
  - member of, not member of, is empty, is not empty
  - exist, all, any
- jpql 함수
  - concat, substring, trim, abs, sqrt
  - 콜렉션 함수 : size, index 등
---

- 다음 경우는 jpql 말고 일반 쿼리 사용 고려
- 여러 테이블 조인
  - 레거시 테이블 조인
- dbms에 특화된 쿼리 필요
  - 예, 오라클 힌트
- 서브 쿼리 필요
- 통계, 대량 데이터 조회 / 처리

### criteria
- 코드로 쿼리를 구성하는 api
- jpql 대신 자바 사용
```text
// 1. CriteriaBuilder 생성 (이후 cb로 표현)
CriteriaBuilder cb = em.getCriteriaBuilder();

// 2. CreateQuery 생성 (이후 cq로 표현) 파라미터는 쿼리 결과 타입
CriteriaQuery<Review> cq = cb.createQuery(Review.class);

// 3. from 메소드로 from 절 엔티티 지정, Root 타입 이용해서 엔티티 속성 접근
Root<Review> root = cq.from(Review.class);

// 4. select 메소드로 select 대상 지정
cq.select(root);

// 5. where 메소드로 조건 지정, cb를 이용해서 predicate 생성
cq.where(cb.equal(root.get("hotelId"), "H-001"));

// 6. orderBy 메소드로 정렬 순서 지정, cb를 이용해서 Order 생성
cq.orderBy(cb.asc(root.get("id")));

// select r from Review r where r.hotelId = 'H-001'

// 7. cq를 이용해서 TypedQuery 생성
TypedQuery<Review> query = em.createQuery(cq);
query.setFirstResult(4); // 0부터 시작
query.setMaxResults(4);
List<Review> reviews = query.getResultList();
```
---

- 검색 조건 지정
- where 메소드에 검색 조건 전달
- 검색 조건은 cb를 이용해서 생성
- 예, 같음 조건은 equal 메소드로 생성
- 검색 조건에 사용할 엔티티 속성은 get 메서드로 구함
```text
Root<Review> root = cq.from(Review.class);

// 생성 조건 : Review의 hotelId가 'H-001'과 같음
Predicate predicate = cb.equal(root.get("hotelId"), "H-001");

cq.where(predicate);
```
- 조합
- and, or 메소드로 조건 조합
```text
Predicate p1 = cb.equal(root.get("hotelId"), "H-001");
Predicate p2 = cb.greaterThan(root.get("created"), LocalDateTime.now().minusDays(10));
Predicate predicate = cb.and(p1, p2);
cq.where(predicate);
```
---

- 정렬 순서
- orderBy 메서드로 정렬 지정
- asc, desc 메서드로 정렬 정보(Order) 생성
- 정렬 대상 속성은 get 메서드로 구함
```text
Order orderId = cb.asc(root.get("id"));
cq.orderBy(orderId);
```
- 한 개 이상 정렬 지정 가능
```text
cq.orderBy(
    cb.asc(root.get("hotelId")),
    cb.desc(root.get("id"))
);
```
---

- get 메서드와 제네릭 타입
- <Y> Path<Y> get(String attributeName)
- in 메소드 조건 생성할 때 타입 파라미터 지정하면 유용
```text
// mark 속성이 int 타입
CriteriaBuilder.In(Object> markCond = cb.in(root.get("mark"));
markCond.value(1).value("a"); // 런타임에 쿼리 생성 시점에 에러!
```
```text
// mark 속성이 int 타입
CriteriaBuilder.In(Object> markCond = cb.in(root.<Integer>get("mark"));
markCond.value(1).value("a"); // 컴파일 시점에 에러!
```
---

- Criteria 사용 이점
- 동적인 검색 조건 지정 가능
```text
Predicate p = cb.conjunction();

if (hotelId != null) {
    p = cb.and(p, cb.equal(root.get("hotelId"), hotelId));
}

p = cb.and(p, cb.greaterThan(root.get("created"), LocalDateTime.now().minusDays(10)));
if (mark >= 0) {
    p = cb.and(p, cb.ge(root.get("mark"), mark));
}

cq.where(p);
```
---

- 그 외
- 집합 함수
  - count, max, min, avg, sum
- group by, having
- 콜렉션 관련 비교
  - member of, not member of, is empty, is not empty
  - exist, all, any
- jpql 함수
  - concat, substring, trim, abs, sqrt
  - 콜렉션 함수 : size, index 등
---

- 다음 경우는 jpql 말고 일반 쿼리 사용 고려
- 여러 테이블 조인
  - 레거시 테이블 조인
- dbms에 특화된 쿼리 필요
  - 예, 오라클 힌트
- 서브 쿼리 필요
- 통계, 대량 데이터 조회 / 처리
- 굳이 연관 + 쿼리를 사용하고 싶다면 n+1 문제, fetch 조인 추가 학습

### jpa flush()
- jpa로 엔티티를 저장할 경우 커밋 이전까지 실제 db에 저장이 되지않아 데이터 조회시 데이터가 없어 오류 발생
- 스프링의 경우 @Transactional이 붙은 메서드 실행 종료 시점에 커밋
- 즉 order() 메서드 실행 종료 시점에서 커밋
- jpa도 order() 메서드 실행 종료 시점에 insert 쿼리 실행
- db 프로시저를 실행하는 시점은 insert 쿼리 실행 이전임
```text
@Transactional
public void order(OrderRequest req) {
        ...
        Order order = ...;
        reqository.save(order);
        callPostOrderProcedure(order.getId()); // 프로시저에서 데이터 조회 실패
}
```
---

- flush() 메소드는 영속 컨텍스트 데이터를 미리 반영
- jpa와 sql을 함께 사용할 때 필요
- 조회전 flush() 메소드를 호출하여 앞선 내용들을 미리 db에 반영하여 오류 해결
```text
@Transactional
public void order(OrderRequest req) {
        ...
        Order order = ...;
        reqository.save(order);
        reqository.flush(); // insert 쿼리 실행
        callPostOrderProcedure(order.getId()); // select 쿼리에서 조회됨
}
```