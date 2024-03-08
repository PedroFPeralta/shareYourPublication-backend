package pt.peralta.shareYourDemo.entity.publication;

import pt.peralta.shareYourDemo.entity.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public record PublicationDetailsDTO(Long id, String title, String description, LocalDateTime timestamp, LocalDateTime recordTimestamp, UserDTO createdBy, List<String> pictures) {
}
