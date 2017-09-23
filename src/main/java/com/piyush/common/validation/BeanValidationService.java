package com.piyush.common.validation;

import java.util.Set;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.stereotype.Service;

/**
 * @author piyush on 8/9/17.
 */
@Service
public class BeanValidationService {

  private static final Logger LOGGER = Logger.getLogger(BeanValidationService.class.getName());

  private final Validator validator;

  public BeanValidationService(Validator validator) {
    this.validator = validator;
  }

  /**
   * check if there is any violation on given java bean.
   *
   * @param javaBean bean of T type
   * @param <T>      T type
   * @return true if java bean don't have any violation.
   */
  public <T> boolean isModelHasViolation(T javaBean, String id) {
    Set<ConstraintViolation<T>> violations = validator.validate(javaBean);
    if (violations.isEmpty()) {
      return false;
    } else {
      violations.forEach(violation -> {
        LOGGER.warning("Skipped due to: {}, for id {}" + violation.getMessage() + id);
      });
      return true;
    }
  }
}
