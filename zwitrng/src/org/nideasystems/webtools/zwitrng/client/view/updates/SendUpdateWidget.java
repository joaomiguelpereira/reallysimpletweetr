package org.nideasystems.webtools.zwitrng.client.view.updates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.ShortLinksListenerCallBack;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.client.view.SendUpdateAsyncHandler;
import org.nideasystems.webtools.zwitrng.client.view.persona.SelectTemplateWindow;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.SelectSendingAccountWindow;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class SendUpdateWidget extends
		AbstractVerticalPanelView<TwitterAccountController> implements
		SendUpdateAsyncHandler, ShortLinksListenerCallBack {

	public static final int STATUS = 0;
	public static final int REPLY = 1;
	public static final int RETWEET = 2;
	public static final int PRIVATE_MESSAGE = 3;
	public static final Integer DEFAULT_TWEET_SIZE = 140;

	private boolean showUserImage = true;
	private final HTML remainingChars = new HTML(DEFAULT_TWEET_SIZE.toString());
	private final HTML pub = new HTML("Innovation with twitter :)");
	final TextArea update = new TextArea();
	final Button send = new Button("Send");
	private int type = STATUS;
	private TwitterAccountDTO inResponseToUserAccount = null;
	private TwitterAccountDTO sendingTwitterAccount;
	private List<String> sendingTwitterAccountNames = new ArrayList<String>();
	private TwitterUpdateDTO inResponseTotwitterUpdate;
	private final Image waitingImage = new Image(Constants.WAITING_IMAGE);
	private SendUpdateWidget instance;
	private final HTML loadTemplateLink = new HTML("Load template");
	private final HTML shortLinks = new HTML("Short links");
	private List<SendUpdateAsyncHandler> asynHadlers = null;
	private InlineHTML sendUpdatesFrom = null;

	@Override
	public void init() {
		instance = this;
		this.add(waitingImage);
		waitingImage.setVisible(false);

		update.setWidth("620px");
		// update.setHeight("35px");
		update.addStyleName("input");
		update.setVisibleLines(2);

		FlexTable bottomLayout = new FlexTable();
		FlexCellFormatter formater = bottomLayout.getFlexCellFormatter();
		bottomLayout.setCellSpacing(0);

		Image sendingUserImage = new Image(sendingTwitterAccount
				.getTwitterImageUrl());

		sendingUserImage.setWidth("32px");
		sendingUserImage.setHeight("32px");
		sendingUserImage.setVisible(showUserImage);

		// Send from block:
		final InlineHTML sendUpdatesFromLabel = new InlineHTML("From: ");
		sendUpdatesFromLabel.addStyleName("link");

		sendUpdatesFromLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Open a window bellow the from label with a list of avaialble
				// accounts in the system
				SelectSendingAccountWindow window = new SelectSendingAccountWindow(
						getController(), instance);
				window.init();
				window.show(sendUpdatesFromLabel.getAbsoluteLeft(),
						sendUpdatesFromLabel.getAbsoluteTop() + 20);
				// Load the account by twitter screen name
				window.load(sendingTwitterAccountNames);

			}

		});
		this.sendingTwitterAccountNames.add(sendingTwitterAccount
				.getTwitterScreenName());

		sendUpdatesFrom = new InlineHTML("");
		updateSendFromList();

		// this.add(sendUpdatesFromLabel);

		bottomLayout.setWidget(0, 1, sendUpdatesFromLabel);
		bottomLayout.setWidget(0, 2, sendUpdatesFrom);
		formater.setColSpan(0, 2, 2);

		// End send from block
		bottomLayout.setWidget(1, 0, sendingUserImage);
		HTML cancelLink = new HTML("Cancel");

		formater.setWidth(1, 0, "48px");

		bottomLayout.setWidget(1, 1, update);
		formater.setColSpan(1, 1, 3);
		// formater.setWidth(0, 1, "650px");

		remainingChars.setWidth("35px");
		bottomLayout.setWidget(2, 0, new HTML(""));

		bottomLayout.setWidget(2, 1, remainingChars);
		bottomLayout.setWidget(2, 2, pub);

		HorizontalPanel toolsPanel = new HorizontalPanel();
		toolsPanel.setSpacing(4);
		loadTemplateLink.addStyleName("link");
		cancelLink.addStyleName("link");

		shortLinks.addStyleName("link");

		toolsPanel.add(shortLinks);
		toolsPanel.add(loadTemplateLink);
		toolsPanel.add(cancelLink);
		toolsPanel.add(send);

		bottomLayout.setWidget(2, 3, toolsPanel);
		// bottomLayout.setWidget(1, 4, send);

		formater.setAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);

		formater.setAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		formater.setAlignment(2, 2, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);

		formater.setAlignment(2, 3, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		/*
		 * formater.setAlignment(1, 4, HasHorizontalAlignment.ALIGN_RIGHT,
		 * HasVerticalAlignment.ALIGN_MIDDLE);
		 */

		// Add handlers
		update.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				/*
				 * Integer remaining = DEFAULT_TWEET_SIZE -
				 * update.getValue().length();
				 * remainingChars.setText(remaining.toString());
				 * updateRemainingCharsClass(remaining);
				 */
				updateRemainingChars();
			}

		});

		cancelLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onCancel();

			}

		});
		send.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (sendingTwitterAccountNames.isEmpty()) {
					getController().getMainController().addError(
							"Select at least one sending account");
				} else if (update.getValue().length() > 0) {
					TwitterUpdateDTO twitterUpdate = new TwitterUpdateDTO();
					twitterUpdate.setText(update.getValue());
					twitterUpdate.setTwitterAccount(getSendingTwitterAccount());
					if (type == REPLY) {
						twitterUpdate
								.setInReplyToStatusId(inResponseTotwitterUpdate
										.getId());
					}

					if (type == PRIVATE_MESSAGE) {
						assert (twitterUpdate != null);
						assert (getInResponseToUserAccount() != null);
						twitterUpdate
								.setInReplyToUserId(getInResponseToUserAccount()
										.getId());

					}

					isUpdating(true);

					for (String userScreenName : sendingTwitterAccountNames) {

						TwitterAccountController controller = getController()
								.getMainController()
								.getTwitterAccountController(userScreenName);

						twitterUpdate.setTwitterAccount(controller.getModel());

						controller.sendUpdate(twitterUpdate, instance);
					}

					// Clear
					sendingTwitterAccountNames.clear();
					sendingTwitterAccountNames.add(getController().getModel()
							.getTwitterScreenName());
					updateSendFromList();

				}
			}

		});

		loadTemplateLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PopupPanel wnd = MainController.getInstance().getPopupManager()
						.showSelectTemplateWindow(instance,
								loadTemplateLink.getAbsoluteLeft(),
								loadTemplateLink.getAbsoluteTop() + 20);

				wnd.addCloseHandler(new CloseHandler<PopupPanel>() {

					@Override
					public void onClose(CloseEvent<PopupPanel> event) {
						update.setFocus(true);
						update.setCursorPos(update.getValue().length());

					}

				});

			};

		});
		shortLinks.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isUpdating(true);
				SendUpdateWidget.shortLinks(update.getValue(),
						instance);

			}

		});
		super.add(bottomLayout);

	}

	/**
	 * TODO:Refactor this. Is copied somewhere else Short any link in the update
	 */
	/*
	 * private void shortLinks() { // Get the links List<String> links =
	 * HTMLHelper.get().getLinks(update.getValue()); if (links.size() > 0) { try
	 * { getController().getServiceManager().getRPCService().shortLinks( links,
	 * new AsyncCallback<Map<String, String>>() {
	 * 
	 * @Override public void onFailure(Throwable caught) {
	 * getController().getMainController() .addException(caught);
	 * 
	 * }
	 * 
	 * @Override public void onSuccess(Map<String, String> result) {
	 * replaceShortLinks(result);
	 * 
	 * }
	 * 
	 * }); } catch (Exception e) {
	 * getController().getMainController().addException(e); }
	 * 
	 * }
	 * 
	 * }
	 */

	public static void shortLinks(String updateText,
			final ShortLinksListenerCallBack callback) {
		// Get the links
		List<String> links = HTMLHelper.get().getLinks(updateText);
		if (links.size() > 0) {
			try {
				MainController.getInstance().getCurrentPersonaController().getServiceManager().getRPCService().shortLinks(
						links, new AsyncCallback<Map<String, String>>() {

							@Override
							public void onFailure(Throwable caught) {
								MainController.getInstance().addException(
										caught);

							}

							@Override
							public void onSuccess(Map<String, String> result) {
								callback.onLinksShortened(result);

							}

						});
			} catch (Exception e) {
				MainController.getInstance().addException(e);
			}

		} else {
			callback.onLinksShortened(null);
		}

	}

	private void updateRemainingChars(/* Integer remaining */) {

		Integer remaining = DEFAULT_TWEET_SIZE - update.getValue().length();
		remainingChars.setText(remaining.toString());
		// updateRemainingCharsClass(remaining);

		if (remaining < 0) {
			remainingChars.addStyleName("error");
		} else {
			remainingChars.removeStyleName("error");
		}

	}

	@Override
	public void isUpdating(boolean isUpdating) {

		this.waitingImage.setVisible(isUpdating);

	}

	public void setType(int type) {
		this.type = type;

	}

	public void setInResponseTo(TwitterUpdateDTO twitterUpdate) {
		this.inResponseTotwitterUpdate = twitterUpdate;

	}

	public void refresh() {

		remainingChars.setText(DEFAULT_TWEET_SIZE.toString());

		if (this.type == REPLY) {
			assert (this.inResponseTotwitterUpdate != null);
			this.update.setFocus(true);
			this.update.setValue("@"
					+ this.inResponseTotwitterUpdate.getTwitterAccount()
							.getTwitterScreenName() + " ");

		} else if (this.type == RETWEET) {
			assert (this.inResponseTotwitterUpdate != null);
			this.update.setFocus(true);

			this.update.setValue("RT @"
					+ this.inResponseTotwitterUpdate.getTwitterAccount()
							.getTwitterScreenName() + ":"
					+ this.inResponseTotwitterUpdate.getText());

		}

		/*
		 * Integer remainingLength = DEFAULT_TWEET_SIZE -
		 * this.update.getValue().length();
		 * 
		 * this.remainingChars.setText(remainingLength.toString());
		 */
		updateRemainingChars(/* remainingLength */);

	}

	public int getType() {
		return this.type;
	}

	public void setSendingTwitterAccount(TwitterAccountDTO sendingTwitterAccount) {
		this.sendingTwitterAccount = sendingTwitterAccount;
	}

	public TwitterAccountDTO getSendingTwitterAccount() {
		return sendingTwitterAccount;
	}

	public void addAsyncHandler(SendUpdateAsyncHandler asyncHandler) {
		if (this.asynHadlers == null) {
			this.asynHadlers = new ArrayList<SendUpdateAsyncHandler>();
		}
		this.asynHadlers.add(asyncHandler);
	}

	@Override
	public void onSuccess(Object result) {
		isUpdating(false);
		update.setValue("");
		this.remainingChars.setText(DEFAULT_TWEET_SIZE.toString());
		if (this.asynHadlers != null) {
			for (SendUpdateAsyncHandler handler : this.asynHadlers) {
				handler.onSuccess(result);
			}
			this.asynHadlers = null;
		}

	}

	@Override
	public void onFailure(Throwable tr) {
		isUpdating(false);
		if (this.asynHadlers != null) {
			for (SendUpdateAsyncHandler handler : this.asynHadlers) {
				handler.onFailure(tr);
			}
			this.asynHadlers = null;
		}

	}

	public void setShowUserImage(boolean showUserImg) {
		this.showUserImage = showUserImg;

	}

	@Override
	public void onCancel() {
		update.setValue("");
		updateRemainingChars();
		if (this.asynHadlers != null) {
			for (SendUpdateAsyncHandler handler : this.asynHadlers) {
				handler.onCancel();
			}
			this.asynHadlers = null;
		}

	}

	public TwitterAccountDTO getInResponseToUserAccount() {
		return inResponseToUserAccount;
	}

	public void setInResponseToUserAccount(TwitterAccountDTO twitterAccount) {
		this.inResponseToUserAccount = twitterAccount;

	}

	public void addSendingAccount(TwitterAccountDTO account) {

		this.sendingTwitterAccountNames.add(account.getTwitterScreenName());
		updateSendFromList();

	}

	private void updateSendFromList() {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < this.sendingTwitterAccountNames.size(); i++) {
			sb.append(this.sendingTwitterAccountNames.get(i));

			if (i < this.sendingTwitterAccountNames.size() - 1) {
				sb.append(", ");
			}

		}

		this.sendUpdatesFrom.setHTML(sb.toString());

	}

	public void removeSendingAccount(TwitterAccountDTO account) {
		this.sendingTwitterAccountNames.remove(account.getTwitterScreenName());
		updateSendFromList();

	}

	public void setTemplateText(String templateText) {
		this.update.setValue(templateText);
		
		this.updateRemainingChars();

	}

	@Override
	public void onLinksShortened(Map<String, String> result) {
		isUpdating(false);
		String currentStr = update.getValue();
		String newStr = HTMLHelper.get().replaceText(currentStr, result);
		update.setFocus(true);
		update.setValue(newStr);
		updateRemainingChars();

	}

	public String getText() {
		return update.getValue();
		
	}

}
