package hu.webuni.hr.gyd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Position {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	private Requirement minRequirement;
	private int minSalary;
	
	@OneToOne
	private Company company;
	
	public Position() {}
	
	public Position(Long id, String name, Requirement minRequirement, int minSalary) {
		this.id = id;
		this.name = name;
		this.minRequirement = minRequirement;
		this.minSalary = minSalary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Requirement getMinRequirement() {
		return minRequirement;
	}

	public void setMinRequirement(Requirement minRequirement) {
		this.minRequirement = minRequirement;
	}

	public int getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(int minSalary) {
		this.minSalary = minSalary;
	}
	
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Position [id=" + id + ", name=" + name + ", minRequirement=" + minRequirement + ", minSalary="
				+ minSalary + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
