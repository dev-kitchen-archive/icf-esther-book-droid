package kitchen.dev.icfbooks.model.books;

import java.util.ArrayList;
import java.util.List;

import kitchen.dev.icfbooks.model.media.Media;

/**
 * Created by samue on 03/21/16.
 */
public class BookFactory {

    public List<Book> getAllBooks(){

        //return Ester book hardcoded
        List<Book> books = new ArrayList<>();
        books.add(new Book(){

        });

        return books;
    }
}
