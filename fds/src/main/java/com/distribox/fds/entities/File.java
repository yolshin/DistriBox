package com.distribox.fds.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "files")
//Prevents recursive reference with server
@JsonIgnoreProperties(value = {"servers", "userid"},allowGetters = true)
public class File {

	@Id
	@SequenceGenerator(
			name = "file_sequence",
			sequenceName = "file_sequence",
			allocationSize = 1
	)
	public String filepath;

	public File(String filepath, String userid) {
		this(filepath, new User(userid));
	}

	public File(String filepath, User user) {
		this.filepath = filepath;
		this.user = user;
	}

	public File() {

	}


	@ManyToMany(mappedBy = "files", fetch = FetchType.EAGER)
	private Set<Server> servers = new HashSet<>();


	public Set<Server> getServers() {
		return servers;
	}

	private void setServers(Set<Server> servers) {
		this.servers = servers;
	}

	@ManyToOne
	@JoinColumn(name = "userid")
	public User user;

	@JsonIdentityInfo(
			generator = ObjectIdGenerators.PropertyGenerator.class,
			property = "userid")
	@JsonIdentityReference(alwaysAsId=true)
	@JsonProperty("userid")
	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "File [" + filepath + ", " + user.userid + "]";
	}

	public void addServer(Server server) {
		if (!servers.contains(server)) {
			servers.add(server);
			server.addFile(this);
		}
	}

	public void addServers(Set<Server> servers) {
		for (Server server : servers) {
			addServer(server);
		}
	}

	public void removeServer(Server server) {
		if (servers.contains(server)) {
			servers.remove(server);
			server.removeFile(this);
		}
	}

	public void removeServers(Set<Server> servers) {
		for (Server server : servers) {
			removeServer(server);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		File file = (File) o;

		return Objects.equals(filepath, file.filepath);
	}

	@Override
	public int hashCode() {
		return filepath != null ? filepath.hashCode() : 0;
	}
}
