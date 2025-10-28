package model;

public class User {
	
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String role;       // "user" or "admin"
    private String status;     // "active" or "banned"
    private String createdAt;
    
    // Empty constructor
	public User() {}

	// Constructor
	public User(int userId, String fullName, String email, String password, 
			String phone, String role, String status, String createdAt) {
		this.userId = userId;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
	}

	// Getters
	public int getUserId() { return userId; }
	public String getFullName() { return fullName; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }
	public String getPhone() { return phone; }
	public String getRole() { return role; }
	public String getStatus() { return status; }
	public String getCreatedAt() { return createdAt; }

	// Setters
	public void setUserId(int userId) { this.userId = userId; }
	public void setFullName(String fullName) { this.fullName = fullName; }
	public void setEmail(String email) { this.email = email; }
	public void setPassword(String password) { this.password = password; }
	public void setPhone(String phone) { this.phone = phone; }
	public void setRole(String role) { this.role = role; }
	public void setStatus(String status) { this.status = status; }
	public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}