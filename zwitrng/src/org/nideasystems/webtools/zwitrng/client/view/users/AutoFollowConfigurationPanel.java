package org.nideasystems.webtools.zwitrng.client.view.users;

import java.util.ArrayList;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AutoFollowConfigurationPanel extends VerticalPanel implements
		AutoFollowRuleCallback {
	InlineHTML summary;
	TextBox updatesInput;
	TextBox ffRatioInput;
	TextBox templateName;
	CheckBox sendDm;
	TextArea excludeUserNames;
	CheckBox enableAutoFollow;
	Button saveBt;
	private AutoFollowRuleDTO autoFollowRule;
	Image waitingImg = new Image(Constants.WAITING_IMAGE);

	public AutoFollowConfigurationPanel() {
		waitingImg.setVisible(false);
		this.add(waitingImg);
		enableAutoFollow = new CheckBox("Enabled");
		this.add(enableAutoFollow);
		InlineHTML ffRatio = new InlineHTML(
				"Follow users with a Following/Followers ratio less than");
		ffRatioInput = new TextBox();
		ffRatioInput.setValue("140%");
		this.add(ffRatio);
		this.add(ffRatioInput);

		InlineHTML updates = new InlineHTML(
				"Follow users with updates greater than:");
		updatesInput = new TextBox();
		updatesInput.setValue("130");
		this.add(updates);
		this.add(updatesInput);

		sendDm = new CheckBox("Send Direct message on follow?");
		templateName = new TextBox();
		this.add(sendDm);
		this.add(templateName);

		InlineHTML excludeUserLabel = new InlineHTML(
				"Exclude Users with usernames that contains (one word per line):");
		excludeUserNames = new TextArea();
		excludeUserNames.setVisibleLines(3);
		excludeUserNames.setWidth("150px");
		this.add(excludeUserLabel);
		this.add(excludeUserNames);

		summary = new InlineHTML(createSummary());
		this.add(summary);

		enableAutoFollow
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						setFieldsLock(event.getValue());

					}

				});
		excludeUserNames.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				refreshSummary();

			}

		});
		sendDm.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				refreshSummary();
				setTemplateNameLock(event.getValue());
			}

		});
		templateName.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				refreshSummary();

			}

		});
		ffRatioInput.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				refreshSummary();

			}

		});

		updatesInput.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				refreshSummary();

			}

		});
		saveBt = new Button("Save");
		saveBt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveAutoFollowRule();

			}

		});
		this.add(saveBt);

	}

	protected void setTemplateNameLock(Boolean value) {
		this.templateName.setEnabled(value);

	}

	public void loadRule(AutoFollowRuleDTO rule) {
		this.autoFollowRule = rule;

	}

	private void setFieldsLock(Boolean value) {
		this.excludeUserNames.setEnabled(value);
		this.ffRatioInput.setEnabled(value);
		this.sendDm.setEnabled(value);
		this.templateName.setEnabled(value);
		this.updatesInput.setEnabled(value);

	}

	private void saveAutoFollowRule() {
		// Validate

		boolean hasErrors = false;
		Integer followingFollowRation = 0;
		Integer updates = 0;
		// check ratio
		if (this.ffRatioInput.getValue().trim().length() == 0
				|| this.ffRatioInput.getValue().indexOf("%") < 1) {
			MainController
					.getInstance()
					.addError(
							"Please provide a value for Following/Followers ratio. Use format xxx%.");
			hasErrors = true;
		} else {
			// Try to conver it
			try {
				followingFollowRation = Integer
						.valueOf(this.ffRatioInput.getValue().trim()
								.substring(
										0,
										this.ffRatioInput.getValue().trim()
												.length() - 1));
			} catch (NumberFormatException e) {
				MainController
						.getInstance()
						.addError(
								"Please provide a value for Following/Followers ratio. Use format xxx%.");
				hasErrors = true;
			}
		}

		if (this.updatesInput.getValue().trim().length() == 0) {
			MainController.getInstance().addError(
					"Please provide a value for the updates.");
			hasErrors = true;
		} else {

			try {
				updates = Integer.valueOf(this.updatesInput.getValue().trim());
			} catch (NumberFormatException e) {
				MainController
						.getInstance()
						.addError(
								"Please provide a value for Following/Followers ratio. Use format xxx%.");
				hasErrors = true;
			}
		}

		if (sendDm.getValue()
				&& this.templateName.getValue().trim().length() == 0) {
			MainController
					.getInstance()
					.addError(
							"Please provide a name of a template to use in the Direct Message.");
			hasErrors = true;
		}

		if (!hasErrors) {

			// create a dto
			AutoFollowRuleDTO rule = new AutoFollowRuleDTO();

			rule.setEnabled(this.enableAutoFollow.getValue());
			rule.setMaxRatio(followingFollowRation);
			rule.setMinUpdates(updates);
			rule.setSendDirectMessage(this.sendDm.getValue());
			java.util.List<String> excludedNames = new ArrayList<String>();
			rule.setTemplateName(this.templateName.getValue());
			String[] lines = getLines(this.excludeUserNames);
			if (lines.length > 0) {
				for (String str : lines) {
					if (str.trim().length() > 0) {
						excludedNames.add(str);
					}
				}
			}
			rule.setExcludedWordsInNames(excludedNames);
			rule.setTriggerType(AutoFollowTriggerType.ON_FOLLOW_ME);
			waitingImg.setVisible(true);
			MainController.getInstance().getCurrentPersonaController()
					.saveAutofollowRule(rule, this);

		}

	}

	private void refreshSummary() {
		this.summary.setHTML(createSummary());
	}

	private String[] getLines(TextArea excludeUserNames) {
		//String[] exStrings = excludeUserNames.getValue().split("\\n");
		return HTMLHelper.getLines(excludeUserNames.getValue());
		//return exStrings;
		

	}

	private void adjustLines(TextArea excludeUserNames, int lines) {
		HTMLHelper.adjustLines(excludeUserNames,lines,3,10);
		

	}

	private String createSummary() {

		StringBuffer sb = new StringBuffer();
		sb.append("<span class=\"bolder\">Summary:</span>");
		sb
				.append("Follow all users that follow me and have a Following/Followers ration below ");
		sb.append(this.ffRatioInput.getValue());
		sb.append(" and have more than ");
		sb.append(this.updatesInput.getValue());
		sb.append(" updates.");
		if (sendDm.getValue()) {
			sb
					.append(" Send a Direct Message before following using the template ");
			sb.append(this.templateName.getValue() + ".");
		}

		String[] excludedNames = getLines(this.excludeUserNames);
		if (excludedNames != null && excludedNames.length > 0) {
			sb
					.append("Exclude users that have in the user name the following words: ");
			for (String str : excludedNames) {
				sb.append(str);
				sb.append(",");
			}
			adjustLines(this.excludeUserNames, excludedNames.length);
		}

		return sb.toString();

	}

	@Override
	public void onAutoFollowRuleChanged(AutoFollowRuleDTO rule) {
		this.autoFollowRule = rule;
		refresh();
		refreshSummary();
		
	}

	private void refresh() {
		waitingImg.setVisible(false);
		this.enableAutoFollow.setValue(autoFollowRule.isEnabled());
		this.ffRatioInput.setValue(autoFollowRule.getMaxRatio() + "%");
		this.sendDm.setValue(autoFollowRule.isSendDirectMessage());
		this.templateName.setValue(autoFollowRule.getTemplateName());
		this.updatesInput.setValue(autoFollowRule.getMinUpdates() + "");
		StringBuffer sb = new StringBuffer();

		for (String str : autoFollowRule.getExcludedWordsInNames()) {
			sb.append(str);
			sb.append("\n");
		}
		this.excludeUserNames.setValue(sb.toString());
		setFieldsLock(autoFollowRule.isEnabled());
	}

	@Override
	public void onError(Throwable tr) {
		waitingImg.setVisible(false);
		MainController.getInstance().addException(tr);

	}

	public void loadRule() {
		if (this.autoFollowRule==null) {
			waitingImg.setVisible(true);
			MainController.getInstance().getCurrentPersonaController().loadRule(
					AutoFollowTriggerType.ON_FOLLOW_ME, this);
			
		}
	}

	@Override
	public void onAutoFollowRuleLoaded(AutoFollowRuleDTO rule) {
		this.autoFollowRule = rule;
		refresh();
		refreshSummary();
	}
}
