package com.derrickshimada.techchallenge;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class User {
    private @Id String username;
    private String name;
    private String email;

    public User() {}

	public User(String username, String name, String email) {
		this.username = username;
        this.name = name;
        this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
    }
    
    public String getEmail() {
		return email;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}