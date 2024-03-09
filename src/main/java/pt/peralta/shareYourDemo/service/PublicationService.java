package pt.peralta.shareYourDemo.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pt.peralta.shareYourDemo.entity.publication.Publication;
import pt.peralta.shareYourDemo.entity.publication.PublicationDTO;
import pt.peralta.shareYourDemo.entity.publication.PublicationDetailsDTO;
import pt.peralta.shareYourDemo.entity.publication.ReviewDTO;
import pt.peralta.shareYourDemo.entity.user.User;
import pt.peralta.shareYourDemo.entity.user.UserDTO;
import pt.peralta.shareYourDemo.exceptions.InvalidReviewValueException;
import pt.peralta.shareYourDemo.exceptions.NoMorePublicationException;
import pt.peralta.shareYourDemo.exceptions.PublicationAlreadyVotedException;
import pt.peralta.shareYourDemo.repository.PublicationRepository;
import pt.peralta.shareYourDemo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PublicationService {
    @Value("${publication.page.size}")
    int pageSize;

    private PublicationRepository repository;
    private UserRepository userRepository;

    public PublicationService(PublicationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<PublicationDetailsDTO> listAll(int pageNumber) throws NoMorePublicationException {
        List<PublicationDetailsDTO> publicationList = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        Page<Publication> page = repository.findAll(pageable);

        if (page.getContent().size()==0) throw new NoMorePublicationException("Não existem mais publicações");

        page.getContent().forEach(publication -> publicationList.add(
                new PublicationDetailsDTO(
                        publication.getId(),
                        publication.getTitle(),
                        publication.getDescription(),
                        publication.getLocation(),
                        publication.getTimestamp(),
                        publication.getRecordTimestamp(),
                        getPublicationUser(publication),
                        publicationPicturesToList(publication),
                        publication.getType().toString(),
                        publicationReviewsAVG(publication)
                )));

        return publicationList;
    }

    public PublicationDetailsDTO getPublication(Long id){
        Publication publication = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        return new PublicationDetailsDTO(
                publication.getId(),
                publication.getTitle(),
                publication.getDescription(),
                publication.getLocation(),
                publication.getTimestamp(),
                publication.getRecordTimestamp(),
                getPublicationUser(publication),
                publicationPicturesToList(publication),
                publication.getType().toString(),
                publicationReviewsAVG(publication)
        );
    }

    private UserDTO getPublicationUser(Publication publication){
        User user = (User) userRepository.findByLogin(publication.getCreatedBy());
        return new UserDTO(user.getLogin(),user.getContact());
    }

    private List<String> publicationPicturesToList(Publication publication){
        if (publication.getPictures().isEmpty())
            return null;
        return Arrays.stream(publication.getPictures().split(",")).toList();
    }

    private Double publicationReviewsAVG(Publication publication){
        if (publication.getReviews().isEmpty())
            return null;

        //This list have (reviewValue-user)
        List<String> reviews = Arrays.stream(publication.getReviews().split(",")).toList();

        double reviewsSUM = reviews.stream().map(review -> Arrays.stream(review.split("-")).toList()).mapToInt(aux -> Integer.valueOf(aux.get(0))).sum();

        return reviewsSUM / reviews.size();
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
        publication.setLocation(publicationDTO.location());

        return repository.save(publication);
    }

    private boolean ownPublication(Publication publication) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) return false;

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

    public Publication addReviewToPublication(Long id, ReviewDTO value) {
        int reviewValue = value.reviewValue();
        if (reviewValue < 0 || reviewValue > 5) throw new InvalidReviewValueException("Review value needs to be between 0 and 5");

        Publication publication = findById(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) throw new SecurityException("You need to be logged to add a review");

        if (publication.getReviews().contains(user.getLogin())) throw new PublicationAlreadyVotedException();

        publication.setReviews(publication.getReviews().concat(String.valueOf(reviewValue)).concat("-").concat(user.getLogin()).concat(","));

        repository.save(publication);

        return publication;
    }

    public Publication delete(Long id) {
        Publication publication = findById(id);

        if(!ownPublication(publication)) throw new SecurityException("Dont have authorization to delete this publication");

        repository.delete(publication);

        return publication;
    }

    public Publication findById(Long id) {
        Publication publication = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return publication;
    }


}
