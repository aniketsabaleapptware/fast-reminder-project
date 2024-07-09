package com.example.application.views.main;

import com.example.application.entities.FastData;
import com.example.application.entities.User;
import com.example.application.enums.FastType;
import com.example.application.enums.Phases;
import com.example.application.enums.Schedule;
import com.example.application.enums.WeekDays;
import com.example.application.repository.FastDataRepository;
import com.example.application.services.impl.FastDataServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Stream;

@PageTitle("Fast-Details")
@Route("/fast-details")
public class FastDataView extends VerticalLayout implements HasUrlParameter<String> {

  private final transient FastDataRepository fastDataRepository;
  private final transient FastDataServiceImpl fastDataServiceImpl;
  private transient User user;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final Binder<FastData> fastDataBinder = new Binder<>(FastData.class);

  private final ComboBox<FastType> fastTypeComboBox = new ComboBox<>("Select Fast Type");
  private final ComboBox<Phases> phasesComboBox = new ComboBox<>("Select Phase");
  private final ComboBox<WeekDays> dayComboBox = new ComboBox<>("Select Days");
  private final ComboBox<Schedule> recurrenceComboBox = new ComboBox<>("Schedule");
  private final Button submitButton = new Button("Submit");

  private final Button scheduledFastBotton = new Button("Scheduled Fast");


  public FastDataView(FastDataRepository fastDataRepository, FastDataServiceImpl fastDataServiceImpl) {
    this.fastDataRepository = fastDataRepository;
    this.fastDataServiceImpl = fastDataServiceImpl;
    initializeComponents();
    setupForm();
  }

  private void initializeComponents() {
    dayComboBox.setItems(Stream.of(WeekDays.values())
        .filter(day -> day != WeekDays.NONE)
        .toList());
    dayComboBox.setItemLabelGenerator(WeekDays::getDisplayName);

    recurrenceComboBox.setItems(Schedule.values());
    recurrenceComboBox.setItemLabelGenerator(Schedule::getDisplayName);

    fastTypeComboBox.setItems(FastType.values());
    fastTypeComboBox.setItemLabelGenerator(FastType::getDisplayName);

    phasesComboBox.setItems(Stream.of(Phases.values())
        .filter(phase -> phase != Phases.NONE)
        .toList());
    phasesComboBox.setItemLabelGenerator(Phases::getDisplayName);

    fastTypeComboBox.addValueChangeListener(this::onFastTypeChange);
  }

  public void setupForm() {
    fastDataBinder.forField(fastTypeComboBox)
        .asRequired("Fast type is required")
        .bind(FastData::getFastType, FastData::setFastType);

    fastDataBinder.forField(dayComboBox)
        .bind(FastData::getDay, (fastData, value) -> fastData.setDay(value != null ? WeekDays.valueOf(String.valueOf(value)) : WeekDays.NONE));

    fastDataBinder.forField(recurrenceComboBox)
        .asRequired("Schedule is required")
        .bind(FastData::getSchedule, FastData::setSchedule);

    fastDataBinder.forField(phasesComboBox)
        .bind(FastData::getPhases, (fastData, value) -> fastData.setPhases(value != null ? Phases.valueOf(String.valueOf(value)) : Phases.NONE));

    submitButton.addClickListener(event -> {
      FastData fastData = new FastData();
      fastData.setUser(user);
      if (fastDataBinder.writeBeanIfValid(fastData)) {
        if (fastDataServiceImpl.isFastDataExist(fastData)) {
          Notification.show("Fast details already exists !", 3000, Notification.Position.BOTTOM_CENTER);
          UI.getCurrent().navigate(ThanksView.class);
        } else {
          fastDataRepository.save(fastData);
          Notification.show("Fast details saved successfully", 3000, Notification.Position.BOTTOM_CENTER);
          UI.getCurrent().navigate(ThanksView.class);
        }
      } else {
        Notification.show("Please fill out the form correctly", 3000, Notification.Position.BOTTOM_CENTER);
      }
    });

    scheduledFastBotton.addClickListener(buttonClickEvent -> {
      Integer userId = user.getId();
      UI.getCurrent().navigate(ScheduledFastDetailsView.class, userId);
    });

    submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    submitButton.addClickShortcut(Key.ENTER);

    scheduledFastBotton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    FormLayout fastDetailsLayout = new FormLayout();
    fastDetailsLayout.setWidth("400px");
    H2 header = new H2("Enter the details");
    fastDetailsLayout.add(header);
    fastDetailsLayout.setColspan(header, 3);
    fastDetailsLayout.add(fastTypeComboBox, phasesComboBox, dayComboBox, recurrenceComboBox, submitButton, scheduledFastBotton);

    VerticalLayout centeredLayout = new VerticalLayout();
    centeredLayout.setSizeFull();
    centeredLayout.setAlignItems(Alignment.CENTER);
    centeredLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    centeredLayout.getStyle().set("padding-left", "800px");

    centeredLayout.add(fastDetailsLayout);

    add(centeredLayout);
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
  }

  private void onFastTypeChange(HasValue.ValueChangeEvent<FastType> event) {
    FastType selectedValue = event.getValue();
    if (Objects.nonNull(selectedValue)) {
      updateRecurrenceOptions(selectedValue);
    }
  }

  private void updateRecurrenceOptions(FastType fastType) {
    if (fastType.equals(FastType.WEEKLY)) {
      recurrenceComboBox.setItems(Schedule.ONCE, Schedule.EVERY_WEEK);
      dayComboBox.setEnabled(true);
      phasesComboBox.clear();
      phasesComboBox.setEnabled(false);
    } else {
      recurrenceComboBox.setItems(Stream.of(Schedule.values())
          .filter(recurrence -> recurrence != Schedule.EVERY_WEEK)
          .toList());
      recurrenceComboBox.setItemLabelGenerator(Schedule::getDisplayName);
      dayComboBox.clear();
      dayComboBox.setEnabled(false);
      phasesComboBox.setEnabled(true);
    }
  }

  @Override
  public void setParameter(BeforeEvent beforeEvent, String parameter) {
    if (Objects.nonNull(parameter)) {
      String userJson = URLDecoder.decode(parameter.substring("user=".length()), StandardCharsets.UTF_8);
      try {
        this.user = objectMapper.readValue(userJson, User.class);
      } catch (JsonProcessingException e) {
        Notification.show("Error deserializing user data", 3000, Notification.Position.BOTTOM_CENTER);
      }
    }
  }
}
