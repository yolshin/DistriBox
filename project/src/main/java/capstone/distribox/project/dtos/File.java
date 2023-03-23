package capstone.distribox.project.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class File {
    @Id
    private Long id;
}
