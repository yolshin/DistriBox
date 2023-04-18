package com.distribox.fds.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    public File (String filepath, Server server, String userid) {
        this.filepath = filepath;
        this.server = server;
        this.user = new User(userid);
    }

    public File() {

    }

    @ManyToOne
    @JoinColumn(name="userid")
    public User user;

    @ManyToOne
    @JoinColumn(name="id")
    public Server server;

    @Override
    public String toString() {
        return "File [" + fileid + ", " + filepath + ", " + user.userid + "]";
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
