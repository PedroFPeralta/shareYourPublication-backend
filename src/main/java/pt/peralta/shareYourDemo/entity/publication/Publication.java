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
    private String title;
    private String description;
    private LocalDateTime timestamp;
    private LocalDateTime recordTimestamp;
    private String createdBy;
    //The list of pictures will be saved via String (example: "picture1.png,picture2.png,...")
    private String pictures;

    public Publication(PublicationDTO publicationDTO) {
        this.title = publicationDTO.title();
        this.description = publicationDTO.description();
        this.timestamp = LocalDateTime.now();
        this.recordTimestamp = this.timestamp;
        this.pictures = "";
    }
}
