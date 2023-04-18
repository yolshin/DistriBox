package com.distribox.fds.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "server")
public class Server {

	public enum State {
		WAITING, BUSY, OFFLINE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private UUID id;

	public State state = State.WAITING;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "server")
	public List<File> files;

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