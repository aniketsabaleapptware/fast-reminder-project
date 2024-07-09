package com.example.application.views.main;

import com.example.application.entities.User;
import com.example.application.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@PageTitle("Main")
@Route(value = "")
public class MainView extends VerticalLayout {

  private final Binder<User> userBinder = new Binder<>(User.class);

  ObjectMapper objectMapper = new ObjectMapper();

  public MainView(UserServiceImpl userServiceImpl) {
    TextField userNameField = new TextField("User Name");
    userBinder.forField(userNameField)
        .asRequired("")
        .bind(User::getUsername, User::setUsername);

    TextField contactNumberField = new TextField("Contact Number");
    userBinder.forField(contactNumberField)
        .asRequired("")
        .withValidator(contact -> contact.length() == 10 && contact.matches("\\d+"), "Contact Number must be exactly 10 digits")
        .withConverter(new StringToLongConverter("Must enter a number"))
        .bind(User::getContactNumber, User::setContactNumber);

    Button submitButton = new Button("Submit");
    submitButton.addClickListener(click -> {
      User user = new User();
      try {
        userBinder.writeBean(user);
        User existingUser = userServiceImpl.findByContactNumber(user.getContactNumber());
        if (Objects.nonNull(existingUser)) {
          Notification.show("User already exists", 3000, Notification.Position.BOTTOM_CENTER);
          String userJson = objectMapper.writeValueAsString(existingUser);
          UI.getCurrent().navigate(FastDataView.class, "user=" + URLEncoder.encode(userJson, StandardCharsets.UTF_8));
        } else {
          User saveduser = userServiceImpl.saveUser(user);
          Notification.show("User register successfully!", 3000, Notification.Position.BOTTOM_CENTER);
          String userJson = objectMapper.writeValueAsString(saveduser);
          UI.getCurrent().navigate(FastDataView.class, "user=" + URLEncoder.encode(userJson, StandardCharsets.UTF_8));
        }
      } catch (Exception ex) {
        Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
      }
    });

    FormLayout formLayout = new FormLayout();
    formLayout.setWidth("400px");
    H1 loginHeader = new H1("Login Form");
    formLayout.add(loginHeader);
    formLayout.setColspan(loginHeader, 2);
    formLayout.add(userNameField, contactNumberField, submitButton);
    formLayout.setColspan(submitButton, 2);

    submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    submitButton.addClickShortcut(Key.ENTER);


    VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setSizeFull();
    mainLayout.setAlignItems(Alignment.CENTER);
    mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    mainLayout.getStyle().set("padding-left", "800px");
    mainLayout.add(formLayout);

    add(mainLayout);
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

  }
}
