package com.example.application.services.impl;


import com.example.application.entities.FastData;
import com.example.application.repository.FastDataRepository;
import com.example.application.services.FastDataService;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class FastDataServiceImpl implements FastDataService {
  private final FastDataRepository fastDataRepository;


  public FastDataServiceImpl(FastDataRepository fastDataRepository) {
    this.fastDataRepository = fastDataRepository;
  }
  @Override
  public boolean isFastDataExist(FastData fastData) {
    return fastDataRepository.existsByUserIdAndDayAndFastTypeAndPhasesAndSchedule(
        fastData.getUser().getId(),
        fastData.getDay(),
        fastData.getFastType(),
        fastData.getPhases(),
        fastData.getSchedule()
    );
  }

  @Override
  public void updateFastData(FastData fastData) {
    fastDataRepository.save(fastData);
  }

  @Override
  public List<FastData> findAllActiveFastsByUserId(int userId){
    return fastDataRepository.findByStatusTrueAndUserId(userId);
  }

  @Override
  public void deleteFastData(FastData fastData){
    if(Objects.nonNull(fastData)){
      fastData.setStatus(false);
      updateFastData(fastData);
    }
  }

}

