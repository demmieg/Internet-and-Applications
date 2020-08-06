package model;

public class Affiliation {
	private String laboratory;
	private String institution;
	private Location location;

	public String getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(String value) {
		this.laboratory = value;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String value) {
		this.institution = value;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location value) {
		this.location = value;
	}
}
