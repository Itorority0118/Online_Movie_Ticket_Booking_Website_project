package model;

public class News {
	
	private int id;
    private String title;
    private String date;
    private String image;
    private String content;
    
	public News(int id, String title, String date, String image, String content) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
		this.image = image;
		this.content = content;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
    
}
