package edu.yu.intro;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.util.*;
import java.util.Scanner;

public class AuthorsAndBooks
{
  public Set<Book> bookSet = new HashSet<>();
  public Map <String, Set<Book>> mapOfAuthors = new HashMap<>();
  public Map <String, Set<Book>> mapOfPublisher = new HashMap<>();
  public Map <String, Set<String>> UniquePublisherMap = new HashMap<>();
  public AuthorsAndBooks()
  {}
  public void add(final Book book)
  {
    if (book == null)
    {
     throw new IllegalArgumentException("The book cannot be added since it was null. Deleting input and moving on");
    }
    if (bookSet.contains(book))
    {
     throw new IllegalArgumentException("The input for the book "+ book.getISBN()+" was was already inputted. Deleting input and moving on");
    }
    //code for the bookset to calculate if a book is doubled for the above exception
    bookSet.add(book);
    //code to determine how many Books are associated with a given author
    if (mapOfAuthors.containsKey(book.getAuthor()))
    {
     mapOfAuthors.get(book.getAuthor()).add(book);
    }
    else
    {
     Set<Book> setBookAuthor = new HashSet<>();
     mapOfAuthors.put(book.getAuthor(),setBookAuthor);
     setBookAuthor.add(book);
    }
    //code to determine how many Books are associated with a given publisher
    if (mapOfPublisher.containsKey(book.getPublisher()))
    {
     mapOfPublisher.get(book.getPublisher()).add(book);
    }
    else
    {
       Set<Book> setBookPublisher = new HashSet<>();
       mapOfPublisher.put(book.getPublisher(),setBookPublisher);
       setBookPublisher.add(book);
    }
    //code to determine which publisher contains the most unique authors
    //this code isn't done yet
    String bookPublisher = book.getPublisher();
    if (UniquePublisherMap.containsKey(bookPublisher))
    {
      String bookAuthor= book.getAuthor();
      UniquePublisherMap.get(bookPublisher).add(bookAuthor);

    }
    else
    {
       Set<String> setAuthor = new HashSet<>();
       String getTheAuthor = book.getAuthor();
       setAuthor.add(getTheAuthor);
       UniquePublisherMap.put(bookPublisher, setAuthor);
    }
  }
  public Set<Book> booksByAuthor(final String author)
  {
    if (author == null)
    {
      throw new IllegalArgumentException("Error: the author inputted was null");
    }
    if (author.isEmpty())
    {
      throw new IllegalArgumentException("Error: The author is empty");
    }
    if (!mapOfAuthors.containsKey(author)) //not sure if we should throw this. What does he mean by empty set?
    {
      Set<Book> emptySetForAuthor = new HashSet<>();
      return emptySetForAuthor;
    }
    Set<Book> booksAssocWithAuthor = mapOfAuthors.get(author);
    Set<Book> unmodmapOfAuthors = Collections.unmodifiableSet(booksAssocWithAuthor);
    return unmodmapOfAuthors;
  }
  public Set<Book> booksByPublisher(final String publisher)
  {
    if (publisher == null)
    {
      throw new IllegalArgumentException("Error: The publisher is null");
    }
    if (publisher.isEmpty())
    {
      throw new IllegalArgumentException("Error: The publisher is empty");
    }
    if (!mapOfPublisher.containsKey(publisher))
    {
      Set<Book> emptySetForPublisher = new HashSet<>();
      return emptySetForPublisher;
    }
    Set<Book> booksAssocWithPublisher = mapOfPublisher.get(publisher);
    Set<Book> unmodmapOfPublisher = Collections.unmodifiableSet(booksAssocWithPublisher);
    return unmodmapOfPublisher;
  }
  public Set<String> allTitles(final String author)
  {
    if (author == null)
    {
      throw new IllegalArgumentException("Error: The author is null");
    }
    if (author.isEmpty())
    {
      throw new IllegalArgumentException("Error: The author is empty");
    }
    if (!mapOfAuthors.containsKey(author))
    {
      Set<String> emptySetForAuthor = new HashSet<>();
      return emptySetForAuthor;
    }
    Set<Book> booksAssocWithAuthor = mapOfAuthors.get(author);
    Set<String> setOfTitles = new HashSet<>();
    for (Book titles: booksAssocWithAuthor)
    {
      setOfTitles.add(titles.getTitle());
    }
    return setOfTitles;
  }
  public String publisherMostUniqueAuthors()
  {
    if (UniquePublisherMap.isEmpty())//what does it mean no publisher has any author
    {
      return null;
    }
    String maxKey = null;
    //System.out.println(UniquePublisherMap.keySet());
    for (String key : UniquePublisherMap.keySet())
    {
      //System.out.println(key);
      //System.out.println("first trial:" +UniquePublisherMap.get(key).size());
      if (maxKey == null || UniquePublisherMap.get(key).size() >= UniquePublisherMap.get(maxKey).size())
      {
        maxKey = key;
      }
    }
    //Set<String> jonathan = UniquePublisherMap.get(maxKey);
    //for (String j:jonathan)
    //{
    //  System.out.println(j);
    //}
    //System.out.println("second trial" +UniquePublisherMap.get(maxKey).size());
    return maxKey;
  }
  public String authorMostUniqueBooks()
  {
    String maxKey = null;
    for (String key : mapOfAuthors.keySet())
    {
      if (maxKey == null || mapOfAuthors.get(key).size() > mapOfAuthors.get(maxKey).size())
      {
        maxKey = key;
      }
    }
    return maxKey;
  }
  public String publisherMostUniqueBooks()
  {
    String maxKey = null;
    for (String key : mapOfPublisher.keySet())
    {
      if (maxKey == null || mapOfPublisher.get(key).size() > mapOfPublisher.get(maxKey).size())
      {
        maxKey = key;
      }
    }
    return maxKey;
  }
  public int numberOfBooks()
  {
    return bookSet.size();
  }
  public int numberOfAuthors()
  {
    return mapOfAuthors.size();
  }
  public int numberOfPublishers()
  {
    return mapOfPublisher.size();
  }
  public static void main (String[] args)
  {
    if (args.length != 1)
    {
      final String msg = "Usage: AuthorsAndBooks name_of_input file";
      System.err.println (msg);
      throw new IllegalArgumentException(msg);
    }
    try
    {
      AuthorsAndBooks authorsAndBooks = new AuthorsAndBooks();
      final String inputFile = args[0];
      File Input = new File(inputFile);
      Scanner InputScanner = new Scanner(Input);
        if (InputScanner.hasNext())
        {
          InputScanner.nextLine();
        }
         //this allows me to skip the first line check that this is correct
        //long timeBef = System.currentTimeMillis();
        //long timeAft = System.currentTimeMillis();
        //System.out.println("time first line: " + (timeAft-timeBef));
        //timeBef = System.currentTimeMillis();
        while (InputScanner.hasNext())
        {
          try
          {
          String line = InputScanner.nextLine();
          String[] parts = line.split("[;]");
          String[] str = new String[8];
          for (int i = 0; i<parts.length; i++)
          {
            str[i] = parts[i].replaceAll("\"","");
          }
          Book book = new Book(str[0],str[1],str[2],str[3],str[4]);
          authorsAndBooks.add(book);
        }
        catch (IllegalArgumentException e)
        {
          System.out.println(e.getMessage());
        }
      }

      //timeAft = System.currentTimeMillis();
      //System.out.println("time loop: " + (timeAft-timeBef));
      try
      {
        System.out.printf("%s %,d%n", "AuthorsAndBooks main - Number of distinct books: ",authorsAndBooks.numberOfBooks());
        System.out.printf("%s %,d%n", "AuthorsAndBooks main - Number of distinct authors: ", authorsAndBooks.numberOfAuthors());
        System.out.printf("%s %,d%n","AuthorsAndBooks main - Number of distinct publishers: ", authorsAndBooks.numberOfPublishers());
        String richAuthor = authorsAndBooks.authorMostUniqueBooks();
        System.out.println("AuthorsAndBooks main - Author with the most books: "+richAuthor);
        String richPublisher = authorsAndBooks.publisherMostUniqueBooks();
        System.out.println("AuthorsAndBooks main - Publisher with the most books: "+richPublisher);
        //int jonathan = authorsAndBooks.booksByPublisher("Harlequin").size();
        //System.out.println(jonathan);
      }
      catch (IllegalArgumentException e)
      {
        System.out.println(e.getMessage());
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("File not found with the path specified");
    }
  }
}
