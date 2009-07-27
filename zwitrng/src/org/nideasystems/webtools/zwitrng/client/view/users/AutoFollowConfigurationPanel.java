package org.nideasystems.webtools.zwitrng.client.view.users;

import java.util.ArrayList;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class AutoFollowConfigurationPanel extends AbstractRuleConfiguration {

	private TextBox keepMyRatioUnderLimit;
	private TextBox searchTerm;
	private CheckBox ignoreUsersIUnfollowed;
	private TextBox minUpdates;
	private TextBox maxRatio;
	private TextArea excludeUserNames;
	private CheckBox retweetTeaser;
	private InlineHTML summary;
	private TextArea exludeSource;

	public AutoFollowConfigurationPanel() {
		super(AutoFollowTriggerType.SEARCH, "Auto follow based on Tweet Search");

		this.add(new InlineHTML("Keep my Following/Followers ratio under:"));
		keepMyRatioUnderLimit = new TextBox();
		this.add(keepMyRatioUnderLimit);

		this.add(new InlineHTML("Follow users based on search term"));
		searchTerm = new TextBox();
		this.add(searchTerm);

		ignoreUsersIUnfollowed = new CheckBox(
				"Dont follow people I have unfollowed");
		this.add(ignoreUsersIUnfollowed);

		this.add(new InlineHTML(
				"Follow people with a following/followers ratio under "));
		this.maxRatio = new TextBox();
		this.add(maxRatio);

		this.add(new InlineHTML("Follow people with a a minimum of updates:"));
		this.minUpdates = new TextBox();
		this.add(minUpdates);

		this
				.add(new InlineHTML(
						"Don't follow people with usernames containing the following words"));
		this.excludeUserNames = new TextArea();
		excludeUserNames.setVisibleLines(3);
		excludeUserNames.setWidth("150px");
		this.add(excludeUserNames);

		this
				.add(new InlineHTML(
						"Don't follow people tweeting from clients containing the following words:"));
		this.exludeSource = new TextArea();
		this.exludeSource.setVisibleLines(3);
		this.exludeSource.setWidth("150px");
		this.add(this.exludeSource);

		retweetTeaser = new CheckBox("Retweet before following as a teaser");
		this.add(retweetTeaser);

		summary = new InlineHTML();
		this.add(summary);
		this.add(save);

	}

	@Override
	public void refresh() {
		this.enabled.setValue(rule.isEnabled());
		this.ignoreUsersIUnfollowed.setValue(rule.isDonFollowWhoIUnfollowed());
		this.keepMyRatioUnderLimit.setValue(rule.getKeepRatio()+"");
		this.maxRatio.setValue(rule.getMaxRatio()+"");
		this.minUpdates.setValue(rule.getMinUpdates()+"");
		this.retweetTeaser.setValue(rule.isSendTeaserTweet());
		this.searchTerm.setValue(rule.getSearchTerm());
		StringBuffer sb = new StringBuffer();
		for (String str: rule.getExcludedWordsInNames() ) {
			sb.append(str);
			sb.append("\n");
			
		}
		this.excludeUserNames.setValue(sb.toString());
		sb = new StringBuffer();
		for (String str: rule.getExludeClientsWithWords() ) {
			sb.append(str);
			sb.append("\n");
		}
		this.exludeSource.setValue(sb.toString());
		waitingImg.setVisible(false);

	}

	@Override
	public void refreshSummary() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveAutoFollowRule() {
		

		// check if is valid
		boolean isValid = true;
		AutoFollowRuleDTO rule = new AutoFollowRuleDTO();

		// Enabled
		rule.setEnabled(this.enabled.getValue());
		// if (this.enabled.getValue()) {

		if (this.keepMyRatioUnderLimit.getValue().trim().length() == 0) {
			MainController
					.getInstance()
					.addError(
							"Please provide a valid number for you following/follower ratio.");
			isValid = false;
		} else {
			try {
				// KeepRation
				rule.setKeepRatio(Integer.valueOf(this.keepMyRatioUnderLimit
						.getValue().trim().substring(
								0,
								this.keepMyRatioUnderLimit.getValue().trim()
										.length())));
			} catch (NumberFormatException e) {
				MainController
						.getInstance()
						.addError(
								"Please provide a valid number for you following/follower ratio.");
				isValid = false;
			}
		}

		if (this.searchTerm.getValue().trim().length() == 0) {
			MainController.getInstance().addError(
					"Please provide a valid search term");
			isValid = false;
		} else {
			// search term
			rule.setSearchTerm(this.searchTerm.getValue().trim());
		}

		if (this.maxRatio.getValue().trim().length() == 0) {
			MainController
					.getInstance()
					.addError(
							"Please provide a valid number for max following/follower ratio of users to follow.");
			isValid = false;
		} else {
			try {
				// Ration
				rule.setMaxRatio(Integer.valueOf(this.maxRatio.getValue()
						.trim().substring(0,
								this.maxRatio.getValue().trim().length())));
			} catch (NumberFormatException e) {
				MainController
						.getInstance()
						.addError(
								"Please provide a valid number for max following/follower ratio of users to follow.");
				isValid = false;
			}
		}

		if (this.minUpdates.getValue().trim().length() == 0) {
			MainController.getInstance().addError(
					"Please provide a valid number for minimum updates.");
			isValid = false;
		} else {
			try {
				rule
						.setMinUpdates(Integer.valueOf(this.minUpdates
								.getValue().trim().substring(
										0,
										this.minUpdates.getValue().trim()
												.length())));
			} catch (NumberFormatException e) {
				MainController.getInstance().addError(
						"Please provide a valid number for minimum updates.");
				isValid = false;
			}
		}

		java.util.List<String> excludedNames = new ArrayList<String>();

		String[] lines = HTMLHelper.getLines(this.excludeUserNames.getValue());
		if (lines.length > 0) {
			for (String str : lines) {
				if (str.trim().length() > 0) {
					excludedNames.add(str);
				}
			}
		}
		rule.setExcludedWordsInNames(excludedNames);

		java.util.List<String> excludedClients = new ArrayList<String>();

		lines = HTMLHelper.getLines(this.exludeSource.getValue());
		if (lines.length > 0) {
			for (String str : lines) {
				if (str.trim().length() > 0) {
					excludedClients.add(str);
				}
			}
		}
		rule.setExludeClientsWithWords(excludedClients);
		rule
				.setDontFollowWhoIUnfollowed(this.ignoreUsersIUnfollowed
						.getValue());
		rule.setSendTeaserTweet(this.retweetTeaser.getValue());
		
		rule.setTriggerType(AutoFollowTriggerType.SEARCH);

		// } //end if isEnabled
		if (isValid) {
			waitingImg.setVisible(true);
			MainController.getInstance().getCurrentPersonaController()
					.saveAutofollowRule(rule, this);
		}

	}

}
