package sjw;

import java.util.*;

public class User {
  private String userName;   
  private String userPassword;  //4자리 숫자
  private Map<String, Book> rentList = new HashMap<>();

  User(String userName, String userPassword) {
    this.userName = userName;
    this.userPassword = userPassword;
  }
  User(String userName, String userPassword, Map<String, Book> rentList) {
    this.userName = userName;
    this.userPassword = userPassword;
    this.rentList = rentList;
  }

  Map<String, Book> getRentList() {
    return rentList;
  }

  String getUserPassword() {
    return userPassword;
  }

  String getUserName() {
    return userName;
  }

  boolean checkList(String bookId) {
    if (!rentList.containsKey(bookId)) {
      return false;
    }
    return true;
  }

  void showList() {
    if (rentList.isEmpty()) {
      System.out.println("대출 도서가 없습니다.");
      System.out.println("--------------------");
      return;
    }
    System.out.println(userName + "님의 대출 목록입니다. ( 도서 번호 | 제목 )");
    Set<Map.Entry<String, Book>> entrySet = rentList.entrySet();
    Iterator<Map.Entry<String, Book>> iterator = entrySet.iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, Book> entry = iterator.next();
      String bookId = entry.getKey();
      Book book = entry.getValue();
      System.out.println(bookId + " | " + book.getBookName());
    }
    System.out.println("--------------------");
  }

  void addToList(String bookId, Book book) {
    rentList.put(bookId, book);
  }

  void removeFromList(String bookId) {
    rentList.remove(bookId);
  }
}
