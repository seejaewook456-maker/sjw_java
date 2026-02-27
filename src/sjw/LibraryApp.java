package sjw;

import java.util.*;

public class LibraryApp {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    LibraryMannager mannager = new LibraryMannager();

    Map<String, Book> bookList = new HashMap<>();
    bookList.put("1", new Book("1","aaaaa"));
    bookList.put("2", new Book("2", "bbbbb"));
    bookList.put("3", new Book("3", "ccccc"));
    bookList.put("4", new Book("4","ddddd"));
    bookList.put("5", new Book("5", "eeeee"));      

    Map<String, User> userList = new HashMap<>();
    User currentUser = null;  //현재 로그인된 유저 
    mannager.loadFromFile(userList, bookList);
    
    boolean run = true;
    while(run) {
      System.out.println("""
          1. 회원가입
          2. 로그인
          3. 로그아웃
          4. 대출 목록 조회
          5. 도서 대출
          6. 도서 반납
          7. 전체 도서 목록 조회 (도서 번호 기준 정렬)
          8. 전체 도서 목록 조회 (대출 횟수 기준 정렬)
          9. 파일 저장                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
          0. 프로그램 종료
          --------------------
          """);
      System.out.print("원하는 항목의 번호를 입력: ");
      int menu = sc.nextInt();
      sc.nextLine();

      switch(menu) {
        //회원 가입
        case 1:
          mannager.addUser(userList, sc);
          break;
        //로그인
        case 2:
          currentUser = mannager.logIn(userList, sc);
          break;
        //로그아웃
        case 3:
          currentUser = mannager.logOut();
          break;
        //대출 목록 조회
        case 4:
          mannager.showLentList(currentUser);
          break;
        //도서 대출
        case 5:
          mannager.rentBook(currentUser, bookList, sc);
          break;
        //도서 반납
        case 6:
          mannager.returnBook(currentUser, bookList, sc);
          break;
        //전체 도서 목록 조회
        case 7:
          mannager.showBookList(currentUser, bookList); 
          break;
        //전체 도서 목록 조회 (대출 횟수 기준 정렬)
        case 8: 
          mannager.showByRentNum(bookList);
          break;
        //파일 저장
        case 9:
          mannager.saveToFile(userList, bookList);
          break;
        //프로그램 종료
        case 0:
          System.out.println("도서 관리 프로그램을 종료합니다.");
          run = false;
          break;
        default:
          System.out.println("잘못된 번호 입력입니다.");
          break;
      }
    }
  }
}
