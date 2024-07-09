package com.example.application.repository;


import com.example.application.entities.FastData;
import com.example.application.enums.FastType;
import com.example.application.enums.Phases;
import com.example.application.enums.Schedule;
import com.example.application.enums.WeekDays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FastDataRepository extends JpaRepository<FastData, Integer> {

  List<FastData> findByUserId(int userId);

  boolean existsByUserIdAndDayAndFastTypeAndPhasesAndSchedule(int userId, WeekDays day, FastType fastType, Phases phases, Schedule schedule);

  List<FastData> findByStatusTrueAndFastType(FastType fastType);

  List<FastData> findByPhasesNotInAndFastType(List<Phases> excludedPhases, FastType fastType);

  List<FastData> findByStatusTrueAndUserId(int userId);


}
