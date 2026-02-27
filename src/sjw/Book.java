package sjw;

import java.util.*;

public class Book implements Comparable<Book> {
  private String bookId;
  private String bookName;
  private Boolean rented = false;
  private int rentedNum = 0;

  Book(String bookId, String bookName) {
    this.bookId = bookId;
    this.bookName = bookName;
  }
  Book(String bookId, String bookName, boolean rented, int rentedNum) {
    this.bookId = bookId;
    this.bookName = bookName;
    this.rented = rented;
    this.rentedNum = rentedNum;
  }

  String getBookId() {
    return bookId;
  }

  int getRentedNum() {
    return rentedNum;
  }

  boolean isRented() {
    return rented;
  }

  void setRented(boolean rented) {
    this.rented = rented;
    rentedNum++;
  }

  String getBookName() {
    return bookName;
  }

  @Override
  public int compareTo(Book other) {
    return other.rentedNum - this.rentedNum;
  }
}
