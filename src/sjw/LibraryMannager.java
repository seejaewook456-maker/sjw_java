package sjw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class LibraryMannager {

  //회원가입
  void addUser(Map<String, User> userList, Scanner sc) {
    System.out.println("--------------------");
    System.out.println("회원 가입을 시작합니다.");

    System.out.print("이름 입력: ");
    String userName = sc.nextLine();

    System.out.print("id 입력(2자리 숫자): ");
    String userId = sc.nextLine();
    if (!userId.matches("\\d{2}")) {
      System.out.println("잘못된 id 형식입니다.");
      return;
    }
    if (userList.containsKey(userId)) {
      System.out.println("이미 존재하는 id 입니다.");
      return;
    }

    System.out.print("비밀번호 입력(4자리 숫자): ");
    String userPassword = sc.nextLine();
    if (!userPassword.matches("\\d{4}")) {
      System.out.println("잘못된 비밀번호 형식입니다.");
      return;
    }
    System.out.print("비밀번호 재입력: ");
    String PasswordTest = sc.nextLine();
    if (!userPassword.equals(PasswordTest)) {
      System.out.println("비밀번호가 일치하지 않습니다.");
      return;
    }

    userList.put(userId, new User(userName, userPassword));     
    System.out.println("회원가입이 완료되었습니다.");
    System.out.println("--------------------");
  }

  //로그인
  User logIn(Map<String, User> userList, Scanner sc) {
    System.out.println("--------------------");
    System.out.println("로그인을 시작합니다.");

    System.out.print("id 입력: ");
    String userId = sc.nextLine();
    if (!userList.containsKey(userId)) {
      System.out.println("등록되지 않은 id 입니다.");
      return null;
    }
    User currentUser = userList.get(userId);

    System.out.print("비밀번호 입력: ");
    String userPassword = sc.nextLine();
    if (!userPassword.equals(currentUser.getUserPassword())) {
      System.out.println("비밀번호를 잘못 입력하셨습니다.");
      return null;
    }

    System.out.println(currentUser.getUserName() + "님 환영합니다.");
    System.out.println("--------------------");
    return currentUser;
  }

  //로그아웃
  User logOut() {
    System.out.println("로그아웃 완료");
    System.out.println("--------------------");
    return null;
  }

  //대출 목록 조회
  void showLentList(User currentUser) {
    if (currentUser == null) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }
    currentUser.showList();
    }

  //도서 대출
  void rentBook(User currentUser, Map<String, Book> bookList, Scanner sc) {
    if (currentUser == null) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    } 
    System.out.print("대출할 도서의 도서 번호 입력: ");
    String bookId = sc.nextLine();
    if(!bookList.containsKey(bookId)) {
      System.out.println("존재하지 않는 도서 번호입니다.");
      return;
    }
    Book book = bookList.get(bookId);
    if (book.isRented()) {
      System.out.println("이미 대출중인 도서입니다.");
      return;
    }
    book.setRented(true);
    currentUser.addToList(bookId, book);
    System.out.println(book.getBookName() + " 대출 완료");
    System.out.println("--------------------");
  }

  //도서 반납 
  void returnBook(User currentUser, Map<String, Book> bookList, Scanner sc) {
    if (currentUser == null) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }
    System.out.print("반납할 도서의 도서 번호 입력: ");
    String bookId = sc.nextLine();
    if(!bookList.containsKey(bookId)) {
      System.out.println("존재하지 않는 도서 번호입니다.");
      return;
    }
    if (!currentUser.checkList(bookId)) {
      System.out.println("대출 중인 도서가 아닙니다.");
      return;
    }
    Book book = bookList.get(bookId);
    book.setRented(false);
    currentUser.removeFromList(bookId);
    System.out.println(book.getBookName() + " 반납 완료");
    System.out.println("--------------------");
  }
  
  //전체 도서 목록 조회
  void showBookList(User currentUser, Map<String, Book> bookList) {
    if (currentUser == null) {
      System.out.println("로그인 후 이용해주세요.");
      return;
    }
    System.out.println("도서 번호 | 제목 | 대출 여부");
    Set<Map.Entry<String, Book>> entrySet = bookList.entrySet();
    Iterator<Map.Entry<String, Book>> iterator = entrySet.iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, Book> entry = iterator.next();
      String bookId = entry.getKey();
      Book book = entry.getValue();
      System.out.println(bookId + "  |  " + book.getBookName() + "  |  " + book.isRented());
    }
    System.out.println("--------------------");
  }

  //전체 도서 대출 횟수 기준으로 정렬 후 출력
  void showByRentNum(Map<String, Book> bookList) {
    Set<Map.Entry<String, Book>> entrySet = bookList.entrySet();
    Iterator<Map.Entry<String, Book>> iterator = entrySet.iterator();
    List<Book> list = new ArrayList<>();
    while (iterator.hasNext()) {
      Map.Entry<String, Book> entry = iterator.next();
      Book book = entry.getValue();
      list.add(book);
    }
    Collections.sort(list);
    System.out.println("대출 횟수 많은 순으로 정렬 (대출 횟수 | 도서 번호 | 제목 | 대출 여부)");
    for(Book book : list) {
      System.out.println(book.getRentedNum() + "회 | " + book.getBookId() + " | " + book.getBookName() + " | " + book.isRented());
    }
    System.out.println("--------------------");
  }

  //파일 저장
  void saveToFile(Map<String, User> userList, Map<String, Book> bookList) {
  
    //bookList 파일
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:/JavaStudy/sjw_java/bookList.txt"))) {
      Set<Map.Entry<String, Book>> entrySet = bookList.entrySet();
      Iterator<Map.Entry<String, Book>> iterator = entrySet.iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, Book> entry = iterator.next();
        Book book = entry.getValue();
        bw.write(book.getBookId() + "|" + book.getBookName() + "|" + book.isRented() + "|" + book.getRentedNum());
        bw.newLine();
      }
    } catch (Exception e) {
      System.out.println("파일 저장 실패");
      return;
    }

    //userList 파일
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:/JavaStudy/sjw_java/userList.txt"))) {
      Set<Map.Entry<String, User>> entrySet = userList.entrySet();
      Iterator<Map.Entry<String, User>> iterator = entrySet.iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, User> entry = iterator.next();
        String userId = entry.getKey();
        User user = entry.getValue();

        //유저의 lentList에서 bookId만 뽑아서 나열
        String line = "|";
        Map<String, Book> lentList = user.getRentList();
        Set<Map.Entry<String, Book>> entrySet2 = lentList.entrySet();
        Iterator<Map.Entry<String, Book>> iterator2 = entrySet2.iterator();
        while (iterator2.hasNext()) {
          Map.Entry<String, Book> entry2 = iterator2.next();
          String bookId = entry2.getKey();
          line += (bookId + "|");
        }

        bw.write(userId + "|" + user.getUserName() + "|" + user.getUserPassword() + line);
        bw.newLine();
      }
    } catch (Exception e) {
      System.out.println("파일 저장 실패");
      return;
    }

    System.out.println("파일 저장 완료");
  }

  //저장 파일 불러오기
  void loadFromFile(Map<String, User> userList, Map<String, Book> bookList) {

    //bookList 파일 
    try (BufferedReader br = new BufferedReader(new FileReader("C:/JavaStudy/sjw_java/bookList.txt"))) {
      while (true) {
        String line = br.readLine();
        if (line == null) {
          break;
        }
        String[] arr = line.split("\\|");
        String bookId = arr[0];
        Book book = new Book(arr[0], arr[1], Boolean.parseBoolean(arr[2]), Integer.parseInt(arr[3]));
        bookList.put(bookId, book);
     }
    } catch (Exception e) {
      System.out.println("파일 불러오기 실패");
      return;
    }

    //userList 파일
    try (BufferedReader br2 = new BufferedReader(new FileReader("C:/JavaStudy/sjw_java/userList.txt"))) {
      while (true) {
        String line2 = br2.readLine();
        if (line2 == null) {
          break;
        }
        String[] arr2 = line2.split("\\|");
        String userId = arr2[0];
        String userName = arr2[1];
        String userPassword = arr2[2];
        Map<String, Book> lentList = new HashMap<String,Book>();
        for (int i = 3; i < arr2.length; i++) {
          String bookId = arr2[i];
          Book book = bookList.get(bookId);
          lentList.put(bookId, book);
        }
        userList.put(userId, new User(userName, userPassword, lentList));
      }
    } catch (Exception e) {
      System.out.println("파일 불러오기 실패");
      return;
    }

    System.out.println("저장된 파일을 불러왔습니다.");
  }
}
