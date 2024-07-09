package com.example.application.views.main;

import com.example.application.entities.FastData;
import com.example.application.enums.FastType;
import com.example.application.enums.Phases;
import com.example.application.enums.Schedule;
import com.example.application.enums.WeekDays;
import com.example.application.services.impl.FastDataServiceImpl;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FastDataForm extends FormLayout {

  private final transient FastDataServiceImpl fastDataServiceImpl;
  private transient FastData fastData;
  private final Binder<FastData> fastDataBinder = new Binder<>(FastData.class);

  private Runnable onCancel;

  private final ComboBox<FastType> fastTypeComboBox = new ComboBox<>("Select Fast Type");
  private final ComboBox<Schedule> scheduleComboBox = new ComboBox<>("Schedule");
  private final ComboBox<Phases> phasesComboBox = new ComboBox<>("Select Phase");
  private final ComboBox<WeekDays> dayComboBox = new ComboBox<>("Select Days");

  private final Button save = new Button("Save");
  private final Button delete = new Button("Delete");
  private final Button close = new Button("Cancel");


  @Autowired
  public FastDataForm(FastDataServiceImpl fastDataServiceImpl) {
    this.fastDataServiceImpl = fastDataServiceImpl;
    addClassName("fastdata-form");

    setupForm();
    fastTypeComboBox.setItems(FastType.values());
    fastTypeComboBox.setItemLabelGenerator(FastType::getDisplayName);

    dayComboBox.setItems(Stream.of(WeekDays.values())
        .filter(day -> day != WeekDays.NONE)
        .toList());
    dayComboBox.setItemLabelGenerator(WeekDays::getDisplayName);

    scheduleComboBox.setItems(Schedule.values());
    scheduleComboBox.setItemLabelGenerator(Schedule::getDisplayName);

    phasesComboBox.setItems(Stream.of(Phases.values())
        .filter(phase -> phase != Phases.NONE)
        .toList());
    phasesComboBox.setItemLabelGenerator(Phases::getDisplayName);

    fastTypeComboBox.addValueChangeListener(this::onFastTypeChange);

    add(fastTypeComboBox, phasesComboBox, dayComboBox, scheduleComboBox, createButtonsLayout());
  }

  public void setupForm() {
    fastDataBinder.forField(fastTypeComboBox)
        .asRequired("")
        .bind(FastData::getFastType, FastData::setFastType);

    fastDataBinder.forField(scheduleComboBox)
        .asRequired("")
        .bind(FastData::getSchedule, FastData::setSchedule);

    fastDataBinder.forField(phasesComboBox)
        .bind(FastData::getPhases, (data, value) -> fastData.setPhases(value != null ? Phases.valueOf(String.valueOf(value)) : Phases.NONE));

    fastDataBinder.forField(dayComboBox)
        .bind(FastData::getDay, (data, value) -> data.setDay(value != null ? WeekDays.valueOf(String.valueOf(value)) : WeekDays.NONE));

    save.addClickListener(event -> saveFastData());

    delete.addClickListener(event -> deleteFastData());

    close.addClickListener(event -> cancleButton());

  }


  public void setFastData(FastData fastData) {
    clearForm();
    this.fastData = fastData;
    if (Objects.nonNull(fastData)) {
      fastTypeComboBox.setValue(FastType.fromDisplayName(fastData.getFastType().getDisplayName()));
      dayComboBox.setValue(WeekDays.fromDisplayName(fastData.getDay().getDisplayName()));
      scheduleComboBox.setValue(Schedule.fromDisplayName(fastData.getSchedule().getDisplayName()));
      phasesComboBox.setValue(Phases.fromDisplayName(fastData.getPhases().getDisplayName()));
      return;
    }
    clearForm();
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    return new HorizontalLayout(save, delete, close);
  }

  private void saveFastData() {
    if (fastDataBinder.writeBeanIfValid(fastData)) {
      fastDataServiceImpl.updateFastData(fastData);
      Notification.show("Fast details saved successfully", 3000, Notification.Position.BOTTOM_CENTER);
      UI.getCurrent().navigate(ThanksView.class);
    }
  }

  private void deleteFastData() {
    if (fastDataBinder.writeBeanIfValid(fastData)) {
      fastDataServiceImpl.deleteFastData(fastData);
      clearForm();
      Notification.show("Fast details deleted successfully", 3000, Notification.Position.BOTTOM_CENTER);
    }
  }

  private void cancleButton() {
    clearForm();
    setVisible(false);
    if (onCancel != null) {
      onCancel.run();
    }
  }

  private void onFastTypeChange(HasValue.ValueChangeEvent<FastType> event) {
    FastType selectedValue = event.getValue();
    if (Objects.nonNull(selectedValue)) {
      updateRecurrenceOptions(selectedValue);
    }
  }

  private void updateRecurrenceOptions(FastType fastType) {
    if (fastType.equals(FastType.WEEKLY)) {
      scheduleComboBox.setItems(Schedule.ONCE, Schedule.EVERY_WEEK);
      dayComboBox.setValue(WeekDays.SUNDAY);
      dayComboBox.setEnabled(true);
      phasesComboBox.clear();
      phasesComboBox.setEnabled(false);
    } else {
      scheduleComboBox.setItems(Stream.of(Schedule.values())
          .filter(recurrence -> recurrence != Schedule.EVERY_WEEK)
          .toList());
      scheduleComboBox.setItemLabelGenerator(Schedule::getDisplayName);
      phasesComboBox.setValue(Phases.BOTH);
      dayComboBox.clear();
      dayComboBox.setEnabled(false);
      phasesComboBox.setEnabled(true);
    }
  }

  private void clearForm() {
    fastTypeComboBox.clear();
    dayComboBox.clear();
    scheduleComboBox.clear();
    phasesComboBox.clear();
  }

}

