package com.example.demo.entities;

import com.example.demo.Position;
import io.ebean.annotation.Formula;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "test_position")
@ToString(of = "distance")
public class TestPosition {

    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Formula(select = "ST_X(position)")
    private Double positionX;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Formula(select = "ST_Y(position)")
    private Double positionY;

    @Transient
    private Double distance;

    public Position getPosition() {
        return new Position(positionX, positionY);
    }
}
