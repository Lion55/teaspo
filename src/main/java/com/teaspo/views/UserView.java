package com.teaspo.views;

import com.teaspo.persistence.entities.UserEntity;
import com.teaspo.pojo.enams.RolesEnum;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Andrii on 15.11.2016.
 */
public class UserView {

    public static final int MAX_EMAIL_SIZE = 45;
    public static final int MAX_PASS_SIZE = 45;
    public static final int MIN_PASS_SIZE = 8;

    private int id;

    @NotNull(message = "error.user.email.require")
    @Size(max = MAX_EMAIL_SIZE, message = "error.user.email.max.size")
    @Email(message = "error.user.email.format")
    private String email;

    @NotNull(message = "error.user.pass.require")
    @Size(min= MIN_PASS_SIZE,max = MAX_EMAIL_SIZE, message = "error.user.pass.size")
    private String password;

    private String nikname;

    private Boolean active;

    private RolesEnum role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNikname() {
        return nikname;
    }

    public void setNikname(String nikname) {
        this.nikname = nikname;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public RolesEnum getRole() {
        return role;
    }

    public void setRole(RolesEnum role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserView userView = (UserView) o;

        if (id != userView.id) return false;
        if (email != null ? !email.equals(userView.email) : userView.email != null) return false;
        if (password != null ? !password.equals(userView.password) : userView.password != null) return false;
        if (nikname != null ? !nikname.equals(userView.nikname) : userView.nikname != null) return false;
        if (active != null ? !active.equals(userView.active) : userView.active != null) return false;
        return role == userView.role;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (nikname != null ? nikname.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserView{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nikname='" + nikname + '\'' +
                ", active=" + active +
                ", role=" + role +
                '}';
    }


}
