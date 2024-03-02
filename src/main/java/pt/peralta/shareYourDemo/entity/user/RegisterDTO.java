package pt.peralta.shareYourDemo.entity.user;

public record RegisterDTO(String login, String password, UserRole role) {
}
