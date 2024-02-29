package pt.peralta.shareYourDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.peralta.shareYourDemo.entity.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
}
