package base;

public class TextNote extends Note {
	
	String content;

	public TextNote(String title) {
		super(title);
	}
	
	public TextNote(String title, String content) {
		this(title);
		this.content = content;
	}
	
	@Override
	boolean matchKeyword(String keyword) {
		return getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
				content.toLowerCase().contains(keyword.toLowerCase());
	}
}
