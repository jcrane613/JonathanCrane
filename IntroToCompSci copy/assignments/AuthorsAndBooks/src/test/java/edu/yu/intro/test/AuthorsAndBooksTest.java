package edu.yu.intro.test;
import edu.yu.intro.AuthorsAndBooks;
import edu.yu.intro.Book;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;
public class AuthorsAndBooksTest
{
  @Test (expected = IllegalArgumentException.class)
  public void nonEmptyString1()
  {
    Book book = new Book("","jon","jon","jon","jon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nonEmptyString2()
  {
    Book book = new Book("jon","","jon","jon","jon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nonEmptyString3()
  {
    Book book = new Book("jon","jon","","jon","jon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nonEmptyString4()
  {
    Book book = new Book("jon","jon","jon","","jon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nonEmptyString5()
  {
    Book book = new Book("jon","jon","jon","jon","");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullValue1()
  {
    Book book = new Book(null,"jon","jon","jon","jon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullValue2()
  {
    Book book = new Book("jon",null,"jon","jon","jon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullValue3()
  {
    Book book = new Book("jon","jon",null,"jon","jon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullValue4()
  {
    Book book = new Book("jon","jon","jon",null,"jon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullValue5()
  {
    Book book = new Book("jon","jon","jon","jon",null);
  }
  @Test
  public void getISBNChecker()
  {
    Book book = new Book("0963270702","The Satanic Verses","Salman Rushdie","1992","Distributors");
    String thisIsbn = book.getISBN();
    assertEquals(thisIsbn,"0963270702");
  }
  @Test
  public void getAuthorChecker()
  {
    Book book = new Book("0963270702","The Satanic Verses","Salman Rushdie","1992","Distributors");
    String thisAuthor = book.getAuthor();
    assertEquals(thisAuthor,"Salman Rushdie");
  }
  @Test
  public void getTitleChecker()
  {
    Book book = new Book("0963270702","The Satanic Verses","Salman Rushdie","1992","Distributors");
    String thisTitle = book.getTitle();
    assertEquals(thisTitle,"The Satanic Verses");
  }
  @Test
  public void getYearPublishedChecker()
  {
    Book book = new Book("0963270702","The Satanic Verses","Salman Rushdie","1992","Distributors");
    String thisYearPublished = book.getYearPublished();
    assertEquals(thisYearPublished,"1992");
  }
  @Test
  public void getPublisherChecker()
  {
    Book book = new Book("0963270702","The Satanic Verses","Salman Rushdie","1992","Distributors");
    String thisPublisher = book.getPublisher();
    assertEquals(thisPublisher,"Distributors");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullBook()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book book = null;
    author.add(book);
  }
  @Test (expected = IllegalArgumentException.class)
  public void bookAlreadyAdded()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Random House");
    Book coolBook3 = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
  }
  @Test (expected = IllegalArgumentException.class)
  public void emptyAuthor()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    author.add(coolBook);
    author.booksByAuthor("");
  }
  @Test
  public void booksByAuthorChecker()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Random House");
    Book coolBook3 = new Book("0812500024","The Jackal's Head","Elizabeth Peters","1995","Tor Books");
    Book coolBook4 = new Book("0446364827","Silhouette in Scarlet","Elizabeth Peters","1994","Warner Books Inc");
    Book coolBook5 = new Book("0553579045","Your Baby's First Year","Steven P. Shelov","1998","Bantam");
    Book coolBook6 = new Book("0765308703","What Dreams May Come","Richard Matheson","2004","Tor Books");
    Book coolBook7 = new Book("0380733390","Borrower of the Night: The First Vicky Bliss Mystery","Elizabeth Peters","2000","Avon");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
    author.add(coolBook4);
    author.add(coolBook5);
    author.add(coolBook6);
    author.add(coolBook7);
    Set <Book> bookTest= author.booksByAuthor("Elizabeth Peters");
    Set <Book> bookTestVersus = new HashSet<>();
    bookTestVersus.add(coolBook3);
    bookTestVersus.add(coolBook4);
    bookTestVersus.add(coolBook7);
    assertEquals(bookTest, bookTestVersus);
  }
  @Test
  public void emptySetReturnAuthor()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Random House");
    Book coolBook3 = new Book("0812500024","The Jackal's Head","Elizabeth Peters","1995","Tor Books");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
    Set <Book> bookTest= author.booksByAuthor("Jonathan Crane");
    assertTrue(bookTest.isEmpty());
  }
  @Test (expected = IllegalArgumentException.class)
  public void emptyPublisher()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    author.add(coolBook);
    author.booksByPublisher("");
  }
  @Test
  public void emptySetReturnPublisher()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Random House");
    Book coolBook3 = new Book("0812500024","The Jackal's Head","Elizabeth Peters","1995","Tor Books");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
    Set <Book> bookTest= author.booksByPublisher("Jonathan Crane");
    assertTrue(bookTest.isEmpty());
  }
  @Test
  public void booksByPublisherChecker()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Avon");
    Book coolBook3 = new Book("0812500024","The Jackal's Head","Elizabeth Peters","1995","Tor Books");
    Book coolBook4 = new Book("0446364827","Silhouette in Scarlet","Elizabeth Peters","1994","Warner Books Inc");
    Book coolBook5 = new Book("0553579045","Your Baby's First Year","Steven P. Shelov","1998","Avon");
    Book coolBook6 = new Book("0765308703","What Dreams May Come","Richard Matheson","2004","Tor Books");
    Book coolBook7 = new Book("0380733390","Borrower of the Night: The First Vicky Bliss Mystery","Elizabeth Peters","2000","Avon");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
    author.add(coolBook4);
    author.add(coolBook5);
    author.add(coolBook6);
    author.add(coolBook7);
    Set <Book> bookTest= author.booksByPublisher("Avon");
    Set <Book> bookTestVersus = new HashSet<>();
    bookTestVersus.add(coolBook2);
    bookTestVersus.add(coolBook5);
    bookTestVersus.add(coolBook7);
    assertEquals(bookTest, bookTestVersus);
  }
  @Test (expected = IllegalArgumentException.class)
  public void emptyAuthorAllTitles()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    author.add(coolBook);
    author.allTitles("");
  }
  @Test
  public void emptySetReturnAllTitles()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Random House");
    Book coolBook3 = new Book("0812500024","The Jackal's Head","Elizabeth Peters","1995","Tor Books");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
    Set <String> bookTest= author.allTitles("Jonathan Crane");
    assertTrue(bookTest.isEmpty());
  }
  @Test
  public void allTitlesForAnAuthor()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Avon");
    Book coolBook3 = new Book("0812500024","The Jackal's Head","Elizabeth Peters","1995","Tor Books");
    Book coolBook4 = new Book("0446364827","Silhouette in Scarlet","Elizabeth Peters","1994","Warner Books Inc");
    Book coolBook5 = new Book("0553579045","Your Baby's First Year","Steven P. Shelov","1998","Avon");
    Book coolBook6 = new Book("0765308703","What Dreams May Come","Richard Matheson","2004","Tor Books");
    Book coolBook7 = new Book("0380733390","Borrower of the Night: The First Vicky Bliss Mystery","Elizabeth Peters","2000","Avon");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
    author.add(coolBook4);
    author.add(coolBook5);
    author.add(coolBook6);
    author.add(coolBook7);
    Set <String> bookTest= author.allTitles("Elizabeth Peters");
    Set <String> bookTestVersus = new HashSet<>();
    bookTestVersus.add("The Jackal's Head");
    bookTestVersus.add("Silhouette in Scarlet");
    bookTestVersus.add("Borrower of the Night: The First Vicky Bliss Mystery");
    assertEquals(bookTest, bookTestVersus);
  }
  @Test
  public void UniquePublisherChecker()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Tor Books");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Avon");
    Book coolBook3 = new Book("0812500024","The Jackal's Head","Elizabeth Peters","1995","Tor Books");
    Book coolBook4 = new Book("0446364827","Silhouette in Scarlet","Elizabeth Peters","1994","Warner Books Inc");
    Book coolBook5 = new Book("0553579045","Your Baby's First Year","Steven P. Shelov","1998","Avon");
    Book coolBook6 = new Book("0765308703","What Dreams May Come","Elizabeth Peters","2004","Tor Books");
    Book coolBook7 = new Book("0380733390","Borrower of the Night: The First Vicky Bliss Mystery","Elizabeth Peters","2000","Avon");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
    author.add(coolBook4);
    author.add(coolBook5);
    author.add(coolBook6);
    author.add(coolBook7);
    String richPublisher = author.publisherMostUniqueBooks();
    assertEquals(richPublisher, "Avon");
  }
  @Test
  public void emptyPublisherMostUnique()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Berkley Publishing Group");
    String richPublisher = author.publisherMostUniqueAuthors();
    assertEquals(richPublisher, null);
  }
  @Test
  public void PublisherTieBreaker()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    Book coolBook = new Book("0425176428","What If?: The World's Foremost Military Historians Imagine What Might Have Been","Robert Cowley","2000","Tor Books");
    Book coolBook2 = new Book("0679425608","Under the Black Flag: The Romance and the Reality of Life Among the Pirates","David Cordingly","1996","Avon");
    Book coolBook3 = new Book("0812500024","The Jackal's Head","Steven P. Shelov","1995","Tor Books");
    Book coolBook4 = new Book("0446364827","Silhouette in Scarlet","Elizabeth Peters","1994","Warner Books Inc");
    Book coolBook5 = new Book("0553579045","Your Baby's First Year","Steven P. Shelov","1998","Avon");
    Book coolBook6 = new Book("0765308703","What Dreams May Come","Elizabeth Peters","2004","Tor Books");
    Book coolBook7 = new Book("0380733390","Borrower of the Night: The First Vicky Bliss Mystery","Elizabeth Peters","2000","Avon");
    author.add(coolBook);
    author.add(coolBook2);
    author.add(coolBook3);
    author.add(coolBook4);
    author.add(coolBook5);
    author.add(coolBook6);
    author.add(coolBook7);
    String richPublisher = author.publisherMostUniqueBooks();
    assertEquals(richPublisher, "Avon");
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullAuthor()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    author.booksByAuthor(null);
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullPublisher()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    author.booksByPublisher(null);
  }
  @Test (expected = IllegalArgumentException.class)
  public void nullTitle()
  {
    AuthorsAndBooks author = new AuthorsAndBooks();
    author.allTitles(null);
  }
}
