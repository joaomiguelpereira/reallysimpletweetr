package org.nideasystems.webtools.zwitrng.server.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static com.googlecode.charts4j.Color.*;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.AuthorizationManager;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.jobs.Job;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;


public class FFStatsServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -441373369273496044L;
	StringBuffer outBuffer = null;
	private User loggedUser;
	private static int MAX_POINTS = 8;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		outBuffer = new StringBuffer();

		UserService userService = UserServiceFactory.getUserService();
		// Check if the user is logged in with a

		try {
			this.loggedUser = AuthorizationManager.checkAuthentication();
		} catch (Exception e) {
			String loginLink = "<a href=\""
					+ userService.createLoginURL(req.getRequestURI())
					+ "\">login</a>";
			throw new ServletException("Please login " + loginLink);
		}
		try {
			startTransaction(true);
		} catch (Exception e1) {
			throw new ServletException(e1);

		}

		// get the personas in active state
		List<PersonaDO> personas = null;

		try {
			personas = getBusinessHelper().getPersonaDao().findAllPersonas(
					loggedUser.getEmail());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new ServletException(e1);
		}

		//Add it to cache
		Cache cache = null;
		try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        } catch (CacheException e) {
            throw new ServletException(e);
        }
        //Try to get the list from cahc
        List<Job> jobList = (List<Job>)cache.get("jobs");
        
        int size = jobList!=null?jobList.size():-1;
		outBuffer.append("<h1> Jobs Queue Size>" + size+ "</h1>");
		for (PersonaDO persona : personas) {
			try {
				// update following count for the period
				/*
				 * List<Integer> historicalFollowing =
				 * persona.getTwitterAccount() .getHistoricalFollowing();
				 * List<Integer> historicalFollowers =
				 * persona.getTwitterAccount() .getHistoricalFollowers();
				 * 
				 * 
				 * if (historicalFollowers == null) { historicalFollowers = new
				 * ArrayList<Integer>(); } if (historicalFollowing == null) {
				 * historicalFollowing = new ArrayList<Integer>(); }
				 */

				int autoFollowedCount = 0;
				int autoUnfollowedCount = 0;
				int followBackQueueSize = 0;
				int followSearchQueueSize = 0;
				int unfollowBackQueueSize = 0;
				float ratio = 0;
				if (persona.getTwitterAccount().getAutoFollowedScreenNames() != null) {
					autoFollowedCount = persona.getTwitterAccount()
							.getAutoFollowedScreenNames().size();
				}
				if (persona.getTwitterAccount().getAutoFollowBackIdsQueue() != null) {
					followBackQueueSize = persona.getTwitterAccount()
							.getAutoFollowBackIdsQueue().size();
				}
				if (persona.getTwitterAccount().getAutoFollowedCount() != null) {
					autoFollowedCount = autoFollowedCount
							+ persona.getTwitterAccount()
									.getAutoFollowedCount();
				}
				if (persona.getTwitterAccount().getAutoFollowScreenNamesQueue() != null) {
					followSearchQueueSize = persona.getTwitterAccount()
							.getAutoFollowScreenNamesQueue().size();
				}

				ratio = new Float(persona.getTwitterAccount().getFollowingIds()
						.size())
						/ new Float(persona.getTwitterAccount()
								.getFollowersIds().size());

				outBuffer.append("<h1> Stats for " + persona.getName()
						+ "</h1>");
				outBuffer.append("<div>Followers: "
						+ persona.getTwitterAccount().getFollowersIds().size()
						+ "</div>");
				outBuffer.append("<div>Following: "
						+ persona.getTwitterAccount().getFollowingIds().size()
						+ "</div>");
				outBuffer.append("<div>Ratio " + ratio + "</div>");

				outBuffer.append("<div>Auto Followed Count: "
						+ autoFollowedCount + "</div>");
				outBuffer.append("<div>Auto Follow Back Queue size: "
						+ followBackQueueSize + "</div>");
				outBuffer.append("<div>Auto Follow on Search Queue size: "
						+ followSearchQueueSize + "</div>");

				outBuffer.append("<div>Auto Unfollowed Count: "
						+ autoUnfollowedCount + "</div>");
				outBuffer.append("<div>Auto Unfollow Queue Size: "
						+ unfollowBackQueueSize + "</div>");

				// get the min of both

				// create chart
				outBuffer.append(renderChart(persona));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				e.printStackTrace();
			}
		}

		endTransaction();

		resp.setContentType("text/html");

		resp.getWriter().println(outBuffer.toString());

	}

	private String renderChart(PersonaDO persona) {
		StringBuffer sb = new StringBuffer();

		List<Integer> following = persona.getTwitterAccount()
				.getHistoricalFollowing();
		List<Integer> followers = persona.getTwitterAccount()
				.getHistoricalFollowers();

		MAX_POINTS = following.size() < following.size() ? followers.size()
				: followers.size();

		if (MAX_POINTS > 30) {
			MAX_POINTS = 30;
		}
		if (followers == null || followers == null) {
			sb.append("NO DATA");
			return sb.toString();
		}

		List<Float> followingSeries = getSeries(following);
		List<Float> followersSeries = getSeries(followers);
		float maxFollowers = getMax(followersSeries);
		float maxFollowing = getMax(followingSeries);
		float minFollowing = getMin(followingSeries);

		float minFollowers = getMin(followersSeries);

		float min = minFollowers < minFollowing ? minFollowers : minFollowing;
		float max = maxFollowers > maxFollowing ? maxFollowers : maxFollowing;

		StringBuffer chartBuffer = new StringBuffer();

		// Defining lines

		// EXAMPLE CODE START

		// Defining lines

		final double[] followingChart = new double[MAX_POINTS];
		final double[] followerChart = new double[MAX_POINTS];
		for (int i = 0; i < MAX_POINTS; i++) {
			// competition[i] = 100-(Math.cos(30*i*Math.PI/180)*10 + 50)*i/20;
			// mywebsite[i] = (Math.cos(30*i*Math.PI/180)*10 + 50)*i/20;
			followingChart[i] = (followingSeries.get(MAX_POINTS - 1 - i) / max) * 100;
			followerChart[i] = (followersSeries.get(MAX_POINTS - 1 - i) / max) * 100;
		}
		Line line1 = Plots.newLine(Data.newData(followingChart), Color
				.newColor("CA3D05"), "Following");
		// Line line1 = Plots.newLine(Data.newData(100,200,300,400),
		// Color.newColor("CA3D05"), "My Website.com");
		line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
		//line1.addShapeMarkers(Shape.DIAMOND, Color.newColor("CA3D05"), 12);
		//line1.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
		Line line2 = Plots.newLine(Data.newData(followerChart), SKYBLUE,
				"Followers");
		// Line line2 = Plots.newLine(Data.newData(20,323,44,555), SKYBLUE,
		// "Competition.com");
		line2.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
		//line2.addShapeMarkers(Shape.DIAMOND, SKYBLUE, 12);
		//line2.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);

		// Defining chart.
		LineChart chart = GCharts.newLineChart(line1, line2);
		chart.setSize(600, 450);
		chart.setTitle("Following/Followers", WHITE, 14);
		chart.addHorizontalRangeMarker(40, 60, Color.newColor(RED, 30));
		chart.addVerticalRangeMarker(70, 90, Color.newColor(GREEN, 30));
		chart.setGrid(25, 25, 3, 2);

		// Defining axis info and styles
		AxisStyle axisStyle = AxisStyle.newAxisStyle(WHITE, 12,
				AxisTextAlignment.CENTER);
		AxisLabels xAxis = AxisLabelsFactory.newAxisLabels("P1", "P2", "P3",
				"P4", "P5");
		xAxis.setAxisStyle(axisStyle);
				AxisLabels yAxis = AxisLabelsFactory.newAxisLabels(createYAxis(min, max));
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Period", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14,
				AxisTextAlignment.CENTER));
		yAxis.setAxisStyle(axisStyle);
		AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Tweeters", 50.0);
		yAxis2.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14,
				AxisTextAlignment.CENTER));
		yAxis2.setAxisStyle(axisStyle);

		// Adding axis info to chart.
		chart.addXAxisLabels(xAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis);
		chart.addYAxisLabels(yAxis2);

		// Defining background and chart fills.
		chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("1F1D1D")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, Color
				.newColor("363433"), 100);
		fill.addColorAndOffset(Color.newColor("2E2B2A"), 0);
		chart.setAreaFill(fill);
		String url = chart.toURLString();

		chartBuffer.append("<img src=\"");
		chartBuffer.append(url);
		chartBuffer.append("\"/>");
		/*
		 * chartBuffer.append("<img src=\"http://chart.apis.google.com/chart");
		 * 
		 * chartBuffer.append("?chs=350x200");
		 * 
		 * outBuffer
		 * .append("&amp;chtt=Comparison+of+last+2+weeks|following/followers");
		 * chartBuffer.append("&amp;cht=lxy");
		 * chartBuffer.append("&amp;chts=000011,10");
		 * chartBuffer.append("&amp;chg=16.6,25,1,5");
		 * chartBuffer.append("&amp;chm=R,000000,0,0.66,0.67");
		 * chartBuffer.append("&amp;chxt=x,y");
		 * chartBuffer.append("&amp;chdl=following|followers");
		 * chartBuffer.append("&amp;chxl=0:|Mon|Tue|Wed|Thu|Fri|Sat|Sun|1:|" +
		 * yAxisString.toString()); // outBuffer //
		 * chartBuffer.append("&amp;chd=t:" + followersSeriesString.toString() +
		 * "|" + followingSeriesString.toString());
		 * 
		 * 
		 * 
		 * 
		 * chartBuffer.append("&amp;chd=t:"+followersSeriesString.toString()+"|"+
		 * followingSeriesString.toString());
		 * chartBuffer.append("&amp;chds=100,555");
		 * 
		 * chartBuffer.append("&amp;cht=lc");
		 * chartBuffer.append("&amp;chco=ff0000,0000ff\">");
		 */

		sb.append(chartBuffer.toString());
		return sb.toString();

	}

	private String[] createYAxis(float min, Float max) {
		
		StringBuffer sb = new StringBuffer();
		int slop = Math.round((max-min)/MAX_POINTS);
		String[] ret = new String[MAX_POINTS];
		for (int i=0;i<MAX_POINTS;i++) {
			ret[i]= min+slop*i+"";
		}
		return ret;
		

	}

	private List<Float> getSeries(List<Integer> following) {
		List<Float> ret = new ArrayList<Float>();

		for (int i = 0; i < MAX_POINTS; i++) {

			ret.add(new Float(following.get(following.size() - i - 1)));

		}

		return ret;
	}

	private float getMax(List<Float> list) {
		float max = 0;

		for (float n : list) {
			if (n > max) {
				max = n;
			}

		}
		return max;
	}

	private float getMin(List<Float> list) {

		float min = 999999999;

		for (float n : list) {

			if (n < min) {
				min = n;
			}
		}
		return min;
	}
}
