package com.distribox.fds.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "server")
public class Server {

	public enum State {
		OPEN, BUSY, OFFLINE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	public State state = State.OPEN;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "server_files",
			joinColumns = @JoinColumn(name = "server_id"),
			inverseJoinColumns = @JoinColumn(name = "file_id")
	)
	public Set<File> files = new HashSet<>();
	public String url;

	public void addFile(File file) {
		if (files.add(file)) {
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

		return Objects.equals(id, server.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}