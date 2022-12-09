package ecocampus;

/**
 * Status of an issue.
 */
public enum Status {
    UNRESOLVED("unresolved"),
    IN_PROGRESS("in progress"),
    RESOLVED("resolved");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    /**
     * @return the status of the issue
     */
    public String getStatus() {
        return status;
    }
}
