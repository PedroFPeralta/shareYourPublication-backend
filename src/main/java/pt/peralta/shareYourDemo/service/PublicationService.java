package pt.peralta.shareYourDemo.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pt.peralta.shareYourDemo.entity.Publication;
import pt.peralta.shareYourDemo.entity.PublicationDTO;
import pt.peralta.shareYourDemo.repository.PublicationRepository;

import java.util.List;

@Service
public class PublicationService {

    private PublicationRepository repository;

    public PublicationService(PublicationRepository repository) {
        this.repository = repository;
    }

    public List<Publication> listAll(){
        return repository.findAll() ;
    }

    public Publication create(PublicationDTO publicationDTO){
        Publication publication = new Publication(publicationDTO);
        repository.save(publication);
        return publication;
    }

    public Publication update(Long id, PublicationDTO publicationDTO) throws Exception {
        Publication publication = findById(id);

        publication.setTitle(publicationDTO.title());
        publication.setDescription(publicationDTO.description());

        return repository.save(publication);
    }

    public Publication delete(Long id) throws Exception {
        Publication publication = findById(id);

        repository.delete(publication);

        return publication;
    }

    public Publication findById(Long id) throws Exception {
        Publication publication = repository.findById(id).orElseThrow(Exception::new);
        return publication;
    }


}
