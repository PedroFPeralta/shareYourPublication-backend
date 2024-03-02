package pt.peralta.shareYourDemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.peralta.shareYourDemo.entity.Publication;
import pt.peralta.shareYourDemo.entity.PublicationDTO;
import pt.peralta.shareYourDemo.service.PublicationService;

import java.util.List;

@Controller
@RequestMapping("publications")
public class PublicationController {

    private PublicationService service;

    public PublicationController(PublicationService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<List<Publication>> listAll(){
        return ResponseEntity.status(HttpStatus.OK).body(service.listAll());
    }

    @PostMapping("/")
    public ResponseEntity<Publication> create(@RequestBody PublicationDTO publicationDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(publicationDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publication> update(@PathVariable("id") Long id, @RequestBody PublicationDTO publicationDTO)   {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id,publicationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Publication> delete(@PathVariable("id") Long id)  {
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(id));
    }

}
