package com.frontbackend.thymeleaf.bootstrap.rules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.Chargeback;
import com.frontbackend.thymeleaf.bootstrap.model.Contract;

public class ValidateRules {

  public static Map<String, Contract> bidAwardsDb = new HashMap<>();
  public static Map<String, ItemLevelErrors> itemsDb = new HashMap<>();
  public static Map<String, FileLevelErrors> summaryDb = new HashMap<>();
  
  public static Map<String, Chargeback> chargeBackDb = new HashMap<>();

  public void processBidAwards(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<Contract> list = new ArrayList<>();
    list.add(mapper.readValue(file, Contract.class));

    Map<String, Contract> map =
        list.stream().collect(Collectors.toMap(x -> x.getContractId(), x -> x));
    bidAwardsDb.putAll(map);
  }

  public List<Chargeback> parseEDI844(File file) throws IOException {
    List<Chargeback> objs = new ArrayList<>();
    if (!file.isFile()) {
      return new ArrayList<>();
    }
    BufferedReader br = new BufferedReader(new FileReader(file));

    String s;
    while ((s = br.readLine()) != null) {
      String[] arr = s.split(",");
      Chargeback cb = new Chargeback();
      if (arr[0] == "HD") {
        cb.setCustomerID(arr[9]);
      } else {
        cb.setContractId(arr[5]);
        cb.setStartdate(s);
        cb.setProductPrice(arr[11]);
        cb.setProductId(arr[3]);
      }
      objs.add(cb);
    }

    return objs;
  }

  public List<ItemLevelErrors> validateChargeBacks(List<Chargeback> objs)
      throws IllegalAccessException, IOException {

    List<ItemLevelErrors> errors = new ArrayList<>();
    for (Chargeback cb : objs) {
    	Contract bidAward = bidAwardsDb.get(cb.getContractId());
      if (bidAward == null) {
        continue;
      }
      Class<?> clazz = cb.getClass();
      Field[] fields = clazz.getDeclaredFields();
      ItemLevelErrors error = new ItemLevelErrors();
      error.setContractId(bidAward.getContractId());
      error.setProductId(bidAward.getProductId());
      error.setCustomerID(bidAward.getCustomerID());
      error.setProductPrice(bidAward.getProductPrice());
      error.setTransactionDate(cb.getStartdate());
      error.setErrorReason("");
      boolean isAdd = false;
      for (Field field : fields) {
        if (!field.isAccessible()) {
          field.setAccessible(true);
        }
        Object value1 = field.get(cb);
        Object value2 = field.get(bidAward);
        if (value2 != null && value1 != value2) {
          error.setErrorReason(error.getErrorReason() + "wrong " + field.getName());
          cb.setRejectReasons(error.getErrorReason() + " wrong " + field.getName());
          isAdd = true;
        }
      }
      if (isAdd) {
    	  chargeBackDb.put(cb.getContractId(), cb);
      }
    }
    
    return errors;
  }

  public void validateEDI844Files(List<File> files) throws IOException, IllegalAccessException {

    List<FileLevelErrors> infoList = new ArrayList<>();
    for (File file : files) {
      List<Chargeback> cbs = parseEDI844(file);
      List<ItemLevelErrors> errors = validateChargeBacks(cbs);
      FileLevelErrors info = new FileLevelErrors();
      info.setFileName(file.getName());
      info.setTotalNoOfRecords(cbs.size());
      info.setNoOfErrorRecords(errors.size());
      infoList.add(info);
      summaryDb.put(file.getName(), info);
    }
    
  }

  public static void appendStrToFile(String fileName, String str) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
    out.write(str);
    out.close();
  }
}