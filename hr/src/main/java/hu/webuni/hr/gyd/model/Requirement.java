package hu.webuni.hr.gyd.model;

public enum Requirement {
	
	NINCS(""),
	ERETTSEGI("erettsegi"),
	FOISKOLA("foiskola"),
	EGYETEM("egyetem"),
	PHD("phd");
	
	public final String name;
	
	private Requirement(String name) {
		this.name = name;
	}

}
