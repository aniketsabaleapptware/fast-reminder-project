package com.example.application.services;

import com.example.application.entities.FastData;
import java.util.List;

public interface FastDataService {

  boolean isFastDataExist(FastData fastData);

  void updateFastData(FastData fastData);

  List<FastData> findAllActiveFastsByUserId(int userId);

  void deleteFastData(FastData fastData);}
