package model;

import java.util.List;

import org.joda.time.DateTime;

/**
 * @author saimir
 *
 */
public abstract class LogEntry {
	protected String commitID;
	protected String author;
	protected DateTime date;
	protected String comment;
	protected List<Change> changeList; //changed, modified, added, etc

	public String getCommitID() {
		return commitID;
	}

	public void setCommitID(String startingToken) {
		this.commitID = startingToken;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public List<Change> getChangeList() {
		return changeList;
	}

	public void setChangeList(List<Change> changeList) {
		this.changeList = changeList;
	}

	@Override
   public String toString() {
	   return "LogEntry [startingToken=" + commitID + ", author=" + author
	         + ", date=" + date + ", comment=" + comment + ", changeList="
	         + changeList + "]";
   }
}
