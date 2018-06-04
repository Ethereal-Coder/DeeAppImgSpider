package com.example;

/**
 * Created by 孙应恒 on 2018/6/1.
 * Description:
 */
public class StringSub {
  public static void main(String[] args) {
    String xx =
        "https://img.alicdn.com/bao/uploaded/TB15ahXbXuWBuNjSspnXXX1NVXa_!!0-item_pic.jpg_320x320Q50s50.jpg_.webp";
    //checkImgSize(xx);
  }

  /**
   * 合适返回true
   */
  private static boolean checkImgSize(String xx) throws Exception {
    int index_j = xx.indexOf(".jpg_");
    int index_p = xx.indexOf(".png_");
    if (index_p == -1) {
      if (index_j == -1) {
        return true;
      } else {
        String xs = xx.substring(index_j + 5);
        int index_x = xs.indexOf("x");
        Integer integer = Integer.getInteger(xs.substring(0, index_x));
        if (integer > 300) {
          return true;
        }
      }
    } else {
      if (index_j == -1) {
        String xs = xx.substring(index_p + 5);
        int index_x = xs.indexOf("x");
        Integer integer = Integer.getInteger(xs.substring(0, index_x));
        if (integer > 300) {
          return true;
        }
      } else {
        int temp_index = index_j > index_p ? index_p : index_j;
        String xs = xx.substring(temp_index + 5);
        int index_x = xs.indexOf("x");
        Integer integer = Integer.getInteger(xs.substring(0, index_x));
        if (integer > 300) {
          return true;
        }
      }
    }
    return false;
  }
}
