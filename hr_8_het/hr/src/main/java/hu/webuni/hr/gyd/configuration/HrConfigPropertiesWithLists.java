package hu.webuni.hr.gyd.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigPropertiesWithLists {

	private List<Object> limits;
	private List<Object> percents;
	
	@Value("${hr.def}")
	private int def;
	
	@Value("${hr.smartDefault}")
	private int smartDefault;

	public List<Object> getLimits() {
		return limits;
	}

	public void setLimits(List<Object> limits) {
		this.limits = limits;
	}

	public List<Object> getPercents() {
		return percents;
	}

	public void setPercents(List<Object> percents) {
		this.percents = percents;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getSmartDefault() {
		return smartDefault;
	}

	public void setSmartDefault(int smartDefault) {
		this.smartDefault = smartDefault;
	}
	
}
