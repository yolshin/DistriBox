package capstone.distribox.project.repos;

import capstone.distribox.project.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilesRepository extends JpaRepository<File, String> {
	Optional<File> findById(String id);
}
