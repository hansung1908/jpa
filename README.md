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
- 필드 접근 : 필드 값을 사용하여 매핑
- @Id 어노테이션을 필드에 붙히면 필드 접근
---
- 프로퍼티 접근 : getter / setter 메서드를 사용해서 매핑
- @Id 어노테이션을 getter 메서드에 붙히면 프로퍼티 접근
---
- @Access 어노테이션을 사용해서 명시적으로 지정
- @Access(AccessType.PROPERTY), @Access(AccessType.FILED)
- 불필요한 setter 메서드를 만들 필요가 없는 필드 접근 선호