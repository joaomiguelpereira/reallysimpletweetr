package org.nideasystems.webtools.zwitrng.client.view.configuration;



import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MultiSelectListWidget extends VerticalPanel {

	
	private String listBoxesWidth="11em";
	private int visibleItemCount = 5;
	
	
	private InlineHTML selectedItemsTitle;
	private ListBox selectedItems;
	private InlineHTML availableItemsTitle;	
	private ListBox availableItems;
	private List<String> availableItemsList;
	private List<String> selectedItemsList;
	
	
	public MultiSelectListWidget() {
		availableItemsList = new ArrayList<String>();
		selectedItemsList = new ArrayList<String>();
		

		FlexTable table = new FlexTable();
		selectedItemsTitle = new InlineHTML("Selected Items");
		selectedItems = new ListBox(true);

		selectedItems.setWidth(listBoxesWidth);
		selectedItems.setVisibleItemCount(5);
		
		table.setWidget(0, 0, selectedItemsTitle);
		table.setWidget(1, 0, selectedItems);

		VerticalPanel itemsOptions = new VerticalPanel();
		itemsOptions.setWidth("5em");
		InlineHTML addSelected = new InlineHTML("< Add");
		InlineHTML removeSelected = new InlineHTML(" Remove >");
		
		
		itemsOptions.add(addSelected);
		itemsOptions.setCellHorizontalAlignment(addSelected, HasHorizontalAlignment.ALIGN_CENTER);
		
		itemsOptions.add(new InlineHTML(""));
		itemsOptions.add(removeSelected);
		itemsOptions.setCellHorizontalAlignment(removeSelected, HasHorizontalAlignment.ALIGN_CENTER);
		addSelected.addStyleName("link");
		removeSelected.addStyleName("link");

		table.setWidget(0, 1, new InlineHTML(""));
		table.setWidget(1, 1, itemsOptions);
		
		availableItemsTitle = new InlineHTML(
				"Available Items");
		
		availableItems = new ListBox(true);
		availableItems.setWidth(listBoxesWidth);
		availableItems.setVisibleItemCount(visibleItemCount);
		

		table.setWidget(0, 2, availableItemsTitle);
		table.setWidget(1, 2, availableItems);
		this.add(table);
		addSelected.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addSelected();
				
			}

			
			
		});
		removeSelected.addClickHandler(new ClickHandler()  {

			@Override
			public void onClick(ClickEvent event) {
				removeSelected();
				
			}

			
			
		});

	}

	private void addSelected() {
		
		for (int i=0; i<this.availableItems.getItemCount();i++) {
			if ( this.availableItems.isItemSelected(i)) {
				this.selectedItemsList.add(this.availableItems.getItemText(i));
				this.availableItemsList.remove(this.availableItems.getItemText(i));
			}
		}
		synchLists();
	}
	
	private void removeSelected() {
		for (int i=0; i<this.selectedItems.getItemCount();i++) {
			if ( this.selectedItems.isItemSelected(i)) {
				this.availableItemsList.add(this.selectedItems.getItemText(i));
				this.selectedItemsList.remove(this.selectedItems.getItemText(i));
			}
		}
		synchLists();
		
	}
	
	private void synchLists() {
		this.availableItems.clear();
		this.selectedItems.clear();
		
		for (String str: availableItemsList ) {
			this.availableItems.addItem(str);
		}


		for (String str: selectedItemsList) {
			this.selectedItems.addItem(str);
		}
		

	}
	
	public String getListBoxesWidth() {
		return listBoxesWidth;
	}


	public void setListBoxesWidth(String listBoxesWidth) {
		this.listBoxesWidth = listBoxesWidth;
		this.availableItems.setWidth(this.listBoxesWidth);
		this.selectedItems.setWidth(this.listBoxesWidth);
	}


	public int getVisibleItemCount() {
		return visibleItemCount;
		
	}

	public void setVisibleItemCount(int visibleItemCount) {
		this.visibleItemCount = visibleItemCount;
		this.availableItems.setVisibleItemCount(this.visibleItemCount);
		this.selectedItems.setVisibleItemCount(this.visibleItemCount);
	}

	public void setSelectedItemsTitle(String selectedItemsTitle) {
		this.selectedItemsTitle.setHTML(selectedItemsTitle);
		
	}

	public void setAvailableItemsTitle(String selectedItemsTitle) {
		this.availableItemsTitle.setHTML(selectedItemsTitle);
		
	}


	public void setAvailableItemsList(List<String> availableItemsList) {
		this.availableItemsList = availableItemsList;
		synchLists();
	}


	public List<String> getAvailableItemsList() {
		return availableItemsList;
	}


	public void setSelectedItemsList(List<String> selectedItemsList) {
		this.selectedItemsList = selectedItemsList;
		synchLists();
	}


	public List<String> getSelectedItemsList() {
		return selectedItemsList;
	}

	
}
