package org.fms.training.repository;

import org.fms.training.common.entity.Trainer;
import org.fms.training.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    @Query(value = "SELECT t.* FROM Trainer t " +
            "JOIN Fms_User u ON t.User_Id = u.User_Id " +
            "JOIN Fms_User_Role ur ON u.User_Id = ur.User_Id " +
            "JOIN Role r ON ur.Role_Id = r.Role_Id " +
            "WHERE u.Status = 'Active' " +
            "AND r.Role_Name = 'TRAINER'", nativeQuery = true)
    List<Trainer> getActiveTrainers();

    @Query(value = "SELECT t.* FROM Trainer t " +
            "JOIN Fms_User u ON t.User_Id = u.User_Id ORDER BY u.Account", nativeQuery = true)
    List<Trainer> getAllByOrderByUserAccountAsc();

    @Query("SELECT u FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE r.roleName = 'TRAINER' " +
            "AND u.id NOT IN (SELECT t.user.id FROM Trainer t) ORDER BY u.account")
    List<User> getAllUserWithTrainerRoleExcludingHavingTrainerId();

    boolean existsByPhone(String phone);

    boolean existsByUserId(Integer userId);
}