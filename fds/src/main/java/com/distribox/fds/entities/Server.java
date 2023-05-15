package com.distribox.fds.entities;

import com.fasterxml.jackson.annotation.*;
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
@JsonIgnoreProperties(value = {"files"},allowGetters = true)
public class Server {

	public Server() {
	}


	public enum State {
		OPEN, BUSY, OFFLINE
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	public void setId(UUID id) {
		this.id = id;
	}


	public UUID getId() {
		return id;
	}

	public State state = State.OPEN;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="s_f",
			joinColumns = {@JoinColumn(name="id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name="fileid", referencedColumnName = "fileid")})
	private Set<File> files = new HashSet<>();

	@JsonIdentityInfo(
			generator = ObjectIdGenerators.PropertyGenerator.class,
			property = "fileid")
	@JsonIdentityReference(alwaysAsId=true)
	@JsonProperty("fileids")
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