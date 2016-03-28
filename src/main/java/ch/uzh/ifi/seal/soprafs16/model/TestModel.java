package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class TestModel implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private int age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }
}
