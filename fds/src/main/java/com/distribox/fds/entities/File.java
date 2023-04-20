package com.distribox.fds.entities;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="files")
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
        this.filepath = filepath;
        this.user = new User(userid);
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

    public void removeServer(Server server) {
        if (servers.contains(server)) {
            servers.remove(server);
            server.removeFile(this);
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
