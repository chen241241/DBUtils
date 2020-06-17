package com.ck.customer.utils;

import com.ck.customer.dao.CustomerEntity;
import com.ck.customer.dao.Models;
import com.google.common.base.Strings;
import com.mysql.cj.jdbc.MysqlDataSource;
import io.requery.meta.EntityModel;
import io.requery.query.Result;
import io.requery.sql.Configuration;
import io.requery.sql.ConfigurationBuilder;
import io.requery.sql.EntityDataStore;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Software: IntelliJ IDEA
 * ClassName: DBUtils
 * Description:
 * author: Kevin
 * E-mail:b9016609@newcastle.ac.uk
 * date: 2020/6/16 21:27
 */
public class DBUtils {
  private EntityDataStore<Object> dataStore;

  public DBUtils() throws SQLException {
    MysqlDataSource mysqlDataSource = new MysqlDataSource();
    mysqlDataSource.setServerName("localhost");
    mysqlDataSource.setPort(3306);
    mysqlDataSource.setDatabaseName("customer");
    mysqlDataSource.setUser("root");
    mysqlDataSource.setPassword("chen241241");
    mysqlDataSource.setServerTimezone("GMT+8");

    EntityModel model = Models.DEFAULT;
    Configuration configuration =
        new ConfigurationBuilder(mysqlDataSource, model).useDefaultLogging().build();

    dataStore = new EntityDataStore<>(configuration);
  }

  public Object[][] getAllCustomer() {
    // "姓名", "性别", "年龄", "爱好", "毕业学校", "职业"

    Result<CustomerEntity> result =
        dataStore
            .select(CustomerEntity.class)
                .orderBy(CustomerEntity.ID.asc()).get();
    List<CustomerEntity> collect = result.stream().collect(Collectors.toList());
    return result.stream()
        .map(
            item ->
                new Object[] {
                  item.getName(),
                  item.getSex(),
                  item.getAge(),
                  item.getHobby(),
                  item.getGraduate(),
                  item.getOccupation()
                })
        .toArray(Object[][]::new);
  }

  public Object[][] getCustomer(String name, String occupation) {
    name = Strings.nullToEmpty(name);
    occupation = Strings.nullToEmpty(occupation);

    Result<CustomerEntity> result =
        dataStore
            .select(CustomerEntity.class)
            .where(
                CustomerEntity.NAME
                    .like('%' + name + '%')
                    .and(CustomerEntity.OCCUPATION.like('%' + occupation + '%')))
            .orderBy(CustomerEntity.ID.asc())
            .get();
    List<CustomerEntity> collect = result.stream().collect(Collectors.toList());
    return result.stream()
        .map(
            item ->
                new Object[] {
                  item.getName(),
                  item.getSex(),
                  item.getAge(),
                  item.getHobby(),
                  item.getGraduate(),
                  item.getOccupation()
                })
        .toArray(Object[][]::new);
  }
  public void addCustomer(String name, String sex, Integer age, String hobby,
                          String graduate, String occupation) {
    name = Strings.nullToEmpty(name);
    sex = Strings.nullToEmpty(sex);
    hobby = Strings.nullToEmpty(hobby);
    graduate = Strings.nullToEmpty(graduate);
    occupation = Strings.nullToEmpty(occupation);
//    name,sex,age,hobby,graduate,occupation

    dataStore.insert(CustomerEntity.class)
            .value(CustomerEntity.NAME,name)
            .value(CustomerEntity.SEX,sex)
            .value(CustomerEntity.AGE,age)
            .value(CustomerEntity.HOBBY,hobby)
            .value(CustomerEntity.GRADUATE,graduate)
            .value(CustomerEntity.OCCUPATION,occupation)
            .get().first().get(CustomerEntity.ID);

  }
  public void editCustomer(String name, String sex, Integer age, String hobby,
                           String graduate, String occupation) {
    name = Strings.nullToEmpty(name);
    sex = Strings.nullToEmpty(sex);
    hobby = Strings.nullToEmpty(hobby);
    graduate = Strings.nullToEmpty(graduate);
    occupation = Strings.nullToEmpty(occupation);
//    name,sex,age,hobby,graduate,occupation

    dataStore.update(CustomerEntity.class)
            .set(CustomerEntity.SEX,sex)
            .set(CustomerEntity.AGE,age)
            .set(CustomerEntity.HOBBY,hobby)
            .set(CustomerEntity.GRADUATE,graduate)
            .set(CustomerEntity.OCCUPATION,occupation)
            .where(CustomerEntity.NAME.eq(name))
            .get().value();

  }
  public void delCustomer(String key){
    dataStore.delete(CustomerEntity.class).where(CustomerEntity.NAME.eq(key)).get().value();
  }
}
