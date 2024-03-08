package pt.peralta.shareYourDemo.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pt.peralta.shareYourDemo.entity.publication.Publication;
import pt.peralta.shareYourDemo.entity.publication.PublicationDTO;
import pt.peralta.shareYourDemo.entity.publication.PublicationDetailsDTO;
import pt.peralta.shareYourDemo.entity.user.User;
import pt.peralta.shareYourDemo.entity.user.UserDTO;
import pt.peralta.shareYourDemo.repository.PublicationRepository;
import pt.peralta.shareYourDemo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PublicationService {

    private PublicationRepository repository;
    private UserRepository userRepository;

    public PublicationService(PublicationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<PublicationDetailsDTO> listAll(){
        List<PublicationDetailsDTO> publicationList = new ArrayList<>();
        repository.findAll().forEach(publication -> publicationList.add(
                new PublicationDetailsDTO(
                        publication.getId(),
                        publication.getTitle(),
                        publication.getDescription(),
                        publication.getTimestamp(),
                        publication.getRecordTimestamp(),
                        getPublicationUser(publication),
                        publicationPicturesToList(publication)
                )));

        return publicationList;

    }

    public PublicationDetailsDTO getPublication(Long id){
        Publication publication = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        return new PublicationDetailsDTO(
                publication.getId(),
                publication.getTitle(),
                publication.getDescription(),
                publication.getTimestamp(),
                publication.getRecordTimestamp(),
                getPublicationUser(publication),
                publicationPicturesToList(publication)
        );
    }

    private UserDTO getPublicationUser(Publication publication){
        User user = (User) userRepository.findByLogin(publication.getCreatedBy());
        return new UserDTO(user.getLogin(),user.getContact());
    }

    private List<String> publicationPicturesToList(Publication publication){
        return Arrays.stream(publication.getPictures().split(",")).toList();
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
        publication.setRecordTimestamp(LocalDateTime.now());

        return repository.save(publication);
    }

    private boolean ownPublication(Publication publication) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getLogin().equals(publication.getCreatedBy());
    }

    public Publication updateAddPictures(Long id, List<String> pictures){
        Publication publication = findById(id);

        if(!ownPublication(publication)) throw new SecurityException("Dont have authorization to Update this publication");

        pictures.forEach(picture -> publication.setPictures(publication.getPictures().concat(picture).concat(",")));
        publication.setRecordTimestamp(LocalDateTime.now());

        repository.save(publication);

        return publication;
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
