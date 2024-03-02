package pt.peralta.shareYourDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import pt.peralta.shareYourDemo.entity.user.User;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByLogin(String login);
}
