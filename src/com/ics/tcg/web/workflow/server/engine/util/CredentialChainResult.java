package com.ics.tcg.web.workflow.server.engine.util;

import java.io.Serializable;

public class CredentialChainResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2382266118079894956L;

	private String serPro_SerRoleRuleEx;// ServiceProvider.Service��ɫ������ʽ
	private String serPro_SerRoleResultEx;// ServiceProvider.Service��ɫ��֤������ʽ
	private String serPro_SerRoleRuleIssuer;// ServiceProvider.Service��ɫ��Ȩ����ķ�����

	private String serviceRankRuleEx;// ����ȼ���ɫ������ʽ
	private String serviceRankResultEx;// ����ȼ���ɫ������ʽ
	private String serviceRankRuleIssuer;// ����ȼ���ɫ��Ȩ����ķ�����

	private String thirdPG_MemRoleRuleEx;// Third-PartyGroup.Member��ɫ������ʽ
	private String thirdPG_MemRoleResultEx;// Third-PartyGroup.Member��ɫ��֤������ʽ
	private String thirdPG_MemRoleRuleIssuer;// Third-PartyGroup.Member��ɫ��Ȩ����ķ�����

	private String certificationUserRoleRuleEx;// CertificationUser��ɫ��֤������ʽ
	private String certificationUserRoleResultEx;// CertificationUser��ɫ��֤������ʽ
	private String certificationUserRoleRuleIssuer;// CertificationUser��ɫ��Ȩ����ķ�����

	private String cerGroup_AdminRoleRuleEx;// CertificationGroupAdministrator��ɫ������ʽ
	private String cerGroup_AdminRoleResultEx;// CertificationGroupAdministrator��ɫ��֤������ʽ
	private String cerGroup_AdminRoleRuleIssuer;// CertificationGroupAdministrator��ɫ��Ȩ����ķ�����

	private String assignCerUserRoleRightRuleEx;// hasRightAssignCertificationUserRole���Թ�����ʽ
	private String assignCerUserRoleRightResultEx;// hasRightAssignCertificationUserRole���Խ�����ʽ
	private String assignCerUserRoleRightRuleIssuer;// hasRightAssignCertificationUserRole������Ȩ����ķ�����

	private String assignSerRankRoleRightRuleEx;// hasRightAssignServiceRankRole���Թ�����ʽ
	private String assignSerRankRoleRightResultEx;// hasRightAssignServiceRankRole���Խ�����ʽ
	private String assignSerRankRoleRightRuleIssuer;// hasRightAssignServiceRankRole������Ȩ����ķ�����

	private String finalResult;// ��֤���ʽ�����ս��
	private String validateExpression;// ��֤���ʽ

	public CredentialChainResult() {
	}

	public String getSerPro_SerRoleRuleEx() {
		return serPro_SerRoleRuleEx;
	}

	public void setSerPro_SerRoleRuleEx(String serPro_SerRoleRuleEx) {
		this.serPro_SerRoleRuleEx = serPro_SerRoleRuleEx;
	}

	public String getSerPro_SerRoleResultEx() {
		return serPro_SerRoleResultEx;
	}

	public void setSerPro_SerRoleResultEx(String serPro_SerRoleResultEx) {
		this.serPro_SerRoleResultEx = serPro_SerRoleResultEx;
	}

	public String getCertificationUserRoleRuleEx() {
		return certificationUserRoleRuleEx;
	}

	public void setCertificationUserRoleRuleEx(
			String certificationUserRoleRuleEx) {
		this.certificationUserRoleRuleEx = certificationUserRoleRuleEx;
	}

	public String getCertificationUserRoleResultEx() {
		return certificationUserRoleResultEx;
	}

	public void setCertificationUserRoleResultEx(
			String certificationUserRoleResultEx) {
		this.certificationUserRoleResultEx = certificationUserRoleResultEx;
	}

	public String getServiceRankRuleEx() {
		return serviceRankRuleEx;
	}

	public void setServiceRankRuleEx(String serviceRankRuleEx) {
		this.serviceRankRuleEx = serviceRankRuleEx;
	}

	public String getServiceRankResultEx() {
		return serviceRankResultEx;
	}

	public void setServiceRankResultEx(String serviceRankResultEx) {
		this.serviceRankResultEx = serviceRankResultEx;
	}

	public String getThirdPG_MemRoleRuleEx() {
		return thirdPG_MemRoleRuleEx;
	}

	public void setThirdPG_MemRoleRuleEx(String thirdPG_MemRoleRuleEx) {
		this.thirdPG_MemRoleRuleEx = thirdPG_MemRoleRuleEx;
	}

	public String getThirdPG_MemRoleResultEx() {
		return thirdPG_MemRoleResultEx;
	}

	public void setThirdPG_MemRoleResultEx(String thirdPG_MemRoleResultEx) {
		this.thirdPG_MemRoleResultEx = thirdPG_MemRoleResultEx;
	}

	public String getCerGroup_AdminRoleRuleEx() {
		return cerGroup_AdminRoleRuleEx;
	}

	public void setCerGroup_AdminRoleRuleEx(String cerGroup_AdminRoleRuleEx) {
		this.cerGroup_AdminRoleRuleEx = cerGroup_AdminRoleRuleEx;
	}

	public String getCerGroup_AdminRoleResultEx() {
		return cerGroup_AdminRoleResultEx;
	}

	public void setCerGroup_AdminRoleResultEx(String cerGroup_AdminRoleResultEx) {
		this.cerGroup_AdminRoleResultEx = cerGroup_AdminRoleResultEx;
	}

	public String getAssignCerUserRoleRightRuleEx() {
		return assignCerUserRoleRightRuleEx;
	}

	public void setAssignCerUserRoleRightRuleEx(
			String assignCerUserRoleRightRuleEx) {
		this.assignCerUserRoleRightRuleEx = assignCerUserRoleRightRuleEx;
	}

	public String getAssignCerUserRoleRightResultEx() {
		return assignCerUserRoleRightResultEx;
	}

	public void setAssignCerUserRoleRightResultEx(
			String assignCerUserRoleRightResultEx) {
		this.assignCerUserRoleRightResultEx = assignCerUserRoleRightResultEx;
	}

	public String getAssignSerRankRoleRightRuleEx() {
		return assignSerRankRoleRightRuleEx;
	}

	public void setAssignSerRankRoleRightRuleEx(
			String assignSerRankRoleRightRuleEx) {
		this.assignSerRankRoleRightRuleEx = assignSerRankRoleRightRuleEx;
	}

	public String getAssignSerRankRoleRightResultEx() {
		return assignSerRankRoleRightResultEx;
	}

	public void setAssignSerRankRoleRightResultEx(
			String assignSerRankRoleRightResultEx) {
		this.assignSerRankRoleRightResultEx = assignSerRankRoleRightResultEx;
	}

	public String getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}

	public String getValidateExpression() {
		return validateExpression;
	}

	public void setValidateExpression(String validateExpression) {
		this.validateExpression = validateExpression;
	}

	public String getSerPro_SerRoleRuleIssuer() {
		return serPro_SerRoleRuleIssuer;
	}

	public void setSerPro_SerRoleRuleIssuer(String serPro_SerRoleRuleIssuer) {
		this.serPro_SerRoleRuleIssuer = serPro_SerRoleRuleIssuer;
	}

	public String getServiceRankRuleIssuer() {
		return serviceRankRuleIssuer;
	}

	public void setServiceRankRuleIssuer(String serviceRankRuleIssuer) {
		this.serviceRankRuleIssuer = serviceRankRuleIssuer;
	}

	public String getThirdPG_MemRoleRuleIssuer() {
		return thirdPG_MemRoleRuleIssuer;
	}

	public void setThirdPG_MemRoleRuleIssuer(String thirdPG_MemRoleRuleIssuer) {
		this.thirdPG_MemRoleRuleIssuer = thirdPG_MemRoleRuleIssuer;
	}

	public String getCertificationUserRoleRuleIssuer() {
		return certificationUserRoleRuleIssuer;
	}

	public void setCertificationUserRoleRuleIssuer(
			String certificationUserRoleRuleIssuer) {
		this.certificationUserRoleRuleIssuer = certificationUserRoleRuleIssuer;
	}

	public String getCerGroup_AdminRoleRuleIssuer() {
		return cerGroup_AdminRoleRuleIssuer;
	}

	public void setCerGroup_AdminRoleRuleIssuer(
			String cerGroup_AdminRoleRuleIssuer) {
		this.cerGroup_AdminRoleRuleIssuer = cerGroup_AdminRoleRuleIssuer;
	}

	public String getAssignCerUserRoleRightRuleIssuer() {
		return assignCerUserRoleRightRuleIssuer;
	}

	public void setAssignCerUserRoleRightRuleIssuer(
			String assignCerUserRoleRightRuleIssuer) {
		this.assignCerUserRoleRightRuleIssuer = assignCerUserRoleRightRuleIssuer;
	}

	public String getAssignSerRankRoleRightRuleIssuer() {
		return assignSerRankRoleRightRuleIssuer;
	}

	public void setAssignSerRankRoleRightRuleIssuer(
			String assignSerRankRoleRightRuleIssuer) {
		this.assignSerRankRoleRightRuleIssuer = assignSerRankRoleRightRuleIssuer;
	}
}
