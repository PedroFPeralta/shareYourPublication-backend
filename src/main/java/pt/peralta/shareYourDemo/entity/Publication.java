package pt.peralta.shareYourDemo.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public Publication(PublicationDTO publicationDTO) {
        this.title = publicationDTO.title();
        this.description = publicationDTO.description();
    }
}
