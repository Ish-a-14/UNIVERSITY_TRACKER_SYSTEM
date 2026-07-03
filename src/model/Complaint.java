package model;

public class Complaint {

    private int id;
    private int userId;
    private String category;
    private String description;
    private String priority;
    private String status;
    private String userName;
    private String department;
    private String date;

    public Complaint() {}

    public Complaint(int id, int userId, String category, String description, String priority, String status) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDate() { return date; } 

    public void setDate(String date) { this.date = date; }
}