package pt.peralta.shareYourDemo.exceptions;

public class PublicationAlreadyVotedException extends RuntimeException{
    public PublicationAlreadyVotedException() {
        super("You already Voted in this publication");
    }
}
