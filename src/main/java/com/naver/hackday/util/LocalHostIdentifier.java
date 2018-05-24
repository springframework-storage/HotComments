package com.naver.hackday.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class LocalHostIdentifier {

  private InetAddress inetAddress;

  private static final String HOST1 = "10.41.2.109";
  private static final String HOST2 = "10.41.0.155";
  private static final String HOST3 = "10.41.5.132";
  private static final String HOST4 = "10.41.1.101";

  /**
   * ip 주소를 가져오는 메소드
   * @return
   */
  public String getHost() {
    try {
      inetAddress = InetAddress.getLocalHost();
      String ip = inetAddress.getHostAddress();
      return ip;
    } catch (UnknownHostException e) {
      log.error(e + ": Fail to get localhost");
    }
    return null;
  }

  /**
   * ip 주소를 String ip 변수에 받아
   * 매칭시켜주는 역할의 메소드
   * @return
   */
  public int getHostMapping() {
    String ip = getHost();

    if (ip.equals(HOST1)) return 1;
    else if (ip.equals(HOST2)) return 2;
    else if (ip.equals(HOST3)) return 3;
    else if (ip.equals(HOST4)) return 4;
    else return -1;
  }

}
