package pt.peralta.shareYourDemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publication")
@Data
@NoArgsConstructor
public class Publication {

    @Id
    private Long id;
    private String title;
    private String description;

    public Publication(PublicationDTO publicationDTO) {
        this.title = publicationDTO.title();
        this.description = publicationDTO.description();
    }
}
