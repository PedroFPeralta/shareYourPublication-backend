package pt.peralta.shareYourDemo.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pt.peralta.shareYourDemo.entity.Publication;
import pt.peralta.shareYourDemo.entity.PublicationDTO;
import pt.peralta.shareYourDemo.entity.user.User;
import pt.peralta.shareYourDemo.repository.PublicationRepository;
import pt.peralta.shareYourDemo.repository.UserRepository;

import java.util.List;

@Service
public class PublicationService {

    private PublicationRepository repository;
    private UserRepository userRepository;

    public PublicationService(PublicationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Publication> listAll(){
        return repository.findAll() ;
    }

    public Publication create(PublicationDTO publicationDTO){
        Publication publication = new Publication(publicationDTO);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        publication.setCreatedBy(user.getLogin());

        repository.save(publication);
        return publication;
    }

    public Publication update(Long id, PublicationDTO publicationDTO) {
        Publication publication = findById(id);

        if(!ownPublication(publication)) throw new SecurityException("Dont have authorization to Update this publication");

        publication.setTitle(publicationDTO.title());
        publication.setDescription(publicationDTO.description());

        return repository.save(publication);
    }

    private boolean ownPublication(Publication publication) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getLogin().equals(publication.getCreatedBy());
    }

    public Publication delete(Long id) {
        Publication publication = findById(id);

        if(!ownPublication(publication)) throw new SecurityException("Dont have delete to Update this publication");

        repository.delete(publication);

        return publication;
    }

    public Publication findById(Long id) {
        Publication publication = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return publication;
    }


}
