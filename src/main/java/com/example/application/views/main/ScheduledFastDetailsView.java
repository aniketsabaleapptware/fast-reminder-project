package com.example.application.views.main;

import com.example.application.entities.FastData;
import com.example.application.services.impl.FastDataServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Objects;
import org.springframework.context.annotation.Scope;

@PageTitle("Scheduled Fasts")
@Route("scheduled-fast-details")
@Scope("prototype")
public class ScheduledFastDetailsView extends VerticalLayout implements HasUrlParameter<Integer> {

  private final transient FastDataServiceImpl fastDataServiceImpl;

  private final Grid<FastData> grid = new Grid<>(FastData.class);
  private final FastDataForm fastDataForm;
  private Integer userId;

  public ScheduledFastDetailsView(FastDataServiceImpl fastDataServiceImpl) {
    this.fastDataServiceImpl = fastDataServiceImpl;
    this.fastDataForm = new FastDataForm(fastDataServiceImpl);
    setSizeFull();
    configureGrid();
    configureForm();
    add(getToolbar(), getContent());
  }

  private void configureGrid() {
    grid.setSizeFull();
    grid.setColumns("fastType", "phases","day", "schedule");
    grid.addComponentColumn(this::createEditButton);
  }

  private Button createEditButton(FastData fastData) {
    Button editButton = new Button("Edit");
    editButton.addClickListener(event -> {
      fastDataForm.setFastData(fastData);
      fastDataForm.setVisible(true);
    });
    return editButton;
  }

  private Component getContent() {
    HorizontalLayout content = new HorizontalLayout(grid, fastDataForm);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, fastDataForm);
    content.setSizeFull();
    return content;
  }

  private void configureForm() {
    fastDataForm.setWidth("25em");
    fastDataForm.setVisible(false);
  }

  private Component getToolbar() {
    return new HorizontalLayout();
  }

  private void refreshGrid() {
    List<FastData> fastDataList = fastDataServiceImpl.findAllActiveFastsByUserId(userId);
    grid.setItems(fastDataList);
  }

  @Override
  public void setParameter(BeforeEvent beforeEvent, Integer userId) {
    this.userId = userId;
    try {
      if (Objects.isNull(userId)) {
        Notification.show("User ID is missing!", 3000, Notification.Position.BOTTOM_CENTER);
        return;
      }
      List<FastData> fastDataList = fastDataServiceImpl.findAllActiveFastsByUserId(userId);
      if (Objects.nonNull(fastDataList) && !fastDataList.isEmpty()) {
        grid.setItems(fastDataList);
      } else {
        Notification.show("Fast data not found", 3000, Notification.Position.BOTTOM_CENTER);
      }
    } catch (Exception e) {
      Notification.show("Error retrieving fast data for user", 3000, Notification.Position.BOTTOM_CENTER);
    }
  }
}
