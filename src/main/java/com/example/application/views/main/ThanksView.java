package com.example.application.views.main;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("thanks")
@PageTitle("Thanks")
public class ThanksView extends VerticalLayout {

  public ThanksView(){

    FormLayout formLayout = new FormLayout();
    formLayout.setWidth("50%");
    formLayout.setMaxWidth("600px");

    H3 header = new H3("Thanks for using the fast reminder service, you will get the reminder as scheduled!");
    header.getStyle().set("text-align", "center");

    formLayout.add(header);

    VerticalLayout centeredLayout = new VerticalLayout();
    centeredLayout.setSizeFull();
    centeredLayout.setAlignItems(Alignment.CENTER);
    centeredLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    centeredLayout.getStyle().set("padding-left", "800px");

    centeredLayout.add(formLayout);

    add(centeredLayout);
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
  }
}
