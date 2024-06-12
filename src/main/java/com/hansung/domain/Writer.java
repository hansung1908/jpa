package com.hansung.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "writer")
@SecondaryTables({
        @SecondaryTable(name = "writer_address",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "writer_id", // writer_address 테이블 칼럼
                        referencedColumnName = "id") // writer 테이블 칼럼
        ),
        @SecondaryTable(name = "writer_intro", // writer_intro 이름을 가진 테이블 같이 사용한다고 지정
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "writer_id", // writer_intro 테이블 칼럼
                        referencedColumnName = "id") // writer 테이블 칼럼
        )}
)
public class Writer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    @AttributeOverrides({ // address 클래스는 여러 곳에서 사용될 수 있어서 테이블 이름 지정이 힘듬, 그래서 writer에서 컬럼 지정시 테이블 이름도 같이 지정
            @AttributeOverride(name = "address1", column = @Column(table = "writer_address", name = "addr1")),
            @AttributeOverride(name = "address2", column = @Column(table = "writer_address", name = "addr2")),
            @AttributeOverride(name = "zipcode", column = @Column(table = "writer_address"))
    })
    private Address address;

    @Embedded
    private Intro intro;

    protected Writer() {
    }

    public Writer(String name, Address address, Intro intro) {
        this.name = name;
        this.address = address;
        this.intro = intro;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public Intro getIntro() {
        return intro;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Writer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", intro=" + intro +
                '}';
    }
}

