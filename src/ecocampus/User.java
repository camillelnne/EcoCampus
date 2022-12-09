package ecocampus;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * User class
 * Can either be an admin or a normal user
 */
public class User {
    private final int id;
    private final String name;
    private final boolean isAdmin;
    private ArrayList<Integer> upVotedPosts;


    public int getId() {
        return id;
    }

    public ArrayList<Integer> getUpVotedPosts() {
        return upVotedPosts;
    }

    public User(int id, String name,  boolean isAdmin,ArrayList<Integer> upVotedPosts) {
        this.id = id;
        this.name = name;
        this.upVotedPosts = upVotedPosts;
        this.isAdmin = isAdmin;

    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void addUpvote(int issueId){
        upVotedPosts.add(issueId);
    }
    public boolean hasAlreadyVoted(int issueId){
        return upVotedPosts.contains(issueId);
    }
}
