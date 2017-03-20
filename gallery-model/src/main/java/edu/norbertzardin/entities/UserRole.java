package edu.norbertzardin.entities;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "NORBERT_USER_ROLES",
        uniqueConstraints = @UniqueConstraint(
                columnNames = { "ROLE", "USERNAME" }))
public class UserRole{

    private Integer userRoleId;
    private UserEntity user;
    private String role;

    public UserRole() {
    }

    public UserRole(UserEntity user, String role) {
        this.user = user;
        this.role = role;
    }

    @Id
    @SequenceGenerator(name = "roleIdSeq", sequenceName = "NORBERT_ROLE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleIdSeq")
    @Column(name = "ROLE_ID",
            unique = true, nullable = false)
    public Integer getUserRoleId() {
        return this.userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME", nullable = false)
    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Column(name = "ROLE", nullable = false, length = 45)
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}