package org.nideasystems.webtools.zwitrng.server.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;

public class FFStatsServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -441373369273496044L;
	StringBuffer outBuffer = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		outBuffer = new StringBuffer();
		try {
			startTransaction(true);
		} catch (Exception e1) {
			throw new ServletException(e1);

		}

		// get the personas in active state
		List<PersonaDO> personas = getBusinessHelper().getPersonaDao()
				.findAllActivePersonas();

		for (PersonaDO persona : personas) {
			try {
				// update following count for the period
				List<Integer> historicalFollowing = persona.getTwitterAccount()
						.getHistoricalFollowing();
				List<Integer> historicalFollowers = persona.getTwitterAccount()
						.getHistoricalFollowers();

				if (historicalFollowers == null) {
					historicalFollowers = new ArrayList<Integer>();
				}
				if (historicalFollowing == null) {
					historicalFollowing = new ArrayList<Integer>();
				}

				outBuffer.append("<h1> Stats for " + persona.getName() + "</h1>");
				outBuffer.append("<div>Followers: "+persona.getTwitterAccount().getFollowersIds().size()+"</div>");
				outBuffer.append("<div>Following: "+persona.getTwitterAccount().getFollowingIds().size()+"</div>");
				outBuffer.append("<div>Auto Followed Count: "+persona.getTwitterAccount().getAutoFollowedScreenNames()!=null?persona.getTwitterAccount().getAutoFollowedScreenNames().size():"Not Set"+"</div>");
				outBuffer.append("<div>Auto Follow Back: "+persona.getTwitterAccount().getAutoFollowedCount()!=null?persona.getTwitterAccount().getAutoFollowedCount():"Not Set"+"</div>");
				outBuffer.append("<div>Auto Unfollow Back: "+persona.getTwitterAccount().getAutoUnfollowedIds()!=null?persona.getTwitterAccount().getAutoUnfollowedIds().size():"Not Set"+"</div>");
				
				outBuffer.append("<div>Auto Follow Back Queue Size : "+persona.getTwitterAccount().getAutoFollowBackIdsQueue()!=null?persona.getTwitterAccount().getAutoFollowBackIdsQueue().size():"Not Set"+"</div>");
				outBuffer.append("<div>Auto UnFollow Back Queue Size : "+persona.getTwitterAccount().getAutoUnFollowBackIdsQueue()!=null?persona.getTwitterAccount().getAutoUnFollowBackIdsQueue().size():"Not Set"+"</div>");
				outBuffer.append("<div>Auto Follow Queue Size : "+persona.getTwitterAccount().getAutoFollowScreenNamesQueue()!=null?persona.getTwitterAccount().getAutoFollowScreenNamesQueue().size():"Not Set"+"</div>");
				
				//get the min of both
				
				int minFollowing = 999999999;
				int maxFollowing = 0;
				
				int minFollowers = 999999999;
				int maxFollowers = 0;
				
				StringBuffer following = new StringBuffer();
				StringBuffer followers = new StringBuffer();
				
				for (int i = 0; i < historicalFollowing.size(); i++) {
					if ( historicalFollowing.get(i) < minFollowing  ) {
						minFollowing = historicalFollowing.get(i);
					}
					if ( historicalFollowing.get(i) > maxFollowing  ) {
						maxFollowing = historicalFollowing.get(i);
					}
					if (i<8) {
						following.append(historicalFollowing.get(i)-i*10);
						if ( i<historicalFollowing.size()-1 && i<7 ) {
							following.append(",");
						}	
					}
				}
				


				outBuffer.append("<div>Followers:" + historicalFollowers.size()
						+ "</div>");

				for (int i = 0; i < historicalFollowers.size(); i++) {

					
					if ( historicalFollowers.get(i) < minFollowers) {
						minFollowers = historicalFollowers.get(i);
					}
					if ( historicalFollowers.get(i) > maxFollowers) {
						maxFollowers = historicalFollowers.get(i);
					}
					if (i<8) {
						followers.append(historicalFollowers.get(i));
						if ( i<historicalFollowers.size()-1 &&  i<7) {
							followers.append(",");
						}
							
					}
				}
				int slop = 0;
				int start = 0;
				if ( maxFollowers>maxFollowing) {
					slop = ((maxFollowers-minFollowers)+200)/6;
					
				} else {
					slop = ((maxFollowing-minFollowing)+200)/6;
					
				}
				start = minFollowers<minFollowing?minFollowers:minFollowing;
				
				StringBuffer yAxis = new StringBuffer();
				outBuffer.append("Slop:"+slop+";");
				
				outBuffer.append((start-slop)+";");
				
				yAxis.append((start-slop)+"|");
				for (int i=1;i<9;i++) {
					yAxis.append((start+slop*i));
					if ( i<8) {
						yAxis.append("|");
					}
				}
				
				
				
				
				outBuffer.append("<div>yAxis:"+yAxis.toString()+"</div>");
				outBuffer.append("<div>Following:"+following.toString()+"</div>");
				outBuffer.append("<div>Followers"+followers.toString()+"</div>");

				outBuffer
						.append("<img src=\"http://chart.apis.google.com/chart");

				outBuffer.append("?chs=350x200");
				outBuffer
						.append("&amp;chtt=Comparison+of+last+2+weeks|following/followers");
				outBuffer.append("&amp;chts=000011,10");
				outBuffer.append("&amp;chg=16.6,25,1,5");
				outBuffer.append("&amp;chm=R,000000,0,0.66,0.67");
				outBuffer.append("&amp;chxt=x,y");
				outBuffer.append("&amp;chdl=following|followers");
				outBuffer
						.append("&amp;chxl=0:|Mon|Tue|Wed|Thu|Fri|Sat|Sun|1:|"+yAxis.toString());
				//outBuffer
				//		.append("&amp;chd=t:"+followers.toString()+"|"+following.toString());

				outBuffer
				.append("&amp;chd=t:100,200,400,500|800,292,444,555");
				outBuffer
				.append("&amp;chds=100,555");

				outBuffer.append("&amp;cht=lc");
				outBuffer.append("&amp;chco=ff0000,0000ff\">");
				
				outBuffer.append("</hr>");

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

}
