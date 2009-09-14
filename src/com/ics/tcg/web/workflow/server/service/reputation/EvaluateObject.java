package com.ics.tcg.web.workflow.server.service.reputation;

import java.io.Serializable;
import java.util.Hashtable;

import com.ics.tcg.web.workflow.server.engine.util.ServiceList;

/**
 * The object for the display part.
 * 
 * @author jinny
 * 
 */
public class EvaluateObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5682288309641235679L;

	private int userid;

	private Hashtable<Integer, Integer> gIDandNum;

	private double[][] ranks;

	private int[] recommenders;

	private double[] candidates;

	private ServiceList initServiceList;

	private ServiceList serviceList;

	public EvaluateObject(int size) {
		gIDandNum = new Hashtable<Integer, Integer>();
		ranks = new double[10][50];
		recommenders = new int[10];
		candidates = new double[size];
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public Hashtable<Integer, Integer> getGIDandNum() {
		return gIDandNum;
	}

	public void setGIDandNum(Hashtable<Integer, Integer> dandNum) {
		gIDandNum = dandNum;
	}

	public double[][] getRanks() {
		return ranks;
	}

	public void setRanks(double[][] ranks) {
		this.ranks = ranks;
	}

	public int[] getRecommenders() {
		return recommenders;
	}

	public void setRecommenders(int[] recommenders) {
		this.recommenders = recommenders;
	}

	public double[] getCandidates() {
		return candidates;
	}

	public void setCandidates(double[] candidates) {
		this.candidates = candidates;
	}

	public ServiceList getInitServiceList() {
		return initServiceList;
	}

	public void setInitServiceList(ServiceList initServiceList) {
		this.initServiceList = initServiceList;
	}

	public ServiceList getServiceList() {
		return serviceList;
	}

	public void setServiceList(ServiceList serviceList) {
		this.serviceList = serviceList;
	}

}
