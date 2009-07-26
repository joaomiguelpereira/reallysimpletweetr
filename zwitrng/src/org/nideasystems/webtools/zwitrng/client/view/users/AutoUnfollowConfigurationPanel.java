package org.nideasystems.webtools.zwitrng.client.view.users;

import java.util.ArrayList;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AutoUnfollowConfigurationPanel extends VerticalPanel implements
		AutoFollowRuleCallback {

	private AutoFollowRuleDTO autoFollowRule;
	private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private CheckBox enabled;
	private CheckBox sendReply;
	private TextBox replyTemplate;

	private InlineHTML summary;
	private Button saveBt;

	public AutoUnfollowConfigurationPanel() {
		waitingImg.setVisible(false);
		this.add(waitingImg);
		this.add(new InlineHTML("<h2>Configure Auto UnFollow Back</h2>"));
		enabled = new CheckBox("Unfollow Users that do not follow me.");
		this.add(enabled);
		sendReply = new CheckBox("Send a message to the user before unfollow?");
		this.add(sendReply);
		replyTemplate = new TextBox();
		this.add(replyTemplate);
		

		summary = new InlineHTML();

		saveBt = new Button("Save");
		saveBt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveAutoFollowRule();

			}

		});
		this.add(saveBt);
		this.add(summary);

	}

	private void saveAutoFollowRule() {

		// Validate
		boolean valid = true;

		if (this.sendReply.getValue()
				&& this.replyTemplate.getValue().trim().length() == 0) {
			MainController.getInstance().addError(
					"Provide a name for the template");
			valid = false;
		}
		if (valid) {
			// create a dto
			AutoFollowRuleDTO rule = new AutoFollowRuleDTO();

			rule.setEnabled(this.enabled.getValue());
			rule.setMaxRatio(0);
			rule.setMinUpdates(0);
			rule.setSendDirectMessage(this.sendReply.getValue());
			rule.setTemplateName(this.replyTemplate.getValue());
			java.util.List<String> excludedNames = new ArrayList<String>();

			rule.setExcludedWordsInNames(excludedNames);
			rule.setTriggerType(AutoFollowTriggerType.UNFOLLOW);
			rule.setSendDirectMessageOnIgnore(false);
			rule.setIgnoreTemplate("");
			waitingImg.setVisible(true);
			MainController.getInstance().getCurrentPersonaController()
					.saveAutofollowRule(rule, this);

		}
	}

	private void refresh() {
		waitingImg.setVisible(false);
		this.enabled.setValue(this.autoFollowRule.isEnabled());
		this.sendReply.setValue(this.autoFollowRule.isSendDirectMessage());
		this.replyTemplate.setValue(this.autoFollowRule.getTemplateName());
		

	}

	private void refreshSummary() {
		StringBuffer sb = new StringBuffer();

		if (this.autoFollowRule.isEnabled()) {
			sb.append("Unfollow all users that do not follow me.");
		} else {
			sb.append("Keep following users that do not follow me.");
		}

	}

	public void loadRule() {
		if (this.autoFollowRule == null) {
			waitingImg.setVisible(true);
			MainController.getInstance().getCurrentPersonaController()
					.loadRule(AutoFollowTriggerType.UNFOLLOW, this);

		}
	}

	@Override
	public void onAutoFollowRuleChanged(AutoFollowRuleDTO rule) {
		this.autoFollowRule = rule;
		refresh();
		refreshSummary();

	}

	@Override
	public void onAutoFollowRuleLoaded(AutoFollowRuleDTO rule) {
		this.autoFollowRule = rule;
		refresh();
		refreshSummary();

	}

	@Override
	public void onError(Throwable tr) {
		waitingImg.setVisible(false);
		MainController.getInstance().addException(tr);

	}

	public void setAutoFollowRule(AutoFollowRuleDTO autoFollowRule) {
		this.autoFollowRule = autoFollowRule;
	}

	public AutoFollowRuleDTO getAutoFollowRule() {
		return autoFollowRule;
	}

}
