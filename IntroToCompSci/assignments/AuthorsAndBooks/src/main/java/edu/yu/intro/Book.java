package edu.yu.intro;
import java.util.Objects;
/** Ctor: all parameters must be non-empty Strings.
*
* @param isbn Uniquely identifies a book
* @param title Book’s title
* @param author Book’s author
* @param yearPublished When book was published
* @param publisher Who published the book
*/
public class Book
{
  public String isbn;
  public String title;
  public String author;
  public String yearPublished;
  public String publisher;
  public Book(final String isbn, final String title ,final String author, final String yearPublished ,final String publisher)
  {
    // Write a test to check to make sure that none are empty strings- I think this is the way to do it
    if (isbn == null)
    {
      throw new IllegalArgumentException("The isbn is null");
    }
    if (isbn.isEmpty())
    {
      throw new IllegalArgumentException("The isbn is empty");
    }
    this.isbn = isbn;
    if (title == null)
    {
      throw new IllegalArgumentException("The title is null");
    }
    if (title.isEmpty())
    {
      throw new IllegalArgumentException("The title is empty");
    }
    this.title = title;
    if (author == null)
    {
      throw new IllegalArgumentException("The author is null");
    }
    if (author.isEmpty())
    {
      throw new IllegalArgumentException("The author is empty");
    }
    this.author = author;
    if ( yearPublished == null)
    {
      throw new IllegalArgumentException("The yearPublished is null");
    }
    if (yearPublished.isEmpty())
    {
      throw new IllegalArgumentException("The yearPublished is empty");
    }
    this.yearPublished = yearPublished;
    if (publisher == null)
    {
      throw new IllegalArgumentException("The publisher is null");
    }
    if (publisher.isEmpty())
    {
      throw new IllegalArgumentException("The publisher is empty");
    }
    this.publisher = publisher;
  }
  public String getISBN()
  {
    return isbn;
  }
  public String getAuthor()
  {
    return author;
  }
  public String getTitle()
  {
    return title;
  }
  public String getYearPublished()
  {
    return yearPublished;
  }
  public String getPublisher()
  {
    return publisher;
  }
  @Override// still not really sure whats going on
  public int hashCode()
  {
    return Objects.hash(isbn);
  }
  @Override //not really sure how this works and what exactly i'm doing
  public boolean equals(Object that)
  {
    if (this == that )
    {
      return true;
    }
    if (that == null )
    {
      return false;
    }
    if (this.getClass() != that.getClass())
    {
      return false;
    }
    Book otherBook = (Book)that;
    return (Objects.equals(this.isbn, otherBook.getISBN()));
  }
  @Override
  public String toString ()
  {
    return ("The isbn is " +this.getISBN());
  }
}
