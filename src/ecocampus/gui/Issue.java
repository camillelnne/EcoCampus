package ecocampus.gui;

import com.fasterxml.jackson.annotation.JsonProperty;
import ecocampus.Status;

public class Issue {
    private String title;
    private String description;
    private final int author;
    private final String date;
    private Status status;
    private int upVotes;
    private final int id;
    private Boolean empty;
    private String location;

    /**
     * Constructor
     * @param title title of the issue
     * @param description description of the issue
     * @param author author of the issue
     * @param id id of the issue
     */
    public Issue(@JsonProperty("title") String title, @JsonProperty("description") String description,
                 @JsonProperty("author") int author, @JsonProperty("id") int id, @JsonProperty("location") String location) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.location = location;
        // init date by current date
        this.date = java.time.LocalDate.now().toString();
        this.status = Status.UNRESOLVED;
        this.upVotes = 0;
        this.id = id;
        empty = true;
    }

    public void setEverything(String title, String description, String location ){
        this.title = title;
        this.description = description;
        this.location = location;
        empty = false;
    }
    /**
     * @return the title of the issue
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the location of the issue
     */
    public String getLocation(){ return location; }
    public Boolean isEmpty(){
        return empty;
    }
    /**
     * @return the description of the issue
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the author of the issue
     */
    public int getAuthor() {
        return author;
    }

    /**
     * @return the date of the issue
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the status of the issue
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return the priority of the issue
     */
    public int getUpVotes() {
        return upVotes;
    }

    /**
     * @return the id of the issue
     */
    public int getId() {
        return id;
    }

    /**
     * set status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * set upvotes
     */
    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    /**
     * add upvotes
     */
    public void addUpvotes() {
        this.upVotes++;
    }

    @Override
    public String toString() {
        return "Issue " +status.toString()+" "+ title+ " " + id;
    }
}
