package ecocampus.gui;

import ecocampus.User;
import ecocampus.UserSerializer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a JavaFx Bean with the proprieties linked to the waypoints their corresponding route
 */
public final class Bean {
    private  ObservableList<Waypoint> waypoints;
    private ObjectProperty<Boolean> draw = new SimpleObjectProperty<>(true);
    private ObservableList<User> users;

    /**
     *
     * @return
     */
    public int getSelectedMode() {
        return SelectedMode;
    }

    public void setSelectedMode(int selectedMode) {
        SelectedMode = selectedMode;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int id) {
        currentUserId = id;
    }

    public String getCurrentUserName() {
        return users.get(getCurrentUserId()).getName();
    }

    private int SelectedMode = 0;
    // 0 = Show all, 1 = Show Resulted, 2 = Show unresulted

    private int currentUserId = 0;

    public ObjectProperty<Boolean> drawProperty() {
        return draw;
    }


    public ObjectProperty<Issue> getObservedissue() {
        return observedissue;
    }
    public ObservableList<User> getUsers() {
        return users;
    }

    public ObservableList<String> getUserNames() {

        return FXCollections.observableArrayList(users.stream().map(u -> u.getName()).toList());
    }

    public boolean isUserAdmin() {
        return users.get(getCurrentUserId()).isAdmin();
    }

    private final ObjectProperty<Issue> observedissue;

    /**
     * Constructor
     */
    public Bean(ObjectProperty<Issue> issue){
        waypoints = FXCollections.observableArrayList();
        try {
            waypoints = WaypointsSerializer.loadWaypoints();
            users = UserSerializer.loadUsers();

        } catch (Exception e){
            System.out.println(e);
        }
        observedissue = issue;

        /*
        users = FXCollections.observableArrayList();
        users.add(new User(0, "Martin Vetterli", true,new ArrayList<Integer>()));
        users.add(new User(1, "Quentin", false,new ArrayList<Integer>()));
        users.add(new User(2, "Paul", true,new ArrayList<Integer>()));
        */

    }

    /**
     * Return the number of upvotes of the issue with the most upvotes
     * @return the max number of upvotes
     */
    public int getMaxUpvotes(){
        int max = -1;
        for (Waypoint waypoint : waypoints) {
            max = Math.max(waypoint.issue().getUpVotes(),max);
        }
        return max;
    }


    /**
     * Return the list of waypoints
     * @return the list of waypoints
     */
    public ObservableList<Waypoint> getWaypoints(){return waypoints;}

    /**
     * Remove the last waypoints
     */
    public void removeLastWaypoint() {
        waypoints.remove(waypoints.size() - 1);
    }

    /**
     * check if the current issue is submitted (empty or not)
     * @return true if the current issue is submitted
     */
    public boolean noObservedIssue() {
        return observedissue.get().isEmpty();
    }
}
