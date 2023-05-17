package com.distribox.fds.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

//"busy"" if more than 10 seconds since thing
//can represnt with query - if lower than


@Getter
@Setter
@Entity
@Table(name = "servers")
//Prevents recursive reference with file
@JsonIgnoreProperties(value = {"files"},allowGetters = true)
public class Server {

	//TODO: Remove URL from Server
	public Server() {
		this.id = UUID.randomUUID().toString();
	}
	public Server(String id) {
		this.id = id;
	}

	public enum State {
		OPEN, BUSY, OFFLINE
	}

	@Id
	@SequenceGenerator(
			name = "server_sequence",
			sequenceName = "server_sequence",
			allocationSize = 1
	)
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public State state = State.OPEN;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="s_f",
			joinColumns = {@JoinColumn(name="id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name="filepath", referencedColumnName = "filepath")})
	private Set<File> files = new HashSet<>();

	@JsonIdentityInfo(
			generator = ObjectIdGenerators.PropertyGenerator.class,
			property = "filepath")
	@JsonIdentityReference(alwaysAsId=true)
	@JsonProperty("filepaths")
	public Set<File> getFiles() {
		return files;
	}


	public String url;
	private Long lastSeen = System.currentTimeMillis();

	Long updateLastSeen() {
		lastSeen = System.currentTimeMillis();
		return lastSeen;
	}



	public void addFile(File file) {
		if (!files.contains(file)) {
			files.add(file);
			file.addServer(this);
		}
	}

	public void removeFile(File file) {
		if (files.contains(file)) {
			files.remove(file);
			file.removeServer(this);
		}
	}

	@Override
	public String toString() {
		return "Server{" +
				"id=" + id +
				", state=" + state +
				", url='" + url + '\'' +
				", lastSeen=" + lastSeen +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Server server = (Server) o;

		return id.equals(server.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}