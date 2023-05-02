package com.distribox.fds.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="files")
//Prevents recursive reference with server
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "fileid")
public class File {
    @Id
    @SequenceGenerator(
        name = "file_sequence",
        sequenceName = "file_sequence",
        allocationSize = 1
    )
    @GeneratedValue
    public UUID fileid;
    public String filepath;

    public File (String filepath, String userid) {
        this(filepath, new User(userid));
    }

    public File(String filepath, User user) {
        this.filepath = filepath;
        this.user = user;
    }

    public File() {

    }


    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "files")
    public Set<Server> servers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="userid")
    public User user;

    @Override
    public String toString() {
        return "File [" + fileid + ", " + filepath + ", " + user.userid + "]";
    }

    public void addServer(Server server) {
        if (servers.add(server)) {
            server.addFile(this);
        }
    }

    public void addServers(Set<Server> servers) {
        for (Server server: servers) {
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
        for (Server server: servers) {
            removeServer(server);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        return Objects.equals(fileid, file.fileid);
    }

    @Override
    public int hashCode() {
        return fileid != null ? fileid.hashCode() : 0;
    }
}
