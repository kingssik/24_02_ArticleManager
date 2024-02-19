package com.KoreaIT.java.AM.controller;

import com.KoreaIT.java.AM.dto.Article;
import com.KoreaIT.java.AM.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArticleController extends Controller {
  private Scanner sc;
  private List<Article> articles;
  private String cmd;
  private String actionMethodName;

  public ArticleController(Scanner sc) {
    this.sc = sc;
    this.articles = new ArrayList<Article>();
  }

  @Override
  public void doAction(String cmd, String actionMethodName) {
    this.cmd = cmd;
    this.actionMethodName = actionMethodName;

    switch (actionMethodName) {
      case "list":
        showList();
        break;
      case "detail":
        showDetail();
        break;
      case "write":
        doWrite();
        break;
      case "modify":
        doModify();
        break;
      case "delete":
        doDelete();
        break;
      default:
        System.out.println("지원하지 않는 기능입니다.");
        break;
    }
  }

  private void showList() {
    if (articles.size() == 0) {
      System.out.println("게시글이 없습니다.");
      return;
    } else {
      String searchKeyword = cmd.substring("article list".length()).trim();
      System.out.printf("검색어 : %s\n", searchKeyword);
      List<Article> forPrintArticles = articles;

      if (searchKeyword.length() > 0) {
        forPrintArticles = new ArrayList<>();

        for (Article article : articles) {
          if (article.title.contains(searchKeyword)) {
            forPrintArticles.add(article);
          }
        }

        if (forPrintArticles.size() == 0) {
          System.out.println("검색 결과가 없습니다.");
          return;
        }
      }

      System.out.println(" 번호 |   제목   | 조회수 ");
      for (int i = forPrintArticles.size() - 1; i >= 0; i--) {
        Article article = forPrintArticles.get(i);
        System.out.printf("  %2d  |  %6s  |  %2d  \n", article.id, article.title, article.viewCnt);
      }
    }
  }

  private void showDetail() {
    String[] cmdBits = cmd.split(" ");
    int id = Integer.parseInt(cmdBits[2]);

    Article foundArticle = getArticleById(id);
    if (foundArticle == null) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    } else {
      foundArticle.increaseViewCnt();
      System.out.printf("번호 : %d\n", foundArticle.id);
      System.out.printf("날짜 : %s\n", foundArticle.regDate);
      System.out.printf("제목 : %s\n", foundArticle.title);
      System.out.printf("내용 : %s\n", foundArticle.body);
      System.out.printf("조회수 : %d\n", foundArticle.viewCnt);
    }
  }

  private void doWrite() {
    int id = articles.size() + 1;
    String regDate = Util.getNowDateStr();
    System.out.print("제목 : ");
    String title = sc.nextLine();
    System.out.print("내용 : ");
    String body = sc.nextLine();

    Article article = new Article(id, regDate, title, body);
    articles.add(article);

    System.out.printf("%d번 글이 생성되었습니다.\n", id);
  }

  private void doDelete() {
    String[] cmdBits = cmd.split(" ");
    int id = Integer.parseInt(cmdBits[2]);

    int foundIndex = getArticleIndexById(id);
    if (foundIndex == -1) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    } else {
      articles.remove(foundIndex);
      System.out.printf("%d번 게시물이 삭제 되었습니다.\n", id);
    }
  }

  private void doModify() {
    String[] cmdBits = cmd.split(" ");
    int id = Integer.parseInt(cmdBits[2]);

    Article foundArticle = getArticleById(id);

    if (foundArticle == null) {
      System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
      return;
    } else {
      System.out.print("제목 : ");
      String title = sc.nextLine();
      System.out.print("내용 : ");
      String body = sc.nextLine();

      foundArticle.title = title;
      foundArticle.body = body;
      System.out.printf("%d번 게시물이 수정 되었습니다.\n", id);
    }
  }

  private int getArticleIndexById(int id) {
    int i = 0;
    for (Article article : articles) {
      if (article.id == id) {
        return i;
      }
      i++;
    }
    return -1;
  }

  private Article getArticleById(int id) {
    int idx = getArticleIndexById(id);
    if (idx == -1) {
      return null;
    }
    return articles.get(idx);
  }

  public void makeTestData() {
    System.out.println("게시글 테스트 데이터를 생성합니다.");
    articles.add(new Article(1, Util.getNowDateStr(), "title1", "body1", 11));
    articles.add(new Article(2, Util.getNowDateStr(), "title2", "body2", 22));
    articles.add(new Article(3, Util.getNowDateStr(), "title3", "body3", 33));
  }
}
