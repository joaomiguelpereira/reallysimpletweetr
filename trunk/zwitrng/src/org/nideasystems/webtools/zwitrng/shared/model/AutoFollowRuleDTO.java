package org.nideasystems.webtools.zwitrng.shared.model;

import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;

public class AutoFollowRuleDTO implements IDTO {

	private boolean enabled;
	private int maxRatio;
	private int minUpdates;
	private boolean sendDirectMessage;
	private java.util.List<String> excludedWordsInNames;
	private AutoFollowTriggerType triggerType;
	private String templateName;
	private boolean sendDirectMessageOnIgnore;
	private String ignoreTemplate;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getMaxRatio() {
		return maxRatio;
	}

	public void setMaxRatio(int maxRatio) {
		this.maxRatio = maxRatio;
	}

	public int getMinUpdates() {
		return minUpdates;
	}

	public void setMinUpdates(int minUpdates) {
		this.minUpdates = minUpdates;
	}

	public boolean isSendDirectMessage() {
		return sendDirectMessage;
	}

	public void setSendDirectMessage(boolean sendDirectMessage) {
		this.sendDirectMessage = sendDirectMessage;
	}

	public java.util.List<String> getExcludedWordsInNames() {
		return excludedWordsInNames;
	}

	public void setExcludedWordsInNames(
			java.util.List<String> excludedWordsInNames) {
		this.excludedWordsInNames = excludedWordsInNames;
	}

	public AutoFollowTriggerType getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(AutoFollowTriggerType triggerType) {
		this.triggerType = triggerType;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setSendDirectMessageOnIgnore(boolean sendDirectMessageOnIgnore) {
		this.sendDirectMessageOnIgnore = sendDirectMessageOnIgnore;
	}

	public boolean isSendDirectMessageOnIgnore() {
		return sendDirectMessageOnIgnore;
	}

	public void setIgnoreTemplate(String ignoreTemplate) {
		this.ignoreTemplate = ignoreTemplate;
	}

	public String getIgnoreTemplate() {
		return ignoreTemplate;
	}

}
