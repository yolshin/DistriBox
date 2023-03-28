package capstone.distribox.project.repos;

import capstone.distribox.project.dtos.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDataRepo extends JpaRepository<File, Long> {
}
