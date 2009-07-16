package org.nideasystems.webtools.zwitrng.client.view.updates;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesController;
import org.nideasystems.webtools.zwitrng.client.view.AbstractVerticalPanelView;
import org.nideasystems.webtools.zwitrng.shared.UpdatesType;
import org.nideasystems.webtools.zwitrng.shared.model.FilterCriteriaDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;


public class TwitterUpdatesView extends
		AbstractVerticalPanelView<TwitterUpdatesController> {
	private final TwitterUpdatesToolWidget toolBar = new TwitterUpdatesToolWidget();

	private Image waitingImage = new Image(Constants.WAITING_IMAGE);
	private TwitterUpdatesSearchToolWidget searchWidget = null;
	private DirectMessagesSearchToolWidget dmSearchToolWidget = null;
	VerticalPanel toolsPanel = null;
	private VerticalPanel contentsPanel = null;

	private InlineHTML currentPageHtml = null;
	private InlineHTML previousPageLink = null;
	private InlineHTML nextPageLink = null;

	private InlineHTML topCurrentPageHtml = null;
	private InlineHTML topPreviousPageLink = null;
	private InlineHTML topNextPageLink = null;
	private FilterCriteriaDTO currentFilter = null;
	private InlineHTML loadMoreLink = null;

	@Override
	public void init() {
		createTools();

	}

	private void createTools() {
		toolsPanel = new TwitterUpdatesSearchToolWidget();

		final HorizontalPanel toolContainer = new HorizontalPanel();
		// Add common
		toolBar.setController(getController());
		toolBar.init();
		toolContainer.add(toolBar);
		waitingImage.setVisible(false);
		toolContainer.add(waitingImage);
		toolsPanel.add(toolContainer);
		
		// Add searches
		if (currentFilter.getUpdatesType() == UpdatesType.SEARCHES) {

			searchWidget = new TwitterUpdatesSearchToolWidget();
			searchWidget.setCurrentFiler(currentFilter);
			searchWidget.setController(getController());
			searchWidget.init();
			toolsPanel.add(searchWidget);

		}
		this.add(toolsPanel);
		
		if (currentFilter.getUpdatesType() == UpdatesType.DIRECT_RECEIVED
				|| currentFilter.getUpdatesType() == UpdatesType.DIRECT_SENT) {
			// Add the widget
			dmSearchToolWidget = new DirectMessagesSearchToolWidget();
			dmSearchToolWidget.setCurrentFilter(currentFilter);
			dmSearchToolWidget.setController(getController());
			dmSearchToolWidget.init();
			toolsPanel.add(dmSearchToolWidget);

			this.add(createTopPaginPanel());

		}
		
		
		contentsPanel = new VerticalPanel();
		this.add(contentsPanel);

		// final HorizontalPanel pagingOptions = createPaginPanel();
		if (currentFilter.getUpdatesType() == UpdatesType.DIRECT_RECEIVED
				|| currentFilter.getUpdatesType() == UpdatesType.DIRECT_SENT) {
			this.add(createPagingPanel());
			
		} else {
			loadMoreLink = createLoadMorePanel();
			this.add(loadMoreLink);
		}

	}

	private InlineHTML createLoadMorePanel() {

		

		InlineHTML loadMoreLink = new InlineHTML("See older tweets?");
		loadMoreLink.addStyleName("link");
		
		loadMoreLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getController().loadMoreUpdates();
				
			}
			
		});
		return loadMoreLink;
	}

	private HorizontalPanel createTopPaginPanel() {
		HorizontalPanel pagingOptions = new HorizontalPanel();
		pagingOptions.setSpacing(5);

		topPreviousPageLink = new InlineHTML("Previous");
		topPreviousPageLink.setVisible(false);
		topPreviousPageLink.addStyleName("link");
		pagingOptions.add(topPreviousPageLink);

		topCurrentPageHtml = new InlineHTML("Page " + currentFilter.getPage());
		pagingOptions.add(topCurrentPageHtml);

		topNextPageLink = new InlineHTML("Next");
		topNextPageLink.addStyleName("link");

		topPreviousPageLink.addClickHandler(new PreviousPageClickHandler());

		topNextPageLink.addClickHandler(new NextPageClickHandler());
		pagingOptions.add(topNextPageLink);

		return pagingOptions;
	}

	private HorizontalPanel createPagingPanel() {
		HorizontalPanel pagingOptions = new HorizontalPanel();
		pagingOptions.setSpacing(5);
		previousPageLink = new InlineHTML("Previous");
		previousPageLink.setVisible(false);
		previousPageLink.addStyleName("link");
		pagingOptions.add(previousPageLink);

		currentPageHtml = new InlineHTML("Page " + currentFilter.getPage());
		pagingOptions.add(currentPageHtml);

		nextPageLink = new InlineHTML("Next");
		nextPageLink.addStyleName("link");

		previousPageLink.addClickHandler(new PreviousPageClickHandler());

		nextPageLink.addClickHandler(new NextPageClickHandler());
		pagingOptions.add(nextPageLink);

		return pagingOptions;
	}

	private class PreviousPageClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			getController().changePage(currentFilter.getPage() - 1);

		}

	}

	private class NextPageClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			getController().changePage(currentFilter.getPage() + 1);

		}

	}

	private void updatePaging() {

		if (currentFilter.getUpdatesType().equals(UpdatesType.DIRECT_RECEIVED)
				|| currentFilter.getUpdatesType().equals(
						UpdatesType.DIRECT_SENT)) {
			if (currentFilter.getPage() > 1) {
				previousPageLink.setVisible(true);
				topPreviousPageLink.setVisible(true);
			} else {
				previousPageLink.setVisible(false);
				topPreviousPageLink.setVisible(false);

			}
			currentPageHtml.setHTML("Page " + currentFilter.getPage());
			topCurrentPageHtml.setHTML("Page " + currentFilter.getPage());

		}

	}

	public void refresh() {
		if (currentFilter.getUpdatesType() == UpdatesType.SEARCHES) {
			searchWidget.refresh();
		}
		this.loadMoreLink.setVisible(true);
		updatePaging();

	}

	public FilterCriteriaDTO getCurrentFilter() {
		return currentFilter;
	}

	public void setCurrentFilter(FilterCriteriaDTO currentFilter) {
		this.currentFilter = currentFilter;
	}

	@Override
	public void isUpdating(boolean isUpdating) {
		waitingImage.setVisible(isUpdating);

	}

	public void addUpdate(TwitterUpdateWidget updateWidget, int pos) {
		this.contentsPanel.insert(updateWidget, pos);

	}

	public void addUpdate(TwitterUpdateWidget updateWidget) {
		this.contentsPanel.add(updateWidget);

	}

	public int getUpdateCount() {

		return this.contentsPanel.getWidgetCount();
	}

	public void removeUpdate(TwitterUpdateWidget updateWidget) {
		this.contentsPanel.remove(updateWidget);

	}

	public TwitterUpdateWidget getUpdateWidget(int i) {

		return (TwitterUpdateWidget) this.contentsPanel.getWidget(i);
	}

}
