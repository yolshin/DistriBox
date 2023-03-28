package capstone.distribox.project.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;

@Entity
@Data
public class FileData {
    @Id
    private Long id;
    private String path;
    private String location1;
    private String location2;
    private String location3;

}
