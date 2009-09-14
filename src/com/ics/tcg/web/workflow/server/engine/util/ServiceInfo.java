package com.ics.tcg.web.workflow.server.engine.util;
import java.io.Serializable;

/**
 * The service info for search verify and evaluate.
 * @author Administrator
 *
 */
public class ServiceInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3736330799489627645L;

	/** Service id in the database. */
	private int sid;
	
	/** Service provider name. */
	private String businessName;
	
	/** The service name. */
	private String serviceName;
	
	/** The service url. */
	private String accessPoint;
	
	/** The service wsdl url. */
	private String overviewURL;
	
	/** The service qos url. */
	private String qosInfo;

	public ServiceInfo() {
		businessName = new String();
		serviceName = new String();
		accessPoint = new String();
		overviewURL = new String();
		qosInfo = new String();
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAccessPoint() {
		return accessPoint;
	}

	public void setAccessPoint(String accessPoint) {
		this.accessPoint = accessPoint;
	}

	public String getOverviewURL() {
		return overviewURL;
	}

	public void setOverviewURL(String overviewURL) {
		this.overviewURL = overviewURL;
	}

	public String getQosInfo() {
		return qosInfo;
	}

	public void setQosInfo(String qosInfo) {
		this.qosInfo = qosInfo;
	}
}
