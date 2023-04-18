package com.distribox.fds.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="users")
public class User {

	@Id
	@SequenceGenerator(
			name = "user_sequence",
			sequenceName = "user_sequence",
			allocationSize = 1
	)
	public String userid;

	public User(String userID) {
		this.userid = userID;
	}

	/**
	 * Query all the user's files
	 */
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	public List<File> files;

	protected User() {}

	@Override
	public String toString() {
		return "User [" + userid + " files: " + files.toString() + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		return Objects.equals(userid, user.userid);
	}

	@Override
	public int hashCode() {
		return userid != null ? userid.hashCode() : 0;
	}
}
