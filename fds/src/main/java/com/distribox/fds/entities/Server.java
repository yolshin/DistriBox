package com.distribox.fds.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLUpdate;

import java.util.*;

//todo: add last-seen
//"busy"" if more than 10 seconds since thing
//can represnt with query - if lower than


@Getter
@Setter
@Entity
@Table(name = "servers")
//Prevents recursive reference with file
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Server {

	public Server() {
//		lastSeen = System.currentTimeMillis();
//		this.files = new HashSet<>();
	}


	public enum State {
		OPEN, BUSY, OFFLINE
	}


	@Id
//	@SequenceGenerator(
//			name = "server_sequence",
//			sequenceName = "server_sequence",
//			allocationSize = 1
//	)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "serverid", nullable = false)
	private UUID id;

	public void setId(UUID id) {
		this.id = id;
	}


	public UUID getId() {
		return id;
	}

	public State state = State.OPEN;

	@ManyToMany
	@JoinTable(name="s_f",
			joinColumns = {@JoinColumn(name="serverid", referencedColumnName = "serverid")},
			inverseJoinColumns = {@JoinColumn(name="fileid", referencedColumnName = "fileid")})
	private Set<File> files = new HashSet<>();


	public String url;
	public Long lastSeen;

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
		return id.toString();
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