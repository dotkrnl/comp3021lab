package base;

import java.util.Date;

public abstract class Note implements Comparable<Note>, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Date date;
	private String title;

	public Note(String title) {
		this.title = title;
		date = new Date(System.currentTimeMillis());
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// Remove per lab2.pdf requirement
		//if (getClass() != obj.getClass())
		// 	return false;
		Note other = (Note) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public int compareTo(Note o) {
		// For class Note, we compare it based on its creation date,
		// note created more recently is considered as smaller in this lab.
		// So use minus sign here.
		return -date.compareTo(o.date);
	}
	
	@Override
	public String toString() {
		return date.toString() + "\t" + title;
	}

	abstract boolean matchKeyword(String keyword);
}
