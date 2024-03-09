package pt.peralta.shareYourDemo.entity.publication;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "publication")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private LocalDateTime timestamp;
    private LocalDateTime recordTimestamp;
    private String createdBy;
    //The list of pictures will be saved via String (example: "picture1.png,picture2.png,...")
    private String pictures;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private PublicationType type;
    //Will be a String with reviews with scale [0;5] separeted by ','
    private String reviews;

    public Publication(PublicationDTO publicationDTO) {
        this.title = publicationDTO.title();
        this.description = publicationDTO.description();
        this.timestamp = LocalDateTime.now();
        this.recordTimestamp = this.timestamp;
        this.location = publicationDTO.location();
        this.pictures = "";
        if (publicationDTO.type().equalsIgnoreCase(PublicationType.REQUESTING.toString()))
            this.type = PublicationType.REQUESTING;
        else if (publicationDTO.type().equalsIgnoreCase(PublicationType.OFFERING.toString()))
            this.type = PublicationType.OFFERING;
        this.reviews = "";
    }
}
