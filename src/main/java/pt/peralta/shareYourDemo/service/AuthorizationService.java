package pt.peralta.shareYourDemo.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.peralta.shareYourDemo.entity.user.ContactDTO;
import pt.peralta.shareYourDemo.entity.user.User;
import pt.peralta.shareYourDemo.repository.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    private UserRepository repository;

    public AuthorizationService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }

    public UserDetails updateUserContact(ContactDTO contact){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userToUpdate = (User)repository.findByLogin(user.getLogin());
        userToUpdate.setContact(contact.contact());

        return repository.save(userToUpdate);
    }
}
