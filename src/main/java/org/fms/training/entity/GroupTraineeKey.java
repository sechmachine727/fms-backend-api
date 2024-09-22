package org.fms.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GroupTraineeKey implements Serializable {
    @Column(name = "trainee_id")
    private Integer traineeId;

    @Column(name = "group_id")
    private Integer groupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupTraineeKey that = (GroupTraineeKey) o;
        return Objects.equals(traineeId, that.traineeId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, groupId);
    }
}
