package util;

public class ExtendTagLibrary extends TagLibrary {
	public ExtendTagLibrary() 
	{
		this.insertTag("border", "tags.Border");
		this.insertTag("spacearea", "tags.SpaceArea");
		this.insertTag("tab", "tags.Tab");
		this.insertTag("th", "tags.Column");
	}

}
