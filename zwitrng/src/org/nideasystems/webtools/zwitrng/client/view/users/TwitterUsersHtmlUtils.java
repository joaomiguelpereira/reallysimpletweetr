package org.nideasystems.webtools.zwitrng.client.view.users;

import java.util.Date;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUserDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

public class TwitterUsersHtmlUtils {

	public static String buildStatusLine(String status) {
		return "<span class=\"inlineStatus\"><span class=\"label\">Last status:</span> "
				+ StringUtils.jsParseText(status) + "</span>";
	}

	public static Widget buildUserPopularityPanel(final TwitterUserDTO user,
			boolean linkable) {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(4);
		InlineHTML following_label = new InlineHTML("Following: ");
		InlineHTML followers_label = new InlineHTML("Followers: ");
		InlineHTML updates_label = new InlineHTML("Updates: ");

		InlineHTML followingFollowersRatioLabel = new InlineHTML(
				"Following/Followers Ratio:");

		InlineHTML following;
		InlineHTML followers;
		InlineHTML updates;
		if (linkable) {
			following = new InlineHTML("<a href=\"http://twitter.com/"
					+ user.getTwitterScreenName()
					+ "/friends\" target=\"_blank\">"
					+ user.getTwitterFriends().toString() + "</a>");

			followers = new InlineHTML("<a href=\"http://twitter.com/"
					+ user.getTwitterScreenName()
					+ "/followers\" target=\"_blank\">"
					+ user.getTwitterFollowers().toString() + "</a>");
			updates = new InlineHTML(user.getTwitterUpdates().toString());
			updates.addStyleName("link");

		} else {
			following = new InlineHTML("<span class=\"bolder\">"
					+ user.getTwitterFriends().toString() + "</span>");
			followers = new InlineHTML("<span class=\"bolder\">"
					+ user.getTwitterFollowers().toString() + "</span>");
			updates = new InlineHTML("<span class=\"bolder\">"
					+ user.getTwitterUpdates().toString() + "</span>");

		}

		float ratio = Float.valueOf(user.getTwitterFriends())
				/ Float.valueOf(user.getTwitterFollowers());
		InlineHTML followingFollowersRatio = new InlineHTML(StringUtils
				.formatPercentage(ratio, 2));
		followingFollowersRatio.addStyleName("label");
		panel.add(following_label);
		panel.add(following);

		panel.add(followers_label);
		panel.add(followers);

		panel.add(followingFollowersRatioLabel);
		panel.add(followingFollowersRatio);

		panel.add(updates_label);
		panel.add(updates);
		if (linkable) {
			updates.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					MainController.getInstance().getCurrentPersonaController()
							.getTwitterUpdatesListController().activateSearch(
									"from:" + user.getTwitterScreenName());

				}

			});

		}

		return panel;
	}

	public static String buildUserBioHtml(TwitterUserDTO user) {
		String userBio = "<span class=\"userBio\">"
				+ user.getTwitterDescription() + "</span>";
		return userBio;
	}

	public static String buildUserHomePageHtml(TwitterUserDTO user) {
		String userUrl = "<a href=\"" + user.getTwitterWeb()
				+ "\" target=\"_blank\" class=\"userWeb\">"
				+ user.getTwitterWeb() + "</a>";

		return userUrl;
	}

	public static String buildLocationHtml(TwitterUserDTO user) {
		String location = "Location: <span class=\"userLocation\">"
				+ user.getTwitterLocation() + "</span>";

		return location;
	}

	public static String buildUserLineHtml(TwitterUserDTO user) {
		String userNamePlusUserScreeName = "<span class=\"userName\">"
				+ user.getTwitterName()
				+ "</span> (<span class=\"userScreenName\">"
				+ user.getTwitterScreenName() + "</span>)";
		// add imge follow (ok)
		return userNamePlusUserScreeName;
	}

	public static Widget buildUserActivityPanel(TwitterAccountDTO account) {

		StringBuffer htmlB = new StringBuffer();
		htmlB
				.append("<span class=\"userNewFollowers\">New Followers:<span class=\"bolder\">"
						+ account.getNewFollowers() + "</span></span> ");
		htmlB
				.append("<span class=\"userNewFollowers\">New Friends:<span class=\"bolder\">"
						+ account.getNewFriends() + "</span></span> ");
		/*htmlB
				.append("<span class=\"userNewFollowers\">New Blocking:<span class=\"bolder\">"
						+ account.getNewBlocking() + "</span></span> ");*/
		InlineHTML html = new InlineHTML(htmlB.toString());
		return html;
	}

	public static Widget buildUserRateLimitPanel(TwitterAccountDTO account) {
		StringBuffer htmlB = new StringBuffer();

		htmlB
				.append("<span class=\"rateLimitLimit\">Rate Limit limit:<span class=\"bolder\">"
						+ account.getRateLimits().getRateLimitLimit()
						+ "</span></span> ");
		htmlB
				.append("<span class=\"rateLimitRemaining\">Remainding API calls:<span class=\"bolder\">"
						+ account.getRateLimits().getRateLimitRemaining()
						+ "</span></span> ");
		
		long minutes = (account.getRateLimits().getRateLimitReset()/1000)/60;
		htmlB
				.append("<span class=\"userNewFollowers\">Next Reset in<span class=\"bolder\">"
						+ minutes + " minutes</span></span> ");

		InlineHTML html = new InlineHTML(htmlB.toString());

		return html;
	}

}
