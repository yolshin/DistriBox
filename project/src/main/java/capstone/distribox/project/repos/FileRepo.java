package capstone.distribox.project.repos;

import capstone.distribox.project.dtos.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<File, Long> {
}
