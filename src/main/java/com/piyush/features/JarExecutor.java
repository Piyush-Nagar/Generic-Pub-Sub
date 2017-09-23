package com.piyush.features;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Add your implementation here.
 *
 * @author Piyush on 25/10/16.
 */
@Service
public class JarExecutor {

  private final AbstractProducer commonProducer;

  @Autowired
  public JarExecutor(AbstractProducer commonProducer) {
    this.commonProducer = commonProducer;
  }

  public void execute() {
    commonProducer.produceAndConsume();
  }
}
