package hu.webuni.hr.gyd.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigProperties {
	
	private Employee employee = new Employee(); 
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public static class Employee {
		private Default def = new Default();
		private Smart smart = new Smart();
		
		public Default getDef() {
			return def;
		}
		public void setDef(Default def) {
			this.def = def;
		}
		public Smart getSmart() {
			return smart;
		}
		public void setSmart(Smart smart) {
			this.smart = smart;
		}		
	}
	
	public static class Default {
		private int percent;

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}		
	}
	
	public static class Smart {
		private SmartDefault smartDefault = new SmartDefault();
		private Junior junior = new Junior();
		private Medior medior = new Medior();
		private Senior senior = new Senior();
		
		public SmartDefault getSmartDefault() {
			return smartDefault;
		}
		public void setSmartDefault(SmartDefault smartDefault) {
			this.smartDefault = smartDefault;
		}
		public Junior getJunior() {
			return junior;
		}
		public void setJunior(Junior junior) {
			this.junior = junior;
		}
		public Medior getMedior() {
			return medior;
		}
		public void setMedior(Medior medior) {
			this.medior = medior;
		}
		public Senior getSenior() {
			return senior;
		}
		public void setSenior(Senior senior) {
			this.senior = senior;
		}		
	}
	
	public static class SmartDefault {
		private int percent;

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}		
	}
	
	public static class Junior {
		private double limit;
		private int percent;
		
		public double getLimit() {
			return limit;
		}
		public void setLimit(double limit) {
			this.limit = limit;
		}
		public int getPercent() {
			return percent;
		}
		public void setPercent(int percent) {
			this.percent = percent;
		}		
	}
	
	public static class Medior {
		private double limit;
		private int percent;
		
		public double getLimit() {
			return limit;
		}
		public void setLimit(double limit) {
			this.limit = limit;
		}
		public int getPercent() {
			return percent;
		}
		public void setPercent(int percent) {
			this.percent = percent;
		}		
	}
	
	public static class Senior {
		private double limit;
		private int percent;
		
		public double getLimit() {
			return limit;
		}
		public void setLimit(double limit) {
			this.limit = limit;
		}
		public int getPercent() {
			return percent;
		}
		public void setPercent(int percent) {
			this.percent = percent;
		}		
	}
}
