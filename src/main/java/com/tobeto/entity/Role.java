package com.tobeto.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
@Data
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "role_name")
	private String roleName;
	@ManyToOne
	private User user;
}